package org.orury.domain.gym.infrastructure;

import lombok.RequiredArgsConstructor;
import org.orury.domain.global.constants.NumberConstants;
import org.orury.domain.gym.domain.GymReader;
import org.orury.domain.gym.domain.entity.Gym;
import org.orury.domain.gym.domain.entity.GymLike;
import org.orury.domain.gym.domain.entity.GymLikePK;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GymReaderImpl implements GymReader {
    private final GymRepository gymRepository;
    private final GymLikeRepository gymLikeRepository;

    @Override
    public Optional<Gym> findGymById(Long id) {
        return gymRepository.findById(id);
    }

    @Override
    public boolean existsGymById(Long id) {
        return gymRepository.existsById(id);
    }

    @Override
    public List<Gym> findGymsBySearchWord(String searchWord) {
        return gymRepository.findByNameContainingOrAddressContainingOrRoadAddressContaining(searchWord, searchWord, searchWord);
    }

    public List<Gym> findGymsInAreaGrid(Map<String, Double> gridMap) {
        return gymRepository.findByLatitudeBetweenAndLongitudeBetweenOrderByLikeCount(
                gridMap.get("bottom"),
                gridMap.get("top"),
                gridMap.get("left"),
                gridMap.get("right")
        );
    }

    @Override
    public List<Gym> findGymsByUserLiked(Long userId, Long cursor, Pageable pageRequest) {
        List<GymLike> gymLikes = (cursor.equals(NumberConstants.FIRST_CURSOR))
                ? gymLikeRepository.findByGymLikePK_UserIdOrderByGymLikePKDesc(userId, pageRequest)
                : gymLikeRepository.findByGymLikePK_UserIdAndGymLikePK_GymIdLessThanOrderByGymLikePKDesc(userId, cursor, pageRequest);
        return gymLikes.stream()
                .map(gymLike -> gymRepository.findById(gymLike.getGymLikePK().getGymId()).orElse(null))
                .toList();
    }

    @Override
    public boolean existsGymLikeById(GymLikePK gymLikePK) {
        return gymLikeRepository.existsById(gymLikePK);
    }

    @Override
    public boolean existsGymLikeByUserIdAndGymId(Long userId, Long gymId) {
        return gymLikeRepository.existsByGymLikePK_UserIdAndGymLikePK_GymId(userId, gymId);
    }
}
