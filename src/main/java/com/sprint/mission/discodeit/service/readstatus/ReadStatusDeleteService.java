package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusDeleteResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusDeleteService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatus deleteById(UUID readStatusId) {

        return readStatusRepository.deleteReadStatusById(readStatusId);
    }
}
