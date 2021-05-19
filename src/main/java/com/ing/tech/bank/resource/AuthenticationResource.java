package com.ing.tech.bank.resource;

import com.ing.tech.bank.exceptions.InvalidUserOrPasswordException;
import com.ing.tech.bank.model.entities.AuthenticationRequest;
import com.ing.tech.bank.security.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/authenticate")
@EnableWebSecurity
public class AuthenticationResource {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthenticationResource(AuthenticationManager authenticationManager,
                                  JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    //    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationRequest request) {
       try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
       } catch (BadCredentialsException e) {
           throw new InvalidUserOrPasswordException("Invalid username or password");
       }

       return ResponseEntity.ok(jwtTokenUtil.generateAccessToken(request.getUsername()));
    }

}
