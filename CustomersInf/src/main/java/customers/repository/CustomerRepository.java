package customers.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import customers.domain.CustomerData;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerData, Long> {

	 public CustomerData findFirstByIdOrderByInfoAsOfDateDesc(Long id);
	 public CustomerData findByIdAndInfoAsOfDate(Long id, LocalDate date);
	 public List<CustomerData> findAllByIdAndInfoAsOfDateGreaterThan(Long id, LocalDate date);
	
}
