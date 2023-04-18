package inbe.project.backoffice.Services;

import inbe.project.backoffice.Repositories.AnswerRepository;
import inbe.project.backoffice.Repositories.QuestionRepository;
import inbe.project.backoffice.RequestDTO.DownloadFileDTO;
import inbe.project.backoffice.RequestDTO.FormQueryDTO;
import inbe.project.backoffice.ResponseDTO.FormQueryResponse;
import inbe.project.backoffice.ServiceInterface.FormInterface;
import inbe.project.backoffice.domain.Answers;
import inbe.project.backoffice.domain.Questions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FormService implements FormInterface {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Value("${file.path}")
    private String filePath;

    private Path file;

    @Override
    public void getForm() {
        log.info("getForm------Started()");
        RestTemplate restTemplate= new RestTemplate();
        String uri = "https://api.typeform.com/forms/To6qRc9D";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization","Bearer tfp_FLLAh17MGV47eHy12TpPmCiWeFo6XHAEagECnBjtQeVU_fXzxy2BK3Yq7");

        HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
        Object response= restTemplate.exchange(uri, HttpMethod.GET,entity, Object.class).getBody();
        Map<String, Object> questions = (Map<String, Object>) response;
        List<Object> fetchedQuestions = (List<Object>) questions.get("fields");
        if (fetchedQuestions.size()==questionRepository.count()) {
            log.info("getForm------End()");
            return;
        }
        int index = (int) questionRepository.count();
        setUpQuestions(questions,index);
        log.info("getForm------End()");
    }

    @Override
    public Object getResponse(FormQueryDTO formQueryDTO) {
        log.info("getResponse------Started()");
        List<Answers> answers = answerRepository.findAll().
                stream().filter(
                answers1 -> answers1.getCustomerId().getId().equals(Long.valueOf(formQueryDTO.getCustomerId()))
        ).collect(Collectors.toList());
        List<Questions> questions = questionRepository.findAll();
        List<FormQueryResponse> response = new ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            FormQueryResponse formQueryResponse = new FormQueryResponse();
            formQueryResponse.setAnswer(answers.get(i).getAnswer());
            formQueryResponse.setQuestion(questions.get(i).getQuestion());
            formQueryResponse.setType(answers.get(i).getType());
            response.add(formQueryResponse);
        }
        log.info("getResponse------End()");
        return response;
    }

    @Override
    public String downloadFile(DownloadFileDTO downloadFileDTO) throws IOException {
        log.info("downloadFile------Started()");
        String[] temp = downloadFileDTO.getFileURL().split("\\.");
        String extension = temp[temp.length-1];
        String path = filePath+downloadFileDTO.getCustomerId()+"."+extension;
        RestTemplate restTemplate = new RestTemplate();
        String uri = downloadFileDTO.getFileURL();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
        headers.add("Authorization","Bearer tfp_FLLAh17MGV47eHy12TpPmCiWeFo6XHAEagECnBjtQeVU_fXzxy2BK3Yq7");
        HttpEntity<String> entity = new HttpEntity<>("parameters",headers);
        ResponseEntity<byte[]> result = restTemplate.exchange(uri,HttpMethod.GET,entity,byte[].class);
        byte[] content = result.getBody();
        Files.write(Paths.get(path), content);
        log.info("downloadFile------End()");
        return path;
    }

    @Override
    public Resource downloadFileFromStorage(DownloadFileDTO downloadFileDTO) throws IOException {
        log.info("downloadFile------Started()");
        String path = downloadFileDTO.getFileURL();
        Path path1 = Paths.get(path);
        Resource resource  = new UrlResource(path1.toUri());
        log.info("downloadFile------End()");
        return resource;
    }


    public void setUpQuestions(Map<String,Object> questions, int index){

        List<Object> questionList = (List<Object>) questions.get("fields");
        for (int i = index; i<questionList.size();i++) {
            Questions Q = new Questions();
            Map<String, Object> question = (Map<String, Object>) questionList.get(i);
            String title = (String) question.get("title");
            String ref = (String) question.get("ref");
            Q.setQuestion(title);
            Q.setRef(ref);
            Questions savedQuestion = questionRepository.save(Q);
        }

    }
}
