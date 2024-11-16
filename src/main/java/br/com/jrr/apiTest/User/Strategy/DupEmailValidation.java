package br.com.jrr.apiTest.User.Strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserRepository;

@Component
public class DupEmailValidation implements IUserValidatio {

    @Autowired
    private UserRepository repository;

    @Override
    public boolean validate(UserEntity user) {
        return repository.findByEmail(user.getEmail())
            .filter(existingUser -> !existingUser.getId().equals(user.getId()))
            .isEmpty();
    }

    @Override
    public String getMessage() {
        return "Email already exists";
    }
    
}
