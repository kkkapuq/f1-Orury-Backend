package org.fastcampus.oruryclient.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryclient.global.constants.NumberConstants;
import org.fastcampus.oruryclient.post.converter.message.PostMessage;
import org.fastcampus.oruryclient.post.service.PostLikeService;
import org.fastcampus.orurydomain.base.converter.ApiResponse;
import org.fastcampus.orurydomain.post.db.model.PostLike;
import org.fastcampus.orurydomain.post.db.model.PostLikePK;
import org.fastcampus.orurydomain.post.dto.PostLikeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@RestController
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시글 좋아요 생성", description = "게시글 id를 받아, 게시글 좋아요를 생성한다.")
    @PostMapping("/post/like/{postId}")
    public ApiResponse<Object> createPostLike(@PathVariable Long postId) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(NumberConstants.USER_ID, postId)));

        postLikeService.createPostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_CREATED.getMessage())
                .build();
    }

    @Operation(summary = "게시글 좋아요 삭제", description = "게시글 id를 받아, 게시글 좋아요를 삭제한다.")
    @DeleteMapping("/post/like/{postId}")
    public ApiResponse<Object> deletePostLike(@PathVariable Long postId) {
        PostLikeDto postLikeDto = PostLikeDto.from(PostLike.of(PostLikePK.of(NumberConstants.USER_ID, postId)));

        postLikeService.deletePostLike(postLikeDto);

        return ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message(PostMessage.POST_LIKE_DELETED.getMessage())
                .build();
    }
}
