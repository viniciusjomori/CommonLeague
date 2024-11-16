package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullEmailValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        String email = user.getEmail();
        return email != null && !email.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Email is null";
    }
    
}
