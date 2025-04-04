package com.github.philipepompeu.order_service.app.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.philipepompeu.order_service.app.dto.UserLoginDto;
import com.github.philipepompeu.order_service.domains.model.UserEntity;
import com.github.philipepompeu.order_service.domains.repository.UserRepository;
import com.github.philipepompeu.order_service.infra.auth.AuthFailureException;
import com.github.philipepompeu.order_service.infra.auth.TokenService;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public LoginService(UserRepository userRepository,PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String auth(UserLoginDto dto) throws AuthFailureException {

        
        UserEntity user = userRepository.findByUserName(dto.getUsername()).orElseThrow(() -> new AuthFailureException(String.format("User [ %s ] not found.", dto.getUsername())));

        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            String token = tokenService.generateToken(user);
            return token;
        }

        throw new AuthFailureException("Invalid credentials");
        
    }

}
