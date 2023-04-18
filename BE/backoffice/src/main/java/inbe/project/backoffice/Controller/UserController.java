package inbe.project.backoffice.Controller;

import inbe.project.backoffice.RequestDTO.CustomerAssigningDTO;
import inbe.project.backoffice.RequestDTO.GetCustomers;
import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.CustomerDataResponse;
import inbe.project.backoffice.ResponseDTO.CustomerList;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.Services.UserService;
import inbe.project.backoffice.domain.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RequestMapping(path = "/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Users>> signUp(@RequestBody @Validated SignUpDTO signUpDTO){

        Response<Users> response = userService.register(signUpDTO);

        if (response.getResponseBody()==null){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PostMapping(value = "/getCustomers", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<CustomerList>> getCustomers(@RequestBody @Validated GetCustomers getCustomers) throws IOException {
        return new ResponseEntity<>(userService.getCustomers(getCustomers), HttpStatus.OK);

    }

    @PostMapping(value = "/assignAnalyst", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<CustomerDataResponse>> assignAnalyst(@RequestBody @Validated CustomerAssigningDTO customerAssigningDTO){

        return new ResponseEntity<>(userService.assignAnalyst(customerAssigningDTO),HttpStatus.OK);

    }
}
