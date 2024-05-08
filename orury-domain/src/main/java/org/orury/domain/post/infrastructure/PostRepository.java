package org.orury.domain.post.infrastructure;

import org.orury.domain.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByCategoryOrderByIdDesc(int category, Pageable pageable);

    List<Post> findByCategoryAndIdLessThanOrderByIdDesc(int category, Long cursor, Pageable pageable);

    List<Post> findByIdLessThanAndTitleContainingOrIdLessThanAndContentContainingOrderByIdDesc(Long cursor1, String titleSearchWord, Long cursor2, String contentSearchWord, Pageable pageable);

    List<Post> findByLikeCountLessThanAndTitleContainingOrLikeCountLessThanAndContentContainingOrderByLikeCountDesc(int likeCount1, String titleSearchWord, int likeCount2, String contentSearchWord, Pageable pageable);

    Page<Post> findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc(int likeCount, LocalDateTime localDateTime, Pageable pageable);

    List<Post> findByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Post> findByUserIdAndIdLessThanOrderByIdDesc(Long userId, Long cursor, Pageable pageable);

    List<Post> findByUserId(Long userId);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(:keyword),'%') OR LOWER(p.content) LIKE CONCAT('%',LOWER(:keyword),'%') " +
            "ORDER BY p.id DESC")
    List<Post> findBySearchWordOrderByIdDesc(String keyword, Pageable pageable);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE (LOWER(p.title) LIKE CONCAT('%',LOWER(:keyword),'%') OR LOWER(p.content) LIKE CONCAT('%',LOWER(:keyword),'%')) " +
            "AND (p.id < :cursor) " +
            "ORDER BY p.id DESC")
    List<Post> findBySearchWordOrderByIdDescWithCursor(String keyword, Long cursor, Pageable pageable);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE LOWER(p.title) LIKE CONCAT('%',LOWER(:keyword),'%') OR LOWER(p.content) LIKE CONCAT('%',LOWER(:keyword),'%') " +
            "ORDER BY p.likeCount DESC, p.id DESC")
    List<Post> findBySearchWordOrderByLikeCountDesc(String keyword, Pageable pageable);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE (LOWER(p.title) LIKE CONCAT('%',LOWER(:keyword),'%') OR LOWER(p.content) LIKE CONCAT('%',LOWER(:keyword),'%')) " +
            "AND (p.likeCount < :likeCount OR (p.likeCount = :likeCount AND p.id < :cursor)) " +
            "ORDER BY p.likeCount DESC, p.id DESC")
    List<Post> findBySearchWordOrderByLikeCountDescWithCursor(String keyword, Long cursor, int likeCount, Pageable pageable);

    @Modifying
    @Query("UPDATE post SET viewCount = viewCount + 1 WHERE id = :id")
    void updateViewCount(Long id);

    @Modifying
    @Query("UPDATE post SET commentCount = commentCount + 1 WHERE id = :id")
    void increaseCommentCount(Long id);

    @Modifying
    @Query("UPDATE post SET commentCount = commentCount - 1 WHERE id = :id")
    void decreaseCommentCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount + 1 WHERE id = :id")
    void increaseLikeCount(Long id);

    @Modifying
    @Query("UPDATE post SET likeCount = likeCount - 1 WHERE id = :id")
    void decreaseLikeCount(Long id);
}