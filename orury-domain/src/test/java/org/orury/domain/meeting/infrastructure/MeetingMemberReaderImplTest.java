package org.orury.domain.meeting.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.domain.config.InfrastructureTest;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.meeting.domain.entity.Meeting;
import org.orury.domain.meeting.domain.entity.MeetingMember;
import org.orury.domain.user.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.orury.domain.MeetingDomainFixture.TestMeetingMember.createMeetingMember;
import static org.orury.domain.UserDomainFixture.TestUser.createUser;

@ExtendWith(MockitoExtension.class)
@DisplayName("[Reader] 일정멤버 ReaderImpl 테스트")
@ActiveProfiles("test")
class MeetingMemberReaderImplTest extends InfrastructureTest {

    @DisplayName("일정id와 유저id를 받아, 일정멤버 존재 여부를 확인한다.")
    @Test
    void existsByMeetingIdAndUserId() {
        // given & when
        meetingMemberReader.existsByMeetingIdAndUserId(
                mock(Meeting.class).getId(),
                mock(User.class).getId()
        );

        // then
        then(meetingMemberRepository).should(only())
                .existsByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserId(anyLong(), anyLong());
    }

    @DisplayName("일정id, 일정생성자id, 유저id를 받아, 일정생성자를 제외한 다른 일정멤버목록을 조회한다.")
    @Test
    void getOtherMeetingMembersByMeetingIdMaximum() {
        // given & when
        meetingMemberReader.getOtherMeetingMembersByMeetingIdMaximum(
                mock(Meeting.class).getId(),
                mock(User.class).getId(),
                NumberConstants.MAXIMUM_OF_MEETING_THUMBNAILS
        );

        // then
        then(meetingMemberRepository).should(only())
                .findByMeetingMemberPK_MeetingIdAndMeetingMemberPK_UserIdNot(anyLong(), anyLong(), any(PageRequest.class));
    }

    @DisplayName("일정id를 받아, 일정에 참여한 멤버목록을 조회한다.")
    @Test
    void getMeetingMembersByMeetingId() {
        // given
        Long meetingId = 74578L;
        List<MeetingMember> meetingMembers = List.of(
                createMeetingMember(meetingId, 1L).build().get(),
                createMeetingMember(meetingId, 2L).build().get(),
                createMeetingMember(meetingId, 3L).build().get()
        );
        given(meetingMemberRepository.findByMeetingMemberPK_MeetingId(meetingId))
                .willReturn(meetingMembers);
        given(userRepository.findUserById(anyLong()))
                .willReturn(
                        createUser(1L).build().get(),
                        createUser(2L).build().get(),
                        createUser(3L).build().get()
                );

        // when
        meetingMemberReader.getMeetingMembersByMeetingId(meetingId);

        // then
        then(meetingMemberRepository).should(only())
                .findByMeetingMemberPK_MeetingId(anyLong());
        then(userRepository).should(times(meetingMembers.size()))
                .findUserById(anyLong());
    }
}