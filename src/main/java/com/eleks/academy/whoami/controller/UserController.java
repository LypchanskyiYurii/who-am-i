package com.eleks.academy.whoami.controller;

import com.eleks.academy.whoami.model.request.UserRequestDto;
import com.eleks.academy.whoami.model.response.UserResponseDto;
import com.eleks.academy.whoami.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return this.userService.create(userRequestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> get(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.get(userId));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable("userId") Long userId) {
        userService.deleteById(userId);
    }

}
