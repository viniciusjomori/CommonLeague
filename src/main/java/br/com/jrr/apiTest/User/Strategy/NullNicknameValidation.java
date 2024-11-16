package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullNicknameValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        String nickname = user.getNickname();
        return nickname != null && !nickname.isEmpty();
    }

    @Override
    public String getMessage() {
        return "Nickname is null";
    }
    
}
