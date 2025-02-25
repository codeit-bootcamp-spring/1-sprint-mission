package discodeit.service;

import discodeit.dto.readstatus.CreateReadStatusRequest;
import discodeit.dto.readstatus.UpdateReadStatusRequest;
import discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusRequest createReadStatusRequest);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID userId, UpdateReadStatusRequest updateReadStatusRequest);
    void delete(UUID userId);
}
