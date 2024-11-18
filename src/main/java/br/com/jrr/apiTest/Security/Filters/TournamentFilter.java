package br.com.jrr.apiTest.Security.Filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jrr.apiTest.App.Exceptions.ForbiddenException;
import br.com.jrr.apiTest.App.Exceptions.NotFoundException;
import br.com.jrr.apiTest.Tournament.TournamentService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TournamentFilter extends OncePerRequestFilter {

    @Autowired
    private TournamentService tournamentService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       
        try {
            tournamentService.getCurrentOpenJoin();

        } catch (NotFoundException | ForbiddenException e) {

            Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
            
            List<GrantedAuthority> authorities = new ArrayList<>(
                authentication.getAuthorities()
                );
            
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("UNLOCKED");
            authorities.add(authority);

            Object principal = authentication.getPrincipal();
            
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
                )
            );

        }
        
        filterChain.doFilter(request, response);
    }
    
}
