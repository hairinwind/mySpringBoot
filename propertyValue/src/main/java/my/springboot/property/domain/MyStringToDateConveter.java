package my.springboot.property.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConfigurationPropertiesBinding
public class MyStringToDateConveter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		log.info("...converting...{}", source);
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
