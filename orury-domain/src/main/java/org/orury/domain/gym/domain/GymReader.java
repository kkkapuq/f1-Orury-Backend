package org.orury.domain.gym.domain;

import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GymReader {
    Optional<Gym> findGymById(Long id);

    boolean existsGymById(Long id);

    List<Gym> findGymsBySearchWord(String searchWord);

    List<Gym> findGymsInAreaGrid(Map<String, Double> gridMap);

    List<Gym> findGymsByUserLiked(Long userId, Long cursor, Pageable pageRequest);

    boolean existsGymLikeById(GymLikePK gymLikePK);

    boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId);
}
