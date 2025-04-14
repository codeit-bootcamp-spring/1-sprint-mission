package com.sprint.mission.service;
import com.sprint.mission.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.entity.addOn.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request) ;
    ReadStatus findById(UUID readStatusId) ;
    ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) ;
    void delete(UUID readStatusId) ;
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
}
