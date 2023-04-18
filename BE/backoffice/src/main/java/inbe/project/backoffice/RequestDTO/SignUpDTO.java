package inbe.project.backoffice.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpDTO {

    private String role;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
