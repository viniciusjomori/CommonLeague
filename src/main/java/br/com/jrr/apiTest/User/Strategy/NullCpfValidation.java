package br.com.jrr.apiTest.User.Strategy;

import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.User.UserEntity;

@Component
public class NullCpfValidation implements IUserValidatio {

    @Override
    public boolean validate(UserEntity user) {
        String cpf = user.getCpf();
        return cpf != null && !cpf.isEmpty();
    }

    @Override
    public String getMessage() {
        return "CPF is null";
    }
    
}
