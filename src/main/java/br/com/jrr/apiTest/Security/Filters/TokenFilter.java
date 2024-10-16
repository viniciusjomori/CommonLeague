package br.com.jrr.apiTest.Security.Filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jrr.apiTest.Security.TokenEntity;
import br.com.jrr.apiTest.Security.TokenRepository;
import br.com.jrr.apiTest.Security.Util.JwtUtil;
import br.com.jrr.apiTest.User.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        UserEntity principal = null;
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(jwtUtil.isTokenValid(authHeader)) {
            String token = authHeader.substring(7);

            Optional<TokenEntity> optional = tokenRepository.findByToken(token);

            if(optional.isPresent()) {
                TokenEntity tokenEntity = optional.get();
                    
                if(tokenEntity.getActive() && tokenEntity.isNonExpired()) {
                    UserEntity user = tokenEntity.getUser();
                    String subject = jwtUtil.extractSubject(token);
    
                    if(user.getUsername().equals(subject) && user.getActive()) {
                        principal = user;
                        authorities = user.getAuthorities();
                    }
                }
            }
        }
        
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities
            )
        );
        
        filterChain.doFilter(request, response);
    }

}
