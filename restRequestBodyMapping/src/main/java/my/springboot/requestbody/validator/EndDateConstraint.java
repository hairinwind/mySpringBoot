package my.springboot.requestbody.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = EndDateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EndDateConstraint {

	String message() default "EndDate has to be after startDate";
	
	Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

	String startDate();
	 
    String endDate();
	
	@Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
		EndDateConstraint[] value();
    }
    
}
