package com.railansantana.e_commerce.resource.user;

import com.railansantana.e_commerce.dtos.auth.ResponseDTO;
import com.railansantana.e_commerce.services.user.DataUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class DataUserResource {

    private final DataUserService userService;

    public DataUserResource(DataUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> findById(@PathVariable String id, @RequestHeader String Authorization) {
        return ResponseEntity.ok().body(userService.findById(id, Authorization));
    }

    

}
