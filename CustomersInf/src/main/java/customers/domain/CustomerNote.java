package customers.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name="customernote")
public class CustomerNote {

	@Id
	@Column(name = "id")
	Long id;
	@Column(name = "note_type")
	String type;
	@Column(name = "title")
	String title;
	@Column(name = "content")
	String content;
	@Column(name = "note_day")
	LocalDate noteDay;
	@Column(name = "customer_id")
	Long customerId;
}
