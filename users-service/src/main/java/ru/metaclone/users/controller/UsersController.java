package ru.metaclone.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.dto.SaveUserResponse;
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

    @PostMapping("/save")
    public ResponseEntity<SaveUserResponse> save(@Validated @RequestBody SaveUserDetailsRequest saveUserDetailsRequest,
                                                 UriComponentsBuilder uriBuilder) {
        var body = usersService.saveUser(saveUserDetailsRequest);
        URI location = uriBuilder
                .path("/users/{id}")
                .buildAndExpand(body.userId())
                .toUri();
        return ResponseEntity.created(location).body(body);
    }
}
