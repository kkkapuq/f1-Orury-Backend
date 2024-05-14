package org.orury.client.user.interfaces.response;

import org.orury.client.global.IdIdentifiable;
import org.orury.domain.global.domain.Region;
import org.orury.domain.gym.domain.dto.GymDto;

public record MyGymResponse(
        Long id,
        String gymName,
        String gymType,
        String region
) implements IdIdentifiable {
    public static MyGymResponse of(GymDto gymDto) {
        return new MyGymResponse(
                gymDto.id(),
                gymDto.name(),
                gymDto.gymType().getDescription(),
                Region.getDescriptionFromAddress(gymDto.address())
        );
    }
}
