package inbe.project.backoffice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Answers")
public class Answers {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "answer",length = 2048,columnDefinition = "TEXT")
    private String answer;

    @Column(name = "answerType")
    private String type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customers customerId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "questionId")
    private Questions questionId;



}
