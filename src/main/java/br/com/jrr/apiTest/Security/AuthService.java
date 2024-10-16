package br.com.jrr.apiTest.Security;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Security.DTOS.LoginRequestDTO;
import br.com.jrr.apiTest.Security.DTOS.TokenResponseDTO;
import br.com.jrr.apiTest.Security.Util.JwtUtil;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserRepository;

@Service
public class AuthService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public TokenResponseDTO authenticate(LoginRequestDTO login) {
        
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                login.email(), login.password()
            )
        );

        UserEntity user = userRepository.findByEmail(login.email())
            .orElseThrow();

        if(user.getActive())
            return createTokenResponse(user);

        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED
        );
    }

    public TokenResponseDTO createTokenResponse(UserEntity user) {
        String token = jwtUtil.generateToken(user);
        saveUserToken(user, token);
        return new TokenResponseDTO(token);
    }

    public void saveUserToken(UserEntity user, String token) {
        revokeAllTokens(user);
        TokenEntity tokenEntity = TokenEntity.builder()
            .user(user)
            .token(token)
            .expirationDate(LocalDateTime.now().plusNanos(
                jwtExpiration * 1000000
            ))
            .build();
        tokenRepository.save(tokenEntity);
    }

    public void revokeAllTokens(UserEntity user) {
        Collection<TokenEntity> tokens = tokenRepository.findActiveByUser(user);
        tokens.forEach(token -> {
            token.setActive(false);
        });
        tokenRepository.saveAll(tokens);

    }

}