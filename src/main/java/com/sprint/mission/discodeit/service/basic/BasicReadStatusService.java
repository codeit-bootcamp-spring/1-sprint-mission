package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    @Override
    public ReadStatus create(UUID channelId, UUID userId) {
        return new ReadStatus(channelId, userId);
    }
}
