package br.com.jrr.apiTest.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jrr.apiTest.App.ResponseDTO;
import br.com.jrr.apiTest.Security.DTOS.LoginRequestDTO;
import br.com.jrr.apiTest.Security.DTOS.TokenResponseDTO;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseDTO ResponseDTO;

    @PostMapping("/login")
    @PermitAll
    public ResponseEntity<TokenResponseDTO> authenticate(@RequestBody @Valid LoginRequestDTO login) {
        return ResponseEntity.ok(authService.authenticate(login));
    }

    @PostMapping("logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseDTO> logout() {
        UserEntity user = userService.getCurrentUser();
        authService.revokeAllTokens(user);
        ResponseDTO.setHttpStatus(HttpStatus.OK);
        ResponseDTO.setMessage("Logout success");
        return ResponseEntity.ok(ResponseDTO);
    }

}