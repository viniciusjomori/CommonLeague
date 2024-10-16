package br.com.jrr.apiTest.Security.Filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.jrr.apiTest.Team.Entity.TeamJoinEntity;
import br.com.jrr.apiTest.Team.Service.TeamService;
import br.com.jrr.apiTest.User.UserEntity;
import br.com.jrr.apiTest.User.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CaptainFilter extends OncePerRequestFilter {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        UserEntity currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            
            Optional<TeamJoinEntity> optional = teamService.findActiveByUser(currentUser);
            if(optional.isPresent()) {

                TeamJoinEntity join = optional.get();

                List<GrantedAuthority> authorities = new ArrayList<>(
                    SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getAuthorities()
                );

                authorities.add(
                    new SimpleGrantedAuthority(join.getRole().toString())
                );

                System.out.println(authorities);

                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                        currentUser,
                        null,
                        authorities
                    )
                );
                
            }

        }

        filterChain.doFilter(request, response);
        
    }
    
}
