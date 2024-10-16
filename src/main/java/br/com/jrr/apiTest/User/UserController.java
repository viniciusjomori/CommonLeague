package br.com.jrr.apiTest.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.User.DTO.UserRegisterDTO;
import br.com.jrr.apiTest.User.DTO.UserResponseDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;

    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        UserEntity entity = service.getCurrentUser();
        UserResponseDTO response = mapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PermitAll
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRegisterDTO dto) {
        UserEntity user = service.registerUser(dto);
        return ResponseEntity.ok(mapper.toResponse(user));
    }

}
