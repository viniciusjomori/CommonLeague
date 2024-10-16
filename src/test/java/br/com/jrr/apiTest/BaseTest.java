package br.com.jrr.apiTest;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;
import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class BaseTest {

    @Autowired
    private UserService userService;

    protected UserEntity user1;
    protected UserEntity user2;
    protected UserEntity user3;
    protected UserEntity user4;
    protected UserEntity user5;
    protected UserEntity user6;

    @Transactional
    @BeforeAll
    public void baseSetup() {
        UserRegisterDTO register = new UserRegisterDTO(
            "nicktest",
            "test@email.com",
            "Full Name Test",
            "471.167.670-88",
            LocalDate.parse("1914-08-26"),
            "test@123"
        );
        user1 = userService.registerUser(register);

        UserRegisterDTO register2 = new UserRegisterDTO(
            "nicktest2",
            "test2@email.com",
            "Full Name Test 2",
            "520.190.330-46",
            LocalDate.parse("1915-08-26"),
            "test@123"
        );
        user2 = userService.registerUser(register2);

        UserRegisterDTO register3 = new UserRegisterDTO(
            "nicktest3",
            "test3@email.com",
            "Full Name Test 3",
            "152.107.280-99",
            LocalDate.parse("1916-08-26"),
            "test@123"
        );
        user3 = userService.registerUser(register3);

        UserRegisterDTO register4 = new UserRegisterDTO(
            "nicktest4",
            "test4@email.com",
            "Full Name Test 4",
            "409.950.420-47",
            LocalDate.parse("1917-08-26"),
            "test@123"
        );
        user4 = userService.registerUser(register4);

        UserRegisterDTO register5 = new UserRegisterDTO(
            "nicktest5",
            "test5@email.com",
            "Full Name Test 5",
            "737.049.800-05",
            LocalDate.parse("1918-08-26"),
            "test@123"
        );
        user5 = userService.registerUser(register5);

        UserRegisterDTO register6 = new UserRegisterDTO(
            "nicktest6",
            "test6@email.com",
            "Full Name Test 6",
            "837.723.530-70",
            LocalDate.parse("1919-08-26"),
            "test@123"
        );
        user6 = userService.registerUser(register6);
    } 
    
    @Transactional
    protected void defineCurrentUser(UserEntity user) {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                user,
                null,
                new ArrayList<>()
            )
        );
    }

}

