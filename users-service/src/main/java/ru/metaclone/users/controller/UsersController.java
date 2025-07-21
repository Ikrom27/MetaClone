package ru.metaclone.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.dto.SaveUserResponse;
import ru.metaclone.users.models.dto.UserResponse;
import ru.metaclone.users.service.UsersService;

import java.net.URI;

@RestController
@RequestMapping("/users")
@Validated
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<SaveUserResponse> update(@Validated @RequestBody SaveUserDetailsRequest saveUserDetailsRequest,
                                                 @PathVariable("id") Long userId) {
        var body = usersService.saveUserRequest(userId, saveUserDetailsRequest);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@Validated @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(usersService.getUserById(id));
    }
}
