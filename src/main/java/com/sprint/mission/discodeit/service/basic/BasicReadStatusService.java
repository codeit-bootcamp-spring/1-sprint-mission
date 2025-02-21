package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStautsfindAllByUserIdResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    public ReadStatus createReadStatus(ReadStatusCreateRequest readStatusRequest) {
        if (channelRepository.findChannelById(readStatusRequest.channelId()).isEmpty() &&
                userRepository.findUserById(readStatusRequest.userId()).isEmpty()
        ) {
            throw new NoSuchElementException("createReadStatus 실패 : 채널 ID " + readStatusRequest.channelId() +
                    "또는 사용자 ID" + readStatusRequest.userId() + "가 존재하지 않습니다." );
        }
        List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(readStatusRequest.userId());
        if (readStatuses.stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusRequest.channelId()))
        ) {
            throw new RuntimeException("이미 동일한 ReadStatus가 존재합니다.");
        }
        return readStatusRepository.save(new ReadStatus(readStatusRequest.userId(), readStatusRequest.channelId()));
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus 가 존재하지 않습니다."));
    }

    @Override
    public List<ReadStautsfindAllByUserIdResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatus ->
                        new ReadStautsfindAllByUserIdResponse(
                                readStatus.getId(),
                                readStatus.getUserId(),
                                readStatus.getChannelId(),
                                readStatus.getLastMessageReadAt()
                        )
                )
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public ReadStatusUpdateResponse updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest) {

        List<ReadStatus> readStatuses;
        readStatuses = readStatusRepository.findAllByUserId(readStatusUpdateRequest.userId());

        ReadStatus readStatus = readStatuses.stream()
                .filter(rs -> rs.getChannelId().equals(readStatusUpdateRequest.channelId()))
                .findFirst()
                .orElseGet(() -> new ReadStatus(readStatusUpdateRequest.userId(), readStatusUpdateRequest.channelId()));
        if (!readStatuses.contains(readStatus)) {
            readStatusRepository.save(readStatus);
        }

        readStatus.refreshLastMessageReadTime();
        readStatus.refreshUpdateAt();

        // ReadStatusUpdateRequest 에 없는 정보도 포함되어야 하는데,
        // 연산이나 리소스 아낀다고 깔끔하게 하지 않는 게 싫어서 그냥 맞춥니다!
        ReadStatusUpdateResponse readStatusUpdateResponse = new ReadStatusUpdateResponse(
                readStatus.getId(),
                readStatus.getUserId(),
                readStatus.getChannelId(),
                readStatus.getLastMessageReadAt());

        return readStatusUpdateResponse;
    }

    @Override
    public void deleteReadStatusById(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
