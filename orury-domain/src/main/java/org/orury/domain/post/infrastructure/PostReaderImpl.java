package org.orury.domain.post.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.PostErrorCode;
import org.orury.common.error.exception.InfraImplException;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.post.domain.PostReader;
import org.orury.domain.post.domain.entity.Post;
import org.orury.domain.post.domain.entity.PostLikePK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
@RequiredArgsConstructor
public class PostReaderImpl implements PostReader {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public List<Post> findByCategoryOrderByIdDesc(int category, Long cursor, Pageable pageable) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByCategoryOrderByIdDesc(category, pageable)
                : postRepository.findByCategoryAndIdLessThanOrderByIdDesc(category, cursor, pageable);
    }

    @Override
    public List<Post> findBySearchWordOrderByIdDesc(String searchWord, Long cursor) {
        var pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findBySearchWordOrderByIdDesc(searchWord, pageable)
                : postRepository.findBySearchWordOrderByIdDescWithCursor(searchWord, cursor, pageable);
    }

    @Override
    public List<Post> findBySearchWordOrderByLikeCountDesc(String searchWord, Long cursor, int lastLikeCount) {
        var pageable = PageRequest.of(0, NumberConstants.POST_PAGINATION_SIZE);
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findBySearchWordOrderByLikeCountDesc(searchWord, pageable)
                : postRepository.findBySearchWordOrderByLikeCountDescWithCursor(searchWord, cursor, lastLikeCount, pageable);
    }

    @Override
    public List<Post> findByUserIdOrderByIdDesc(Long userId, Long cursor, Pageable pageable) {
        return (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? postRepository.findByUserIdOrderByIdDesc(userId, pageable)
                : postRepository.findByUserIdAndIdLessThanOrderByIdDesc(userId, cursor, pageable);
    }

    @Override
    public Page<Post> findByLikeCountGreaterDescAndCreatedAtDesc(Pageable pageable) {
        return postRepository.findByLikeCountGreaterThanEqualAndCreatedAtGreaterThanEqualOrderByLikeCountDescCreatedAtDesc
                (NumberConstants.HOT_POSTS_BOUNDARY,
                        LocalDateTime.now().minusMonths(1L),
                        pageable);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public boolean isPostLiked(Long userId, Long postId) {
        return postLikeRepository.existsPostLikeByPostLikePK_UserIdAndPostLikePK_PostId(userId, postId);
    }

    @Override
    public boolean existsByPostLikePK(PostLikePK postLikePK) {
        if (!postRepository.existsById(postLikePK.getPostId()))
            throw new InfraImplException(PostErrorCode.NOT_FOUND);
        return postLikeRepository.existsByPostLikePK(postLikePK);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
