package customers.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name="customerdata")
@Data
@Builder
public class CustomerData {
	
	@Id
	private Long id;
	@Column(name = "customer_id")
	private Long customerId;
	@Column(name = "info_as_of_date")
	private LocalDate infoAsOfDate;
	@Column(name = "customer_name")
	private String customerName;
	@Column(name = "customer_start_date")
	private LocalDate customerStartDate;
	@Column(name = "customer_type")
	private String customerType;
	@Column(name = "customer_income")
	private BigDecimal customerIncome;
	@Column(name = "customer_risk_class")
	private String customerRiskClass;
	@Column(name = "customer_business_type")
	private String customerBusinessType;
	@Column(name = "r1")
	private BigDecimal r1;
	@Column(name = "r2")
	private BigDecimal r2;
}