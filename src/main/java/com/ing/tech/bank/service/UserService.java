package com.ing.tech.bank.service;

import com.ing.tech.bank.model.dto.UserDto;
import com.ing.tech.bank.model.entities.User;
import com.ing.tech.bank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto save(UserDto user) {
        User userEntity = new User(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole());
        User savedUser = userRepository.save(userEntity);

        return new UserDto(savedUser.getUsername(), savedUser.getPassword(), user.getRole());
    }

    public List<UserDto> getAll() {
        return userRepository
                .findAll().stream()
                .map(user -> new UserDto(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole())
                )
                .collect(Collectors.toList());
    }
}
