package com.railansantana.e_commerce.services.user;

import com.railansantana.e_commerce.domain.User;
import com.railansantana.e_commerce.dtos.auth.RequestRegisterDTO;
import com.railansantana.e_commerce.dtos.auth.ResponseDTO;
import com.railansantana.e_commerce.infra.security.TokenService;
import com.railansantana.e_commerce.repository.user.UserRepository;
import com.railansantana.e_commerce.services.Exceptions.AuthenticateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseDTO addUser(RequestRegisterDTO user) {
        if (repository.findByEmail(user.email()).isPresent()) {
            throw new AuthenticateException("User already exists");
        }
        User obj = new User(null, user.name(), user.email(), passwordEncoder.encode(user.password()));
        repository.save(obj);
        return new ResponseDTO(obj.getId(), obj.getEmail(), obj.getName(), tokenService.generateToken(obj), obj.getAddress());
    }

    public ResponseDTO authenticateUser(String email, String password) {
        User user = repository.findByEmail(email).orElseThrow(() -> new AuthenticateException("User does not exist"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticateException("Incorrect password");
        }

        return new ResponseDTO(user.getId(), user.getEmail(), user.getName(), tokenService.generateToken(user), user.getAddress());
    }

}
