package my.spring.junit.staticmethod;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClassHaveStaticMethod {

    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
