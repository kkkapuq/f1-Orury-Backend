package org.orury.client.user.interfaces.message;

import lombok.Getter;

@Getter
public enum UserMessage {

    USER_DELETED("회원 탈퇴 되었습니다."),
    USER_UPDATED("유저 정보가 수정되었습니다."),
    USER_PROFILEIMAGE_UPDATED("프로필 사진이 수정되었습니다."),
    USER_READ("마이페이지를 조회했습니다."),
    USER_POSTS_READ("작성한 게시글을 조회했습니다."),
    USER_COMMENTS_READ("작성한 댓글을 조회했습니다."),
    USER_REVIEWS_READ("작성한 리뷰를 조회했습니다."),
    USER_GYMS_READ("저장한 암장을 조회했습니다."),
    USER_CREW_MEMBERS_READ("크루 일정 조회여부를 조회했습니다."),
    USER_MEETING_VIEWED_UPDATED("크루 일정 조회여부를 수정했습니다."),
    USER_MEETINGS_READ("다가오는 크루 일정을 조회했습니다."),
    USER_REPORTED("신고가 완료되었습니다.");

    private final String message;

    UserMessage(String message) {
        this.message = message;
    }


}
