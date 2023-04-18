package inbe.project.backoffice.ResponseDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String accessToken;

    private String refreshToken;

    private String role;

}
