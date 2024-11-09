package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullFullNameValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        String fullName = user.getFullName();
        return fullName != null && !fullName.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Full name is null";
    }
    
}
