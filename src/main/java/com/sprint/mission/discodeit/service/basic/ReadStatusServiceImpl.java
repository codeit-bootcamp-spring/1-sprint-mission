package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.ReadStatusUpdateRequest;
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
public class ReadStatusServiceImpl implements ReadStatusService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    public ReadStatus createReadStatus(ReadStatusCreateRequest readStatusRequest){
        if ( channelRepository.findChannelById(readStatusRequest.channelId()).isEmpty() &&
            userRepository.findUserById(readStatusRequest.userId()).isEmpty()
        ){
            throw new NoSuchElementException("createReadStatus, 채널 또는 사용자가 존재하지 않는다");
        }
        List<ReadStatus> readStatuses;
        readStatuses = readStatusRepository.findAllByUserId(readStatusRequest.userId());
        if(readStatuses.stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(readStatusRequest.channelId()))
        ){
            throw new RuntimeException("이미 동일한 ReadStatus가 존재합니다.");
        }
        return readStatusRepository.save(new ReadStatus(readStatusRequest.userId(), readStatusRequest.channelId()));
    }

    @Override
    public ReadStatus findReadStatusById(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId).orElseGet( () -> {
                    System.out.println(" No  + " + readStatusId.toString() + " readStatusId exists.\n");
                    return null;
        });
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    public ReadStatus updateReadStatus(ReadStatusUpdateRequest readStatusUpdateRequest) {

        List<ReadStatus> readStatuses;
        readStatuses = readStatusRepository.findAllByUserId(readStatusUpdateRequest.userId());

        ReadStatus readStatus = readStatuses.stream()
                .filter( rs -> rs.getChannelId().equals(readStatusUpdateRequest.channelId()))
                .findFirst()
                .orElseGet(() -> new ReadStatus(readStatusUpdateRequest.userId(), readStatusUpdateRequest.channelId()) );
        if (!readStatuses.contains(readStatus)) {
            readStatusRepository.save(readStatus);
        }

        readStatus.refrashLastMessageReadTime();
        readStatus.refreshUpdateAt();

        return readStatus;
    }

    @Override
    public void deleteReadStatusById(UUID readStatusId) {
        readStatusRepository.deleteById(readStatusId);
    }
}
