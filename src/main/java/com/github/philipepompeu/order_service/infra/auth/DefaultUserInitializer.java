package com.github.philipepompeu.order_service.infra.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.philipepompeu.order_service.domains.model.UserEntity;
import com.github.philipepompeu.order_service.domains.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @PostConstruct
    public void init() {
        if (userRepository.findByUserName("admin").isEmpty()) {            
            UserEntity user = new UserEntity();
            user.setUserName("admin");
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user); //criar um usuário default
        }
    }
    
}
