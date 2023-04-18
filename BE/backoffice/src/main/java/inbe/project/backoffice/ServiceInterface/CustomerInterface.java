package inbe.project.backoffice.ServiceInterface;

import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.domain.Customers;
import inbe.project.backoffice.domain.Users;

public interface CustomerInterface {

    Response<Customers> register(SignUpDTO signUpDTO);
}
