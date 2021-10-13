package customers.pojo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CustomerNotePojo {
	
	String type;
	String title;
	String content;
	
}
