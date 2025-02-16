package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusRegisterService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatus registerReadStatus(UUID userId) {

        ReadStatus ReadStatusToCreate = ReadStatus.createReadStatus(userId);

        return readStatusRepository.createReadStatus(ReadStatusToCreate);
    }
}
