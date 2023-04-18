package inbe.project.backoffice.Repositories;

import inbe.project.backoffice.domain.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customers,Long> {
}
