package my.springboot.performancemonitor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//If you want both builder and noArgsConstructor, you need explicitly put in @NoArgsConstructor and @AllArgsConstructor
public class Person {
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
}
