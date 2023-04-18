package inbe.project.backoffice.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerList {

    private List<CustomerDataResponse> customerDataResponses;

}
