package com.ing.tech.bank.resource;

import com.ing.tech.bank.model.dto.UserDto;
import com.ing.tech.bank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

}
