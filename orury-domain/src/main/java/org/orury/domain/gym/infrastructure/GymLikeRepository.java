package org.orury.domain.gym.infrastructure;

import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GymLikeRepository extends JpaRepository<GymLike, GymLikePK> {
    boolean existsByGymLikePK_UserIdAndGymLikePK_GymId(Long userId, Long gymId);

    List<GymLike> findByGymLikePK_UserId(Long userId);

    List<GymLike> findByGymLikePK_UserIdOrderByGymLikePKDesc(Long userId, Pageable pageable);

    List<GymLike> findByGymLikePK_UserIdAndGymLikePK_GymIdLessThanOrderByGymLikePKDesc(Long userId, Long cursor, Pageable pageable);
}
