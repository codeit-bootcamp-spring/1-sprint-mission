package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(UUID userId, UUID channelId, UUID messageId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId);
        }

        Channel channel = channelRepository.findById(channelId);
        if (channel == null) {
            throw new IllegalArgumentException("채널을 찾을 수 없습니다: " + channelId);
        }

        List<ReadStatus> userReadStatuses = readStatusRepository.findAllByUserId(userId);
        for (int i = 0; i < userReadStatuses.size(); i++) {
            ReadStatus status = userReadStatuses.get(i);
            if (status.getChannelId().equals(channelId)) {
                throw new IllegalStateException(
                        "이미 존재하는 사용자: " + userId +
                                "이미 존재하는 채널: " + channelId
                );
            }
        }

        ReadStatus readStatus = new ReadStatus(userId, channelId);
        return readStatusRepository.save(readStatus);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return readStatusRepository.findById(id);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public ReadStatus update(UUID id, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(id);
        if (readStatus == null) {
            throw new IllegalArgumentException("업데이트 실패");
        }

        readStatus.updateLastReadAt(request.updateLastReadAt());
        return readStatusRepository.save(readStatus);
    }

    @Override
    public void deleteById(UUID id) {
        readStatusRepository.deleteById(id);
    }
}