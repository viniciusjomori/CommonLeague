package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullPasswordValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        String password = user.getPassword();
        return password != null && !password.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Password is null";
    }
    
}
