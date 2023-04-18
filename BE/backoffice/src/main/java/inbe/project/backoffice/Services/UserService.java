package inbe.project.backoffice.Services;

import inbe.project.backoffice.Constants.Constants;
import inbe.project.backoffice.Repositories.AnswerRepository;
import inbe.project.backoffice.Repositories.CustomerRepository;
import inbe.project.backoffice.Repositories.QuestionRepository;
import inbe.project.backoffice.Repositories.UserRepository;
import inbe.project.backoffice.RequestDTO.CustomerAssigningDTO;
import inbe.project.backoffice.RequestDTO.DownloadFileDTO;
import inbe.project.backoffice.RequestDTO.GetCustomers;
import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.CustomerDataResponse;
import inbe.project.backoffice.ResponseDTO.CustomerList;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.ServiceInterface.UserInterface;
import inbe.project.backoffice.domain.*;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class UserService implements UserDetailsService, UserInterface {


    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final PasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;

    private final FormService formService;

    private final AnswerRepository answerRepository;

    public UserService(UserRepository userRepository, QuestionRepository questionRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository, FormService formService, AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.formService = formService;
        this.answerRepository = answerRepository;
    }

    @Override
    public Response<Users> register(SignUpDTO signUpDTO){

        Response<Users> response = new Response<>();
        AtomicReference<Boolean> isUserExists = new AtomicReference<>(false);

        userRepository.findAll().forEach(
                user -> {
                    if (user.getEmail().equals(signUpDTO.getEmail())) {
                        isUserExists.set(true);
                    }

                }
        );

        if (isUserExists.get()) {
            response.setResponseCode(Constants.EMAIL_EXISTS);
            response.setResponseMessage("User is already registered with this Email. Try a Different One");
            response.setResponseBody(null);
            return response;
        }

        Users user = new Users();
        user.setEmail(signUpDTO.getEmail());
        user.setFirstName(signUpDTO.getFirstName());
        user.setLastName(signUpDTO.getLastName());
        //
        for (Roles role: Roles.values()) {
            if (role.toString().equals(signUpDTO.getRole().toUpperCase())) {
                user.setRole(role.toString().toUpperCase());
                break;
            }
        }
        if (user.getRole()==null) {
            response.setResponseCode(Constants.ROLE_NOT_EXISTS);
            response.setResponseMessage("Role does not exist. Please Contact Admin");
            response.setResponseBody(null);
            return response;
        }
        //
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

        Users savedUser = userRepository.save(user);
        user.setPassword(null);
        response.setResponseCode(1);
        response.setResponseMessage("User registered Successfully");
        response.setResponseBody(user);
        return response;
    }

    @Transactional
    @Override
    public Response<CustomerList> getCustomers(GetCustomers getCustomers) throws IOException {

        formService.getForm();
        log.info("Get Customer() --------------------- Start");
        List<Customers> allCustomers = customerRepository.findAll();
        List<Customers> customers = new ArrayList<>();
        if (getCustomers.getRole().equalsIgnoreCase("admin")||getCustomers.getRole().equalsIgnoreCase("supervisor")) {

            RestTemplate restTemplate= new RestTemplate();
            String uri = "https://api.typeform.com/forms/To6qRc9D/responses";

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("Authorization","Bearer tfp_FLLAh17MGV47eHy12TpPmCiWeFo6XHAEagECnBjtQeVU_fXzxy2BK3Yq7");

            HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
            Map<String,List<Object>> response = (Map<String, List<Object>>) restTemplate.exchange(uri, HttpMethod.GET,entity, Object.class).getBody();
            List<Object> items = response.get("items");
            if (allCustomers.size() == items.size()) {
                customers=allCustomers;
            }else {
                int size = allCustomers.size();
                for (int i = size; i < items.size(); i++) {
                    Map<String,List<Object>> item= (Map<String, List<Object>>) items.get(i);
                    List<Object> answers= item.get("answers");
                    Map<String,String> name = (Map<String,String>) answers.get(0);
                    Map<String,String> emailMap = (Map<String,String>) answers.get(1);
                    String email = emailMap.get("email");
                    String fullName = name.get("text");
                    List<Questions> questions = questionRepository.findAll();
                    boolean isCustomer = customerRepository.findAll().stream().anyMatch(
                            customers1 -> customers1.getEmail().equals(email)
                    );
                    if (isCustomer){
                        continue;
                    }
                    /**
                     * @Setting Up Customer DTO
                     * */
                    Customers addCustomer = new Customers();
                    addCustomer.setFirstName(fullName.split(" ")[0]);
                    addCustomer.setLastName(fullName.split(" ")[1]);
                    addCustomer.setEmail(email);
                    addCustomer.setPassword(null);
                    addCustomer.setRole("CUSTOMER");
                    Customers savedUser = customerRepository.save(addCustomer);
                    for (int k=0;k< questions.size();k++) {
                        boolean isAnswerExists = false;
                        int index = 0;
                        Questions questions1 = questions.get(k);
                        for (int j = 0; j < answers.size(); j++) {
                            Map<String, Object> value = (Map<String, Object>) answers.get(j);
                            Map<String, Object> field = (Map<String, Object>) value.get("field");
                            if (field.get("ref").equals(questions1.getRef())) {
                                isAnswerExists = true;
                                index = j;
                                break;
                            }
                        }
                        if (isAnswerExists) {
                            Map<String, Object> value = (Map<String, Object>) answers.get(index);
                            String type = (String) value.get("type");

                            if (type.equals("file_url")&&value.get(type)!=null) {
                                DownloadFileDTO downloadFileDTO = new DownloadFileDTO();
                                downloadFileDTO.setFileURL((String) value.get(type));
                                downloadFileDTO.setCustomerId(String.valueOf(savedUser.getId()));
                                String url = formService.downloadFile(downloadFileDTO);
                                Answers newAnswer = new Answers();
                                newAnswer.setAnswer(url);
                                newAnswer.setType(type);
                                Answers savedAnswer = answerRepository.save(newAnswer);
                                savedUser.addAnswer(savedAnswer);
                                questions1.addAnswer(savedAnswer);
                                continue;
                            }

                            if (type.equals("choice") || type.equals("choices")) {
                                Map<String,Object> answer = (Map<String, Object>) value.get(type);
                                StringBuilder temp= new StringBuilder();
                                if(type.equals("choice")){
                                    String label = String.valueOf(answer.get("label"));
                                    temp= new StringBuilder(label);
                                    Answers newAnswer = new Answers();
                                    newAnswer.setAnswer(temp.toString());
                                    newAnswer.setType(type);
                                    Answers savedAnswer = answerRepository.save(newAnswer);
                                    savedUser.addAnswer(savedAnswer);
                                    questions1.addAnswer(savedAnswer);
                                }else{
                                    List<String> label = (List<String>) answer.get("labels");
                                    for (int j = 0; j < label.size(); j++) {
                                        temp.append(label.get(j)).append("\n");
                                    }
                                    Answers newAnswer = new Answers();
                                    newAnswer.setAnswer(temp.toString());
                                    newAnswer.setType(type);
                                    Answers savedAnswer = answerRepository.save(newAnswer);
                                    savedUser.addAnswer(savedAnswer);
                                    questions1.addAnswer(savedAnswer);
                                }
                                continue;
                            }
                            String temp = (String) value.get(type);
                            Answers newAnswer = new Answers();
                            newAnswer.setAnswer(temp);
                            newAnswer.setType(type);
                            Answers savedAnswer = answerRepository.save(newAnswer);
                            savedUser.addAnswer(savedAnswer);
                            questions1.addAnswer(savedAnswer);

                        }else{
                            Answers newAnswer = new Answers();
                            newAnswer.setAnswer(null);
                            newAnswer.setType(null);
                            Answers savedAnswer = answerRepository.save(newAnswer);
                            savedUser.addAnswer(savedAnswer);
                            questions1.addAnswer(savedAnswer);
                        }
                    }

                }
                customers = customerRepository.findAll();
            }

        }else {
            for (Customers allCustomer : allCustomers) {
                List<Users> temp = allCustomer.getUsers_assigned();
                for (Users users : temp) {
                    if (users.getId().equals(Long.valueOf(getCustomers.getAnalystId()))) {
                        customers.add(allCustomer);
                    }
                }
            }
        }
        List<CustomerDataResponse> customerDataResponses = new ArrayList<>();
        CustomerList customerList = new CustomerList();
        Response<CustomerList> response=new Response<>();

        customers.forEach(
                customers1 -> {
                    CustomerDataResponse customerDataResponse = new CustomerDataResponse();
                    customerDataResponse.setId(String.valueOf(customers1.getId()));
                    customerDataResponse.setEmail(customers1.getEmail());
                    customerDataResponse.setFirstName(customers1.getFirstName());
                    customerDataResponse.setLastName(customers1.getLastName());
                    customerDataResponses.add(customerDataResponse);
                }
        );
        customerList.setCustomerDataResponses(customerDataResponses);
        response.setResponseBody(customerList);
        response.setResponseMessage("Customers fetched Successfully");
        response.setResponseCode(Constants.CUSTOMERS_FETCHED);
        log.info("Get Customer() --------------------- END");
        return response;
    }

    @Transactional
    @Override
    public Response<CustomerDataResponse> assignAnalyst(CustomerAssigningDTO customerAssigningDTO) {

        Response<CustomerDataResponse> response = new Response<>();
        CustomerDataResponse customerDataResponse = new CustomerDataResponse();
        Users users = userRepository.findById(Long.valueOf(customerAssigningDTO.getAnalystId())).get();
        Customers customers = customerRepository.findById(Long.valueOf(customerAssigningDTO.getCustomerId())).get();

        Optional<Users> temp= customers.getUsers_assigned().stream().filter(
                users1 -> users1.getId().equals(users.getId())
        ).findFirst();

        if (temp.isPresent()) {
            response.setResponseBody(null);
            response.setResponseMessage("Customer is Already Assigned to this Analyst.");
            response.setResponseCode(Constants.RESPONSE_FAILURE);
            return response;
        }

        customers.addAnalyst(users);
        users.addCustomer(customers);

        customerDataResponse.setId(String.valueOf(customers.getId()));
        customerDataResponse.setFirstName(customers.getFirstName());
        customerDataResponse.setLastName(customers.getLastName());
        customerDataResponse.setEmail(customers.getEmail());

        response.setResponseBody(customerDataResponse);
        response.setResponseMessage("Customer Assigned Successfully");
        response.setResponseCode(Constants.CUSTOMER_ASSIGNED);
        return response;
    }


    public void saveNewAnswers() {



    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findById(userRepository.findAll().stream().filter(
                user1 -> user1.getEmail().equals(username)
        ).findFirst().get().getId());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with this email does not exist");
        } else {
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String role=user.get().getRole();
            authorities.add(new SimpleGrantedAuthority(role));
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        }
    }
}
