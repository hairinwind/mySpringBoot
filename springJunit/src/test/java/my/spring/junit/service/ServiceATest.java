package my.spring.junit.service;

import my.spring.junit.subservice.SubServiceA;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ServiceATest {

    @Autowired
    private ServiceA serviceA;

    @MockBean
    private SubServiceA subServiceA;

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