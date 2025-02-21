package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusDeleteResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusRegisterResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusSearchResponse;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceFacade {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusRegisterService registerService;
    private final ReadStatusUpdateService updateService;
    private final ReadStatusDeleteService deleteService;

    public ReadStatusRegisterResponse registerReadStatus() {

        ReadStatus ReadStatus = registerService.registerReadStatus();

        return ReadStatusRegisterResponse.from(ReadStatus);
    }

    public ReadStatusSearchResponse searchReadStatusById(UUID ReadStatusId) {

        ReadStatus ReadStatusById =
            readStatusRepository.findReadStatusById(ReadStatusId);

        return ReadStatusSearchResponse.from(ReadStatusById);
    }

    public List<ReadStatus> searchAllReadStatus() {

        return readStatusRepository.findAllReadStatus();
    }

    public ReadStatusUpdateResponse update(ReadStatusUpdateRequest request) {

        ReadStatus updatedReadStatus = updateService.update(request);

        return ReadStatusUpdateResponse.from(updatedReadStatus);
    }

    public ReadStatusDeleteResponse deleteById(UUID readStatusId) {

        ReadStatus deleteReadStatusById = deleteService.deleteById(readStatusId);

        return ReadStatusDeleteResponse.from(deleteReadStatusById);
    }
}
