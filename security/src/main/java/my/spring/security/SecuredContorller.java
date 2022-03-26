package my.spring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecuredContorller {

    @GetMapping("/protected")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_DEVELOPERS')")
    public String protectedEndpoint() {
        return "this is protected";
    }

    @GetMapping("/notprotected")
    public String notProtectedEndpoint() {
        return "this is NOT protected";
    }
}
