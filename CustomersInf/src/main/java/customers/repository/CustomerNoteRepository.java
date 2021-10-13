package customers.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import customers.domain.CustomerNote;

@Repository
public interface CustomerNoteRepository extends JpaRepository<CustomerNote, Long> {
	public List<CustomerNote> findByCustomerIdAndNoteDayBetween(Long customerId, LocalDate dateSince,
			LocalDate dateUntil);
}
