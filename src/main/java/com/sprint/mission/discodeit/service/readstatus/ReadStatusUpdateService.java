package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusUpdateService {

    private final ReadStatusRepository readStatusRepository;

    public ReadStatus update(ReadStatusUpdateRequest request) {

        ReadStatus readStatusToUpdate = ReadStatus.createReadStatus(
            request.userId(),
            request.createAt(),
            request.updateAt()
        );

        return readStatusRepository.updateReadStatus(request.userId(), readStatusToUpdate);
    }
}
