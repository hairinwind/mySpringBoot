package my.spring.junit.service;

import my.spring.junit.subservice.SubServiceA;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceA2ndTest.ServiceA2ndTestContextConfiguration.class)
/**
 * if @SpringBootTest is used without any parameters, it is loading the entire spring container.
 * the classes in @SpringBootTest is point to a class with annotation @Configuration
 * it does not work if the class has annotation @TestConfiguration
 */
public class ServiceA2ndTest {

    @Autowired
    private ServiceA serviceA;

    @MockBean
    private SubServiceA subServiceA;

    /**
     * If inner class is used here, it has to be static
     * Or put it in another java file
     */
    @Configuration
    static class ServiceA2ndTestContextConfiguration {
        @Bean
        public ServiceA serviceA() {
            return new ServiceA();
        }
    }

    @Test
    public void testProcessString() {
        String text = "test123";
        String expectedResult = "TEST123";
        when(subServiceA.toUpperCase(text)).thenReturn(expectedResult);

        String result = serviceA.processString("test");
        assertEquals(expectedResult, result);
    }

    @Test
    public void testConvertDateStringFormat() throws ParseException {
        String dateStr = "12/01/2021";
        String sourceFormat = "mm/dd/yyyy";
        String targetFormat = "yyyy-mm-dd";
        String result = serviceA.convertDateStringFormat(dateStr, sourceFormat, targetFormat);
        assertEquals("2021-12-01", result);
    }


}