package inbe.project.backoffice.Controller;

import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.Services.CustomerService;
import inbe.project.backoffice.Services.UserService;
import inbe.project.backoffice.domain.Customers;
import inbe.project.backoffice.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping(path = "/customer")
@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Customers>> signUp(@RequestBody @Validated SignUpDTO signUpDTO){

        Response<Customers> response = customerService.register(signUpDTO);

        if (response.getResponseBody()==null){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);

    }


}
