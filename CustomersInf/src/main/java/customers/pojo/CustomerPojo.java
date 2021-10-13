package customers.pojo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByPosition;
import com.opencsv.bean.CsvDate;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class CustomerPojo extends CsvBean {
	
	
	    @CsvDate(value = "yyyy-MM-dd")
	    @CsvBindByPosition(position = 0)
	    private LocalDate infoAsOfDate;

	    @CsvBindByPosition(position = 1)
	    private Long id;

	    @CsvBindByPosition(position = 2)
	    private String customerName;

	    @CsvDate(value = "yyyy-MM-dd")
	    @CsvBindByPosition(position = 3)
	    private LocalDate customerStartDate;
	    
	    @CsvBindByPosition(position = 4)
	    private String customerType;
	    
	    @CsvBindByPosition(position = 5)
	    private Double customerIncome;
	    
	    @CsvBindByPosition(position = 6)
	    private String customerRiskClass;
	    
	    @CsvBindByPosition(position = 7)
	    private String customerBusinessType;
	    
	    private BigDecimal r1;
	    
	    private BigDecimal r2;
	    

}


