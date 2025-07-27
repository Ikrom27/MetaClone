package ru.metaclone.users.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.users.data.requests.UpdateUserRequest;
import ru.metaclone.users.data.response.UserFollowersResponse;
import ru.metaclone.users.data.response.UserResponse;
import ru.metaclone.users.security.model.UserContext;
import ru.metaclone.users.service.UsersService;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponse> update(
            @Validated @RequestBody UpdateUserRequest updateUserRequest,
            @PathVariable("id") Long userId,
            @AuthenticationPrincipal UserContext userContext
    ) {
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

    @GetMapping("/{id}/followers")
    public ResponseEntity<UserFollowersResponse> getUserFollowers(
            @PathVariable("id") long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        var response = new UserFollowersResponse(userId,  usersService.getFollowers(userId, page, size));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/following")
    public ResponseEntity<UserFollowersResponse> getUserFollowing(
            @PathVariable("id") long userId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        var response = new UserFollowersResponse(userId,  usersService.getFollowing(userId, page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{followeeId}/follow")
    public ResponseEntity<Void> followUser(
            @AuthenticationPrincipal UserContext userContext,
            @PathVariable("followeeId") Long followeeId
    ) {
        usersService.follow(userContext.id(), followeeId);
        return ResponseEntity.ok().build();
    }
}
