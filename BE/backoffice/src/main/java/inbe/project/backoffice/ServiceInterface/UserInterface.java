package inbe.project.backoffice.ServiceInterface;

import inbe.project.backoffice.RequestDTO.CustomerAssigningDTO;
import inbe.project.backoffice.RequestDTO.GetCustomers;
import inbe.project.backoffice.RequestDTO.SignUpDTO;
import inbe.project.backoffice.ResponseDTO.CustomerDataResponse;
import inbe.project.backoffice.ResponseDTO.CustomerList;
import inbe.project.backoffice.ResponseDTO.Response;
import inbe.project.backoffice.domain.Users;

import java.io.IOException;

public interface UserInterface {

    Response<Users> register(SignUpDTO signUpDTO);

    Response<CustomerList> getCustomers(GetCustomers getCustomers) throws IOException;

    Response<CustomerDataResponse> assignAnalyst(CustomerAssigningDTO customerAssigningDTO);

}
