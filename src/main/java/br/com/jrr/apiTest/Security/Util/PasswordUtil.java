package br.com.jrr.apiTest.Security.Util;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil extends BCryptPasswordEncoder {
    
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }
    
    public boolean matches(String rawPassword, UserDetails user) {
        return super.matches(rawPassword, user.getPassword());
    }
}
