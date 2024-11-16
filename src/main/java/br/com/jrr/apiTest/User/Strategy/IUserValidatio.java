package br.com.jrr.apiTest.User.Strategy;

import br.com.jrr.apiTest.User.UserEntity;

public interface IUserValidatio {
    
    boolean validate(UserEntity user);

    String getMessage();

}
