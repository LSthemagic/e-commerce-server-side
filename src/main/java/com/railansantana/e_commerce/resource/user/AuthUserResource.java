package com.railansantana.e_commerce.resource.user;

import com.railansantana.e_commerce.dtos.auth.RequestLoginDTO;
import com.railansantana.e_commerce.dtos.auth.RequestRegisterDTO;
import com.railansantana.e_commerce.dtos.auth.ResponseDTO;
import com.railansantana.e_commerce.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/auth")
public class AuthUserResource {
    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<ResponseDTO> login (@RequestBody RequestLoginDTO req){
        ResponseDTO obj = userService.authenticateUser(req.email(), req.password());
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<RequestRegisterDTO> register (@RequestBody RequestRegisterDTO req){
        System.err.println(req);
        ResponseDTO res = userService.addUser(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(res.id()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
