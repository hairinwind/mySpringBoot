package my.springboot.jackson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyJacksonController {
	
	@Autowired
	MailProperties mailProperties;

	@GetMapping("/config")
    public ResponseEntity<MailProperties> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(mailProperties); 
    }
	
}
