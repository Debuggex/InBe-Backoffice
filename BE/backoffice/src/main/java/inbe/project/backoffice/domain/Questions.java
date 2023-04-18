package inbe.project.backoffice.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "question",length = 2048,columnDefinition = "TEXT")
    private String question;

    @Column(name = "ref",length = 2048,columnDefinition = "TEXT")
    private String ref;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "questionId")
    @Column(columnDefinition = "TEXT",length = 2048)
    private List<Answers> answers = new ArrayList<>();

    public Questions addAnswer(Answers answer){
        answer.setQuestionId(this);
        this.answers.add(answer);
        return this;
    }

}
