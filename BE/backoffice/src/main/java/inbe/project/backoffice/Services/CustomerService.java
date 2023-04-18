package inbe.project.backoffice.Services;

import inbe.project.backoffice.Constants.Constants;
import inbe.project.backoffice.Repositories.CustomerRepository;
import inbe.project.backoffice.Repositories.RoleRepository;
import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.ServiceInterface.CustomerInterface;
import inbe.project.backoffice.domain.Customers;
import inbe.project.backoffice.domain.Roles;
import inbe.project.backoffice.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class CustomerService implements CustomerInterface {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Response<Customers> register(SignUpDTO signUpDTO) {

        Response<Customers> response = new Response<>();
        AtomicReference<Boolean> isUserExists = new AtomicReference<>(false);

        customerRepository.findAll().forEach(
                user -> {
                    if (user.getEmail().equals(signUpDTO.getEmail())) {
                        isUserExists.set(true);
                    }

                }
        );

        if (isUserExists.get()) {
            response.setResponseCode(Constants.EMAIL_EXISTS);
            response.setResponseMessage("Customer is already registered with this Email. Try a Different One");
            response.setResponseBody(null);
            return response;
        }

        Customers user = new Customers();
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

        Customers savedUser = customerRepository.save(user);
        user.setPassword(null);
        response.setResponseCode(1);
        response.setResponseMessage("Customer registered Successfully");
        response.setResponseBody(user);
        return response;
    }
}
