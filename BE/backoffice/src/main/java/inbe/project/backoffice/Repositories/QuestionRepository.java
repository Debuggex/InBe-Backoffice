package inbe.project.backoffice.Repositories;

import inbe.project.backoffice.domain.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Questions,Long> {
}
