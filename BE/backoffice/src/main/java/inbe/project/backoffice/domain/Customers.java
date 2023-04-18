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
@Table(name = "Customers")
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role")
    private String role;

    @ManyToMany
            @JoinTable(
                    name = "users_assigned", joinColumns = @JoinColumn(name = "customer_id"),
                    inverseJoinColumns = @JoinColumn(name = "user_id")
            )
    List<Users> users_assigned = new ArrayList<>();

    public void addAnalyst(Users users){
        users_assigned.add(users);
    }

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "customerId")
    @Column(columnDefinition = "TEXT",length = 2048)
    private List<Answers>answers = new ArrayList<>();

    public Customers addAnswer(Answers answer){
        answer.setCustomerId(this);
        this.answers.add(answer);
        return this;
    }

}
