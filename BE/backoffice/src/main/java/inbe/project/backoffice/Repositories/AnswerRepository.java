package inbe.project.backoffice.Repositories;

import inbe.project.backoffice.domain.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answers,Long> {
}
