package com.railansantana.e_commerce.services.user;

import com.railansantana.e_commerce.domain.User;
import com.railansantana.e_commerce.dtos.auth.ResponseDTO;
import com.railansantana.e_commerce.dtos.auth.ResponseFullDataDTO;
import com.railansantana.e_commerce.dtos.auth.UpdateUserDTO;
import com.railansantana.e_commerce.infra.security.TokenService;
import com.railansantana.e_commerce.repository.UserRepository;
import com.railansantana.e_commerce.services.Exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataUserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public ResponseFullDataDTO findById(String id) {
        Optional<User> obj = userRepository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return new ResponseFullDataDTO(
                obj.get().getId(),
                obj.get().getName(),
                obj.get().getEmail(),
                obj.get().getOrders(),
                obj.get().getAddress(),
                obj.get().getCreatedAt());
    }

    public User findByIdAux(String id) {
        Optional<User> obj = userRepository.findById(id);
        if (obj.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return obj.get();
    }

    public ResponseDTO update(String id, UpdateUserDTO obj) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty() || obj == null) {
            throw new ResourceNotFoundException();
        }

        updateData(user.get(), obj);
        userRepository.save(user.get());
        return new ResponseDTO(
                user.get().getId(),
                user.get().getEmail(),
                user.get().getName(),
                tokenService.generateToken(user.get()),
                user.get().getAddress()
        );
    }

    private void updateData(User user, UpdateUserDTO obj) {
        user.setEmail(obj.email());
        user.setName(obj.name());
        user.setAddress(obj.address());
    }

    public void delete(String id) {
        userRepository.deleteById(findByIdAux(id).getId());
    }

    public void save(User user){
        userRepository.save(user);
    }
}
