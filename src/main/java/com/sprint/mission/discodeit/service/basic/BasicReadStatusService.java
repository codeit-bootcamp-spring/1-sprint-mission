package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public ReadStatus create(CreateReadStatusRequest request) {
        // User와 Channel 검증하기
        validUser(request.userId());
        validChannel(request.channelId());

        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(request.userId());
        boolean exists = readStatuses.stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(request.channelId()));
        if (exists) {
            throw new ServiceException(ErrorCode.ALREADY_EXIST_READSTATUS);
        }

        Instant lastReadAt = Instant.now();
        ReadStatus readStatus = new ReadStatus(request.userId(), request.channelId(), lastReadAt);
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_READSTATUS));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        validUser(userID);
        return readStatusRepository.findAllByUserId(userID);
    }

    /*
    User가 채널에서 메시지를 읽은 시간을 업데이트
     */
    @Override
    public List<ReadStatus> updateByChannelId(UUID channelId) {
        validChannel(channelId);

        // 채널에 해당하는 ReadStatus 목록을 가져옴
        List<ReadStatus> readStatuses = readStatusRepository.findAllByChannelId(channelId);

        // 모든 ReadStatus의 'lastReadTime'을 업데이트
        readStatuses.forEach(readStatus -> readStatus.updateLastReadTime(Instant.now()));

        // 모든 수정된 ReadStatus 저장
        readStatusRepository.saveAll(readStatuses);

        return readStatuses;
    }


    @Override
    public void delete(UUID id) {
        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_READSTATUS));
        readStatusRepository.delete(readStatus);
    }

    private void validUser(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USER));
    }

    private void validChannel(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL));
    }
}
