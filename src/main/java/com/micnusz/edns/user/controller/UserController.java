package com.micnusz.edns.user.controller;


import com.micnusz.edns.user.dto.UserRequest;
import com.micnusz.edns.user.dto.UserResponse;
import com.micnusz.edns.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid UserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.created(URI.create("/api/users/" + response.id())).body(response);
    }

    @GetMapping()
    public ResponseEntity<List<UserResponse>> getUsers() {
        List<UserResponse> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }
}
