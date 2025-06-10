package com.github.philipepompeu.order_service.infra.auth;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.philipepompeu.order_service.domains.model.UserEntity;

import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class TokenService {

    private String secret = "order-service-secret-key";//TODO: move to properties
    private String issuer = "order-service";

    public String generateToken(UserEntity user){
        try {
            
            
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            String token = JWT.create()
                            .withIssuer(issuer)
                            .withClaim("roles", user.getRoles().stream().toList())
                            .withSubject(user.getUsername())
                            .withExpiresAt(this.generateExperationDate())
                            .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {   
            
            throw new RuntimeException(String.format("Falha na autenticação JWT [ %s ]", e.getMessage()));//TODO: adicionar tratamento no GlobalExceptionHandler
        } catch (Exception e) {
            // TODO: handle exception
        }

        return "";
    }

    public Optional<String> validateToken(String token){

        try {

            Algorithm algorithm = Algorithm.HMAC256(secret);            
            
            return Optional.of(JWT.require(algorithm)
                        .withIssuer(issuer)                           
                        .build()
                        .verify(token)
                        .getSubject());
            
        } catch (JWTVerificationException e) {
            
        }

        return Optional.empty();
    }

    private Instant generateExperationDate(){
        
        return LocalDateTime.now().plusMinutes(20).toInstant(ZoneOffset.of("-3"));
    }
    
    
}
