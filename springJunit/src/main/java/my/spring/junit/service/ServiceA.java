package my.spring.junit.service;

import my.spring.junit.subservice.SubServiceA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ServiceA {

    @Autowired
    private SubServiceA subServiceA;

    public String processString(String s) {
        String text = s + "123";
        return subServiceA.toUpperCase(text);
    }

    /**
     * e.g. convert "12/01/2021" to "2021-12-01"
     * @param dateStr
     * @param sourceFormat
     * @param targetFormat
     * @return
     */
    public String convertDateStringFormat(String dateStr, String sourceFormat, String targetFormat) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat(sourceFormat);
        Date date  = sdf1.parse(dateStr);
        SimpleDateFormat sdf2 = new SimpleDateFormat(targetFormat);
        return sdf2.format(date);
    }

}
