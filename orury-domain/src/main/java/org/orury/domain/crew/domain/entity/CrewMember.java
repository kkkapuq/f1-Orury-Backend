package org.orury.domain.crew.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.NumericBooleanConverter;
import org.orury.domain.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"crewMemberPK"}, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "crew_member")
public class CrewMember extends AuditingField {
    @EmbeddedId
    private CrewMemberPK crewMemberPK;

    @Convert(converter = NumericBooleanConverter.class)
    @Column(name = "meeting_viewed", nullable = false)
    private Boolean meetingViewed;

    private CrewMember(
            CrewMemberPK crewMemberPK,
            Boolean meetingViewed,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.crewMemberPK = crewMemberPK;
        this.meetingViewed = meetingViewed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CrewMember of(
            CrewMemberPK crewMemberPK,
            Boolean meetingViewed,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new CrewMember(
                crewMemberPK,
                meetingViewed,
                createdAt,
                updatedAt
        );
    }
}
