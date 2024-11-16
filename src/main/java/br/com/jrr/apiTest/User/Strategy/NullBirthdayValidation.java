package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullBirthdayValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        return user.getBirthday() != null;
    }

    @Override
    public String getMessage() {
        return "Birthday is null";
    }
    
}
