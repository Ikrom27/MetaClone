package ru.metaclone.posts.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.posts.data.requests.PostRequest;
import ru.metaclone.posts.data.response.PostResponse;
import ru.metaclone.posts.data.response.UserPostsResponse;
import ru.metaclone.posts.exceptions.AccessDeniedException;
import ru.metaclone.posts.security.model.Authorities;
import ru.metaclone.posts.security.model.UserContext;
import ru.metaclone.posts.service.PostsService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostsController {
    private final PostsService postsService;

    @GetMapping
    public ResponseEntity<UserPostsResponse> getUserPosts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var posts = postsService.getUserPosts(userId, page, size);
        return ResponseEntity.ok(new UserPostsResponse(userId, posts));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postsService.getPostById(postId));
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal UserContext userContext,
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PostRequest postRequest
    ) {
        validateOwnerOrAdmin(userContext, userId);
        var post = postsService.createPost(userId, postRequest);
        var uri = URI.create("/api/v1/posts/" + post.postId());
        return ResponseEntity.created(uri).body(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal UserContext userContext,
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long postId,
            @RequestBody PostRequest postRequest
    ) {
        validateOwnerOrAdmin(userContext, userId);
        var post = postsService.updatePost(userId, postId, postRequest);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> removePost(
            @AuthenticationPrincipal UserContext userContext,
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long postId
    ) {
        validateOwnerOrAdmin(userContext, userId);
        postsService.removePost(userId, postId);
        return ResponseEntity.ok("removed");
    }

    public void validateOwnerOrAdmin(UserContext userContext, Long userId) {
        boolean isOwner = userId.equals(userContext.id());
        boolean isAdmin = userContext.authorities().contains(Authorities.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("no owner or admin");
        }
    }
}
