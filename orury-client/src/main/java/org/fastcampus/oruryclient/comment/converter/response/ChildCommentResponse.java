package org.fastcampus.oruryclient.comment.converter.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.fastcampus.orurydomain.comment.dto.CommentDto;
import org.fastcampus.oruryclient.global.constants.Constants;
import org.fastcampus.oruryclient.global.constants.NumberConstants;

import java.time.LocalDateTime;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChildCommentResponse(
        Long id,
        String content,
        Long parentId,
        Long userId,
        String userNickname,
        String userProfileImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        int likeCount,
        boolean isLike,
        int deleted
) {
    public static ChildCommentResponse of(CommentDto commentDto, int likeCount, boolean isLike) {

        return new ChildCommentResponse(
                commentDto.id(),
                commentDto.content(),
                commentDto.parentId(),
                (commentDto.deleted() == 1) ? NumberConstants.DELETED_USER_ID : commentDto.userDto().id(),
                (commentDto.deleted() == 1) ? Constants.DEFAULT_NICKNAME.getMessage() : commentDto.userDto().nickname(),
                (commentDto.deleted() == 1) ? Constants.DEFAULT_IMAGE.getMessage() : commentDto.userDto().profileImage(),
                commentDto.createdAt(),
                commentDto.updatedAt(),
                (commentDto.deleted() == 1) ? 0 : likeCount,
                (commentDto.deleted() == 1) ? false : isLike,
                commentDto.deleted()
        );
    }
}