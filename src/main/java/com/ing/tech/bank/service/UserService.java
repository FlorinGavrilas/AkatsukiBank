package com.ing.tech.bank.service;

import com.ing.tech.bank.model.dto.UserDto;
import com.ing.tech.bank.model.entities.User;
import com.ing.tech.bank.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto save(UserDto user) {
        User userEntity = new User(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword());
        User savedUser = userRepository.save(userEntity);

        return new UserDto(savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getPassword());
    }

    public List<UserDto> getAll() {
        return userRepository
                .findAll().stream()
                .map(user -> new UserDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUsername(),
                        user.getPassword())
                )
                .collect(Collectors.toList());
    }
}
