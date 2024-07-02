package com.railansantana.e_commerce.repository.user;


import com.railansantana.e_commerce.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    String findByPassword(String password);
}
