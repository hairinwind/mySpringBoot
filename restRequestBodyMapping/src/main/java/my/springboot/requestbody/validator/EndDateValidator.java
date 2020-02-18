package my.springboot.requestbody.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class EndDateValidator implements ConstraintValidator<EndDateConstraint, Object> {
	
    private String startDate;
    private String endDate;
 
    public void initialize(EndDateConstraint constraintAnnotation) {
        this.startDate = constraintAnnotation.startDate();
        this.endDate = constraintAnnotation.endDate();
    }

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		Object startDateValue = new BeanWrapperImpl(value).getPropertyValue(startDate);
		Object endDateValue = new BeanWrapperImpl(value).getPropertyValue(endDate);
		
		if ( startDateValue != null && endDateValue != null) {
			System.out.println("startDate..." + startDateValue);
			System.out.println("endDate..." + endDateValue);
			return !((LocalDate)startDateValue).isAfter((LocalDate)endDateValue);
		}
		
		return true;
	}

	
}
