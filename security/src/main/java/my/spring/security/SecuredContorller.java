package my.spring.security;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;

@RestController
public class SecuredContorller {

    @GetMapping("/protected")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_DEVELOPERS')")
    public String protectedEndpoint(Authentication authentication) { //spring injects Authentication
        // Here is the sample code to programatically get authorities from authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for(GrantedAuthority au : authorities) {
            System.out.println(authentication.getName() + " has role " + au.getAuthority());
        }

        return "this is protected for " + authentication.getName();
    }

    @GetMapping("/notprotected")
    public String notProtectedEndpoint() {
        return "this is NOT protected";
    }
}
