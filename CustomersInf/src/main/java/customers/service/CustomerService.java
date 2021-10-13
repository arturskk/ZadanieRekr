package customers.service;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.bean.MappingStrategy;

import customers.csv.CsvReader;
import customers.domain.CustomerData;
import customers.domain.CustomerNote;
import customers.pojo.CustomerNotePojo;
import customers.pojo.CustomerPojo;
import customers.repository.CustomerNoteRepository;
import customers.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerNoteRepository customerNoteRepository;

	private final static BigDecimal multiplier = BigDecimal.valueOf(0.8);

	public enum Kolor {
		TYPE_A1, TYPE_A2, TYPE_A5;
	}

	public List<CustomerNotePojo> getCustomerNotesByDate(Long customerId, LocalDate dateSince, LocalDate dateUntil) {
		return customerNoteRepository.findByCustomerIdAndNoteDayBetween(customerId, dateSince, dateUntil)
				.stream()
				.map(this::mapToCustomerNotePojo)
				.toList();
	}

	public CustomerNotePojo saveCustomerNote(Long customerId, CustomerNotePojo customerNotePojo) {
		CustomerNote customerNote = customerNoteRepository.save(mapToCustomerNote(customerNotePojo));
		return mapToCustomerNotePojo(customerNote);

	}
	
	private CustomerNotePojo mapToCustomerNotePojo(CustomerNote customerNote) {
		return CustomerNotePojo.builder()
				.title(customerNote.getTitle())
				.content(customerNote.getContent())
				.type(customerNote.getType())
				.build();
	}
	
	private CustomerNote mapToCustomerNote(CustomerNotePojo customerNote) {
		return CustomerNote.builder()
				.title(customerNote.getTitle())
				.content(customerNote.getContent())
				.type(customerNote.getType())
				.build();
	}
	
	
	public CustomerPojo getLatestCustomerData(Long customerId) {

		return mapToCustomerPojo(customerRepository.findFirstByIdOrderByInfoAsOfDateDesc(customerId));

	}

	public CustomerPojo getLatestCustomerData(Long customerId, LocalDate date) {

		return mapToCustomerPojo(customerRepository.findByIdAndInfoAsOfDate(customerId, date));

	}

	public List<CustomerPojo> readCustomersCsv(Path path, Class clazz, MappingStrategy mappingStrategy) {

		return CsvReader.beanBuilderExample(path, CustomerPojo.class, mappingStrategy)
				.stream()
				.filter(Objects::nonNull)
				.map(CustomerPojo.class::cast)
				.toList();

	}

	@Transactional
	public CustomerData mapToCustomer(CustomerPojo customerPojo) {
		if ((Kolor.TYPE_A1.name()
				.equals(customerPojo.getCustomerType())
				|| Kolor.TYPE_A5.name()
						.equals(customerPojo.getCustomerType()) && customerRiskClassChange(customerPojo))) {
			sendMail("Uprzejmie informuję, że zmieniła się klasa ryzyka dla klienta" + " [" + customerPojo.getId()
					+ "]");
		} else if (Kolor.TYPE_A2.name()
				.equals(customerPojo.getCustomerType()) && "BR_2".equals(customerPojo.getCustomerBusinessType())
				&& customerRiskClassChange(customerPojo)) {
			sendMail("Proszę o przegląd ryzyka klienta" + " [" + customerPojo.getId() + "]");
		}
		return CustomerData.builder()
				.customerId(customerPojo.getId())
				.infoAsOfDate(customerPojo.getInfoAsOfDate())
				.customerBusinessType(customerPojo.getCustomerBusinessType())
				.customerIncome(BigDecimal.valueOf(customerPojo.getCustomerIncome()))
				.customerName(customerPojo.getCustomerName())
				.customerRiskClass(customerPojo.getCustomerRiskClass())
				.customerStartDate(customerPojo.getCustomerStartDate())
				.customerType(customerPojo.getCustomerType())
				.r1(calculateR1(customerPojo))
				.r2(calculateR2(customerPojo))
				.build();
	}

	public CustomerPojo mapToCustomerPojo(CustomerData customer) {
		return CustomerPojo.builder()
				.id(customer.getCustomerId())
				.infoAsOfDate(customer.getInfoAsOfDate())
				.customerBusinessType(customer.getCustomerBusinessType())
				.customerIncome(customer.getCustomerIncome()
						.doubleValue())
				.customerName(customer.getCustomerName())
				.customerRiskClass(customer.getCustomerRiskClass())
				.customerStartDate(customer.getCustomerStartDate())
				.customerType(customer.getCustomerType())
				.r1(customer.getR1())
				.r2(customer.getR2())
				.build();
	}

	@Transactional
	public void saveCustomers(List<CustomerData> customers) {
		customerRepository.saveAll(customers);
	}

	public BigDecimal calculateR1(CustomerPojo customer) {
		BigDecimal r1 = BigDecimal.ZERO;
		BigDecimal customerIncome = BigDecimal.valueOf(customer.getCustomerIncome());
		if ((Kolor.TYPE_A1.name()).equals(customer.getCustomerType()) || ("A3".equals(customer.getCustomerRiskClass()) && customer.getCustomerType()
				.equals(Kolor.TYPE_A5.name()) )) {
			r1 = customerIncome.divide(BigDecimal.valueOf(10));
			r1 = r1.multiply(calculateF1(customer));
		} else if(Kolor.TYPE_A5.name().equals(customer.getCustomerType()) || Kolor.TYPE_A2.name().equals(customer.getCustomerType())) {
			r1 = customerIncome.divide(BigDecimal.valueOf(10));
		}
		return r1;

	}

	public BigDecimal calculateR2(CustomerPojo customer) {
		BigDecimal r2 = BigDecimal.ZERO;
		if (Kolor.TYPE_A5.name()
				.equals(customer.getCustomerType())
				|| customer.getCustomerType()
						.equals(Kolor.TYPE_A2.name())) {
			r2 = BigDecimal.valueOf(customer.getCustomerIncome())
					.divide(BigDecimal.valueOf(100));

		} else if (Kolor.TYPE_A1.name()
				.equals(customer.getCustomerType())) {
			r2 = BigDecimal.valueOf(customer.getCustomerIncome())
					.divide(BigDecimal.valueOf(1000));
			r2 = r2.multiply(BigDecimal.valueOf(calculateF2(customer)));
		}
		return r2;
	}

	@Transactional
	public BigDecimal calculateF1(CustomerPojo customer) {
		BigDecimal f1 = BigDecimal.ZERO;
		BigDecimal avgValue = BigDecimal.ZERO;
		if (Kolor.TYPE_A5.name()
				.equals(customer.getCustomerType())) {
			LocalDate dateBefore30Days = LocalDate.now()
					.minusDays(30);
			BigDecimal customerCurrentIncome = BigDecimal.valueOf(customer.getCustomerIncome())
					.multiply(multiplier);
			List<CustomerData> customer30daysCustomers = customerRepository
					.findAllByIdAndInfoAsOfDateGreaterThan(customer.getId(), dateBefore30Days);

			BigDecimal sumBigDecimal = customerRepository
					.findAllByIdAndInfoAsOfDateGreaterThan(customer.getId(), dateBefore30Days)
					.stream()
					.map(CustomerData::getCustomerIncome)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			if (customer30daysCustomers.size() != 0) {
				avgValue = sumBigDecimal.divide(new BigDecimal(customer30daysCustomers.size()));
			}

			if (customer30daysCustomers.size() == 0) {
				f1 = BigDecimal.valueOf(0.25);
			} else if (customerCurrentIncome.compareTo(avgValue) > 0) {
				f1 = BigDecimal.valueOf(0.3);
			} else if (customerCurrentIncome.compareTo(avgValue) < 0) {
				f1 = BigDecimal.valueOf(0.2);
			}
		} else if ("A3".equals(customer.getCustomerRiskClass()) && customer.getCustomerType()
				.equals(Kolor.TYPE_A5.name())) {
			f1 = "BR_3".equals(customer.getCustomerBusinessType()) ? BigDecimal.valueOf(0.1) : BigDecimal.valueOf(0.2);
		}
		return f1;
	}

	@Transactional
	public Double calculateF2(CustomerPojo customer) {
		Double f2 = 0.9;
		switch (customer.getCustomerBusinessType()) {
		case "BT_1":
			f2 = 0.5;
			break;
		case "BR_2":
			f2 = 0.5;
			break;
		case "BR_3":
			f2 = 0.8;
			break;
		}
		return f2;
	}

	public boolean customerRiskClassChange(CustomerPojo customerPojo) {
		CustomerData customer = customerRepository.findFirstByIdOrderByInfoAsOfDateDesc(customerPojo.getId());
		return customer != null ? !customer.getCustomerRiskClass()
				.equals(customerPojo.getCustomerRiskClass()) : false;
	}

	public void sendMail(String message) {
		System.out.println(message);
	}
}
