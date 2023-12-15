package org.fastcampus.oruryadmin.domain.notice.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;
import org.fastcampus.oruryadmin.domain.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j
@ToString
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notice extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    //추후에 추가 예정
    @ManyToOne(optional = false)
    private Admin admin;

    private Notice(String title, String content, Admin admin) {
        this.title = title;
        this.content = content;
        this.admin = admin;
    }

    public static Notice of(String title, String content, Admin admin) {
        return new Notice(title, content, admin);
    }
}