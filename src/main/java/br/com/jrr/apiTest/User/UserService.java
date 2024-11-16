package br.com.jrr.apiTest.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.jrr.apiTest.App.Exceptions.BadRequestException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;
import br.com.jrr.apiTest.User.DTO.UserUpdateDTO;
import br.com.jrr.apiTest.User.Enum.UserType;
import br.com.jrr.apiTest.User.Strategy.IUserValidatio;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private List<IUserValidatio> validations;

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
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserEntity registerUser(UserRegisterDTO dto) {
        UserEntity entity = mapper.toEntity(dto);

        String password = passwordEncoder.encode(dto.password());
        entity.setPassword(password);

        entity.setRole(UserType.ROLE_CLIENT);

        return saveUser(entity);
    }

    public UserEntity updateUser(UserUpdateDTO dto) {
        UserEntity entity = getCurrentUser();
        entity = mapper.updateUser(dto, entity);

        if(dto.password() != null) {
            String password = passwordEncoder.encode(dto.password());
            entity.setPassword(password);
        }

        return saveUser(entity);
    }

    public Page<UserEntity> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findAll(pageable);
    }

    public Page<UserEntity> findByNickname(String nickname, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findByNickname(nickname, pageable);
    }

    public UserEntity findByNickname(String nickname) {
        return repository.findByNickname(nickname)
            .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private UserEntity saveUser(UserEntity user) {
        validateUser(user);
        return repository.save(user);
    }

    private void validateUser(UserEntity user) {
        List<String> errors = new ArrayList<>();

        for (IUserValidatio validation : validations) {
            if(!validation.validate(user))
                errors.add(validation.getMessage());
        }

        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

}