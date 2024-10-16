package br.com.jrr.apiTest.User;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserEntity) {
            UUID id = ((UserEntity) principal).getId();
            return repository.findById(id).get();
        }
        else return null;
    }

    public UserEntity findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public UserEntity registerUser(UserRegisterDTO dto) {
        UserEntity entity = mapper.toEntity(dto);

        String password = passwordEncoder.encode(dto.password());
        entity.setPassword(password);

        entity.setRole(UserType.ROLE_CLIENT);

        return repository.save(entity);
    }

}