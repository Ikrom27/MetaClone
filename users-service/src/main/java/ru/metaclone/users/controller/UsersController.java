package ru.metaclone.users.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.users.models.dto.UpdateUserRequest;
import ru.metaclone.users.models.dto.UserResponse;
import ru.metaclone.users.secury.UserContext;
import ru.metaclone.users.service.UsersService;

@RestController
@RequestMapping("users/v1")
@Validated
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(@Validated @RequestBody UpdateUserRequest updateUserRequest,
                                               @PathVariable("id") Long userId) {
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserContext userContext = (UserContext) auth.getPrincipal();
        boolean isOwner = userId.equals(userContext.id());
        boolean isAdmin = userContext.authorities().contains("ROLE_ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("Forbidden");
        }

        var body = usersService.updateUser(userId, updateUserRequest);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@Validated @PathVariable("id") Long id) {
        return ResponseEntity.ok().body(usersService.getUserById(id));
    }
}
