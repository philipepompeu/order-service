package com.github.philipepompeu.order_service.infra.auth;

import java.util.Set;
import java.util.HashSet;

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

            Set<String> roles = new HashSet<String>(){
                {
                    add("ROLE_ADMIN");
                    add("ROLE_USER");
                }
            };
            user.setRoles(roles);
            
            userRepository.save(user); //criar um usuário default
        }
        if (userRepository.findByUserName("user").isEmpty()) {            
            UserEntity user = new UserEntity();
            user.setUserName("user");
            user.setPassword(passwordEncoder.encode("123"));

            Set<String> roles = new HashSet<String>(){
                {                    
                    add("ROLE_USER");
                }
            };
            user.setRoles(roles);
            
            userRepository.save(user); //criar um usuário default
        }
    }

    
}
