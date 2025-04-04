package com.github.philipepompeu.order_service.infra.auth;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.security.auth.login.FailedLoginException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.philipepompeu.order_service.domains.model.UserEntity;
import com.github.philipepompeu.order_service.domains.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository){
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    private String recoverToken(HttpServletRequest request){

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;

        return authHeader.replace("Bearer ", "");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token =this.recoverToken(request);

        Optional<String> validToken = tokenService.validateToken(token);       
        
        if (validToken.isPresent()) {
            String login = validToken.get();            

            UserEntity user;
            
            user = userRepository.findByUserName(login).orElseThrow(() -> new AuthFailureException(String.format("User [ %s ] not found.", login)));            

            List<SimpleGrantedAuthority> authorities = user.getAuthorities();

            var authenticatedUser = new UsernamePasswordAuthenticationToken(user, null,authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            
        }
        filterChain.doFilter(request, response);
    }
    
}
