package customers.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.opencsv.bean.ColumnPositionMappingStrategy;

import customers.domain.CustomerData;
import customers.pojo.CustomerPojo;

@Component
public class CustomerUpdateTask implements InitializingBean {

	@Autowired
	CustomerService customerService;

	@Autowired
	private Environment environment;

	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Value("${customers_csv_path}")
	private String customerCsvPath;

	@Override
	public void afterPropertiesSet() throws Exception {
		String pathString = customerCsvPath;
		Path path = Paths.get(customerCsvPath);
		Long delayTime;

		Runnable task = () ->
			{
				List<CustomerPojo> customersPojos = customerService.readCustomersCsv(path, CustomerPojo.class,
						new ColumnPositionMappingStrategy());
				List<CustomerData> customers = customersPojos.stream()
						.map(c -> customerService.mapToCustomer(c))
						.toList();
				customerService.saveCustomers(customers);

			};

		final Long initialDelay = LocalDateTime.now()
				.until(LocalDate.now()
						.plusDays(1)
						.atTime(1, 24), ChronoUnit.MINUTES);

		if (initialDelay > TimeUnit.DAYS.toMinutes(1)) {
			delayTime = LocalDateTime.now()
					.until(LocalDate.now()
							.atTime(1, 24), ChronoUnit.MINUTES);
		} else {
			delayTime = initialDelay;
		}
		scheduler.scheduleAtFixedRate(task, delayTime, TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES);
	}

	@PostConstruct
	public void postConstruct() {
	}

}