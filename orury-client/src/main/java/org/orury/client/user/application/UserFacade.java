package org.orury.client.user.application;


import lombok.RequiredArgsConstructor;
import org.orury.client.comment.application.CommentService;
import org.orury.client.crew.application.CrewService;
import org.orury.client.global.IdIdentifiable;
import org.orury.client.global.WithCursorResponse;
import org.orury.client.gym.application.GymService;
import org.orury.client.meeting.application.MeetingService;
import org.orury.client.post.application.PostService;
import org.orury.client.review.application.ReviewService;
import org.orury.client.user.interfaces.request.MeetingViewedRequest;
import org.orury.client.user.interfaces.request.UserInfoRequest;
import org.orury.client.user.interfaces.request.UserReportRequest;
import org.orury.client.user.interfaces.response.*;
import org.orury.domain.comment.domain.dto.CommentDto;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.entity.CrewMemberPK;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.meeting.domain.dto.MeetingDto;
import org.orury.domain.post.domain.dto.PostDto;
import org.orury.domain.review.domain.dto.ReviewDto;
import org.orury.domain.user.domain.dto.ReportDto;
import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final PostService postService;
    private final ReviewService reviewService;
    private final CommentService commentService;
    private final MeetingService meetingService;
    private final CrewService crewService;
    private final GymService gymService;

    public UserDto readMypage(Long id) {
        return userService.getUserDtoById(id);
    }

    public void updateProfileImage(Long id, MultipartFile image) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.updateProfileImage(userDto, image);
    }

    public void updateUserInfo(Long id, UserInfoRequest userInfoRequest) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.updateUserInfo(userInfoRequest.toDto(userDto));
    }

    public WithCursorResponse<MyPostResponse> getPostsByUserId(Long id, Long cursor) {
        List<PostDto> postDtos = postService.getPostDtosByUserId(id, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));

        return convertDtosToWithCursorResponse(postDtos, MyPostResponse::of, cursor);
    }

    public WithCursorResponse<MyReviewResponse> getReviewsByUserId(Long userId, Long cursor) {
        List<ReviewDto> reviewDtos = reviewService.getReviewDtosByUserId(userId, cursor, PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE));
        Function<ReviewDto, MyReviewResponse> function = reviewDto -> {
            int myReaction = reviewService.getReactionType(userId, reviewDto.id());
            return MyReviewResponse.of(reviewDto, myReaction);
        };
        return convertDtosToWithCursorResponse(reviewDtos, function, cursor);
    }

    public WithCursorResponse<MyCommentResponse> getCommentsByUserId(Long id, Long cursor) {
        List<CommentDto> commmentDtos = commentService.getCommentDtosByUserId(id, cursor);

        return convertDtosToWithCursorResponse(commmentDtos, MyCommentResponse::of, cursor);
    }

    public WithCursorResponse<MyGymResponse> getGymsByUserLiked(Long userId, Long cursor) {
        List<GymDto> gymDtos = gymService.getGymDtosByUserLiked(userId, cursor);

        return convertDtosToWithCursorResponse(gymDtos, MyGymResponse::of, cursor);
    }

    public List<MyMeetingResponse> getMeetingsByUserId(Long userId) {
        List<MeetingDto> meetingDtos = meetingService.getUpcomingMeetingDtosByUserId(userId);

        return meetingDtos.stream()
                .map(meetingDto -> MyMeetingResponse.of(meetingDto, userId))
                .toList();
    }

    public List<MyCrewMemberResponse> getCrewMembersByUserId(Long userId) {
        List<CrewDto> crewDtos = crewService.getJoinedCrewDtos(userId);
        return crewDtos.stream()
                .map(crewDto -> {
                    Boolean meetingViewed = crewService.getCrewMemberByCrewIdAndUserId(crewDto.id(), userId)
                            .getMeetingViewed();
                    return MyCrewMemberResponse.of(crewDto, meetingViewed);
                }).toList();
    }

    public void updateMeetingViewed(Long userId, List<MeetingViewedRequest> requests) {
        requests.forEach(request -> {
            CrewMemberPK crewMemberPK = CrewMemberPK.of(userId, request.crewId());
            crewService.updateMeetingViewed(request.toDto(crewMemberPK));
        });
    }

    public void deleteUser(Long id) {
        UserDto userDto = userService.getUserDtoById(id);
        userService.deleteUser(userDto);
    }

    public void createReport(UserReportRequest request, Long reporterId) {
        UserDto reporterDto = userService.getUserDtoById(reporterId);
        UserDto reporteeDto = userService.getUserDtoById(request.userId());
        ReportDto reportDto = request.toDto(reporterDto, reporteeDto);
        userService.reportUser(reportDto);
    }

    private <T, R extends IdIdentifiable> WithCursorResponse<R> convertDtosToWithCursorResponse(List<T> dtos, Function<T, R> toResponseFunction, Long cursor) {
        List<R> responses = dtos.stream()
                .map(toResponseFunction)
                .toList();

        return WithCursorResponse.of(responses, cursor);
    }

}
