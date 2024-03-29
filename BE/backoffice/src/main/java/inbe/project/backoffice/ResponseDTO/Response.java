package inbe.project.backoffice.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<ResponseType> {

    private Integer responseCode;

    private String responseMessage;

    private ResponseType responseBody;
}
