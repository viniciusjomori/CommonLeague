package br.com.jrr.apiTest.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.server.ResponseStatusException;

import br.com.jrr.apiTest.Security.Filters.CaptainFilter;
import br.com.jrr.apiTest.Security.Filters.TokenFilter;
import br.com.jrr.apiTest.Security.Filters.TournamentFilter;
import br.com.jrr.apiTest.User.UserRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private TokenFilter tokenFilter;

    @Autowired
    private CaptainFilter captainFilter;

    @Autowired
    private TournamentFilter tournamentFilter;
    
    @Autowired
    private UserRepository userRepository;
    
    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> {
                csrf.disable();
            })
            .headers(h -> {
                h.frameOptions(fo -> {
                    fo.disable();
                });
            })
            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(captainFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(tournamentFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}