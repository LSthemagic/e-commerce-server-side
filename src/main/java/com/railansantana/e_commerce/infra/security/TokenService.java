package com.railansantana.e_commerce.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.railansantana.e_commerce.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${spring.secret.jwt}")
    private String secret;

    public String generateToken (User obj){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(obj.getEmail())
                    .withExpiresAt(expiresAt())
                    .sign(algorithm);
        }catch(JWTCreationException e){
            throw new RuntimeException("Error while creating JWT");
        }
    }

    private Instant expiresAt () {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.UTC);
    }

    public String validToken (String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException e){
            return null;
        }
    }
}
