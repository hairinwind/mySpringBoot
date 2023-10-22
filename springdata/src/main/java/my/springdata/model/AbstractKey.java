package my.springdata.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class AbstractKey {
	
	public String serialize() {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(this);
			so.flush();
			return bo.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
