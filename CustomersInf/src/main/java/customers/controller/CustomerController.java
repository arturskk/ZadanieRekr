package customers.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import customers.pojo.CustomerNotePojo;
import customers.pojo.CustomerPojo;
import customers.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	@GetMapping(value="/customer/{customer_id}")
	public CustomerPojo customers(@PathVariable Long customerId) {
		return customerService.getLatestCustomerData(customerId);

	}

	@GetMapping(value="/customer/{customer_id}", params = {"date"})
	public CustomerPojo customersByDate(@PathVariable Long customerId, @RequestParam(name="date") LocalDate date) {
		return customerService.getLatestCustomerData(customerId);

	}

	@PostMapping(value="/customer/{customer_id}/note")
	public CustomerPojo customersNotes(@PathVariable Long customerId, @RequestBody CustomerNotePojo note) {
		return customerService.getLatestCustomerData(customerId);
	}

	@GetMapping(value="/customer/{customer_id}/note")
	public List<CustomerNotePojo> customers(@PathVariable Long customerId, @RequestParam(name="since") LocalDate since,
			@RequestParam(name="until") LocalDate until) {
		return customerService.getCustomerNotesByDate(customerId, since, until);

	}
}
