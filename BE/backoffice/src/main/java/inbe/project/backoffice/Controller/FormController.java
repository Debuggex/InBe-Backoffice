package inbe.project.backoffice.Controller;

import inbe.project.backoffice.RequestDTO.DownloadFileDTO;
import inbe.project.backoffice.RequestDTO.FormQueryDTO;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.Services.FormService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.web.header.Header;
import org.springframework.util.StreamUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping(path = "/form")
@RestController
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping(value = "/getResponse")
    public ResponseEntity<?> getResponse(@Validated @RequestBody FormQueryDTO formQueryDTO){
        return new ResponseEntity<>(formService.getResponse(formQueryDTO),HttpStatus.OK);
    }

    @PostMapping(value = "/downloadFile")
    public Resource downloadFile(@Validated @RequestBody DownloadFileDTO downloadFileDTO) throws IOException {
        return formService.downloadFileFromStorage(downloadFileDTO);
    }

}
