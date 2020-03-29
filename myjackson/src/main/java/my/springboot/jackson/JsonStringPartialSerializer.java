package my.springboot.jackson;

import java.io.IOException;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonStringPartialSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String output = "";
		if (!StringUtils.isEmpty(value)) {
			output = value.substring(0, value.length() / 2);
		}
		gen.writeString(output);
	}

}
