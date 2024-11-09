package br.com.jrr.apiTest.User.Strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.jrr.apiTest.App.Util.CpfUtil;
import br.com.jrr.apiTest.User.UserEntity;

@Component
public class CpfValidValidation implements IUserValidatio{
    
    @Autowired
    private CpfUtil cpfUtil;

    @Override
    public boolean validate(UserEntity user) {
        String cpf = user.getCpf();
        return cpfUtil.isValid(cpf);
    }

    @Override
    public String getMessage() {
        return "CPF is not valid";
    }



}
