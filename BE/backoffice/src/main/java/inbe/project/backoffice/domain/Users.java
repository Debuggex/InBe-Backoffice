package inbe.project.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FirstName", length = 40)
    private String firstName;

    @Column(name = "LastName", length = 40)
    private String lastName;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role;

    @ManyToMany(mappedBy = "users_assigned")
    List<Customers> customers = new ArrayList<>();

    public void addCustomer(Customers customer){
        customers.add(customer);
    }

}
