package com.fse.login.controller;

import com.fse.login.model.AuthenticationRequest;
import com.fse.login.model.AuthenticationResponse;
import com.fse.login.model.MyUserDetails;
import com.fse.login.model.User;
import com.fse.login.service.LoginServiceImpl;
import com.fse.login.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private LoginServiceImpl loginService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping(value = "/login")
    public ResponseEntity<?> userLogin(@RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect credentials", HttpStatus.UNAUTHORIZED);
        }

        final MyUserDetails userDetails = loginService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateJwtToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        loginService.addUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
