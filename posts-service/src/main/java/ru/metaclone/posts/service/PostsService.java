package ru.metaclone.posts.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.metaclone.posts.data.entity.ImageEntity;
import ru.metaclone.posts.data.entity.PostEntity;
import ru.metaclone.posts.data.requests.PostRequest;
import ru.metaclone.posts.data.response.PostResponse;
import ru.metaclone.posts.exceptions.AccessDeniedException;
import ru.metaclone.posts.exceptions.PostNotFoundException;
import ru.metaclone.posts.mapper.PostMapper;
import ru.metaclone.posts.repository.PostsRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostMapper postMapper;

    public List<PostResponse> getUserPosts(Long userId, int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<PostEntity> posts = postsRepository.findAllByUserId(userId, pageRequest);
        return posts
                .stream()
                .map(postMapper::toResponse)
                .toList();
    }

    public PostResponse getPostById(long postId) {
        var postEntity = postsRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));
        return postMapper.toResponse(postEntity);
    }

    public PostResponse createPost(Long userId, PostRequest postRequest) {
        var postEntity = postMapper.toEntity(postRequest, userId);
        postsRepository.save(postEntity);
        return postMapper.toResponse(postEntity);
    }

    @Transactional
    public PostResponse updatePost(Long userId, Long postId, PostRequest postRequest) {
        PostEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new AccessDeniedException("User is not the author of the post");
        }

        post.setContent(postRequest.content());
        post.setUpdatedAt(OffsetDateTime.now());

        post.getImages().clear();

        if (postRequest.images() != null && !postRequest.images().isEmpty()) {
            List<ImageEntity> images = postRequest.images().stream()
                    .map(imgReq -> ImageEntity.builder()
                            .url(imgReq.imageUrl())
                            .post(post)
                            .build())
                    .toList();
            post.getImages().addAll(images);
        }

        PostEntity updatedPost = postsRepository.save(post);

        return postMapper.toResponse(updatedPost);
    }

    @Transactional
    public void removePost(Long userId, Long postId) {
        PostEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found"));

        if (!post.getUserId().equals(userId)) {
            throw new AccessDeniedException("User is not the author of the post");
        }

        postsRepository.delete(post);
    }
}
