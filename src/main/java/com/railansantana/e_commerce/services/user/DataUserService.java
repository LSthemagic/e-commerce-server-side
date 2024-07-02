package com.railansantana.e_commerce.services.user;

import com.railansantana.e_commerce.domain.user.User;
import com.railansantana.e_commerce.dtos.auth.ResponseDTO;
import com.railansantana.e_commerce.infra.security.TokenService;
import com.railansantana.e_commerce.repository.user.UserRepository;
import com.railansantana.e_commerce.services.Exceptions.AuthenticateException;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataUserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    private String validateToken(String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        System.err.println("Token in validateToken: " + token);
        String tokenValid = tokenService.validToken(token);
        if (tokenValid == null) {
            throw new AuthenticateException("Invalid token");
        }
        return token;
    }

    public ResponseDTO findById(String id, String token) {
        Optional<User> obj = userRepository.findById(id);
        if(obj.isEmpty()){
            throw new ResourceNotFoundException();
        }
        return new ResponseDTO(obj.get().getId(), obj.get().getEmail(), obj.get().getName(), validateToken(token),obj.get().getAddress());
    }

    public User findByEmail(String email) {
        Optional<User> obj = userRepository.findByEmail(email);
        if(obj.isEmpty()){
            throw new ResourceNotFoundException();
        }
        return obj.get();
    }

}
