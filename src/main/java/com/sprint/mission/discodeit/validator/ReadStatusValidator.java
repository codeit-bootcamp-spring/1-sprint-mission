package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.error.execption.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadStatusValidator {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatus validateReadStatusExistsByUserId(UUID userId) {
        return readStatusRepository.findReadStatusByUserId(userId)
                .orElseThrow(() -> new ReadStatusNotFoundException("userId: " + userId));
    }

    public ReadStatus validateReadStatusExistsById(UUID readStatusId) {
        return Optional.ofNullable(readStatusRepository.findReadStatusById(readStatusId))
                .orElseThrow(() -> new ReadStatusNotFoundException("id: " + readStatusId));
    }
}
