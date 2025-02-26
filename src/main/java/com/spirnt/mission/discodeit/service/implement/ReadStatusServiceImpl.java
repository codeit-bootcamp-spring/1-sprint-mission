package com.spirnt.mission.discodeit.service.implement;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadStatusServiceImpl implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatus create(ReadStatusCreate readStatusCreate) {
        // User와 Channel 존재하지 않으면 예외 발생
        if (!userRepository.existsById(readStatusCreate.userId())) {
            throw new NoSuchElementException("User ID Not Found");
        }
        if (!channelRepository.existsById(readStatusCreate.channelId())) {
            throw new NoSuchElementException("Channel ID Not Found");
        }
        //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
        if (existsByUserIdChannelId(readStatusCreate.userId(), readStatusCreate.channelId())) {
            throw new IllegalStateException("The ReadStatus with UserId and ChannelId Already Exists");
        }
        ReadStatus readStatus = new ReadStatus(readStatusCreate.userId(),
                readStatusCreate.channelId(),
                Instant.now());
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("Read Status ID Not Found"));
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new NoSuchElementException("Invalid User ID And Channel ID."));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Map<UUID, ReadStatus> map = readStatusRepository.findAll();
        List<ReadStatus> list = map.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> map = readStatusRepository.findAll();
        List<ReadStatus> list = map.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdate readStatusUpdate) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("Read Status Not Found"));
        readStatus.update(readStatusUpdate.lastReadAt());
        readStatusRepository.save(readStatus);
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.delete(id);
    }

    // 채널 서비스에서 호출
    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, ReadStatus> map = readStatusRepository.findAll();
        List<UUID> list = map.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(channelId))
                .map(readStatus -> readStatus.getId())
                .collect(Collectors.toList());
        for (UUID id : list) {
            delete(id);
        }
    }

    @Override
    public boolean existsByUserIdChannelId(UUID userId, UUID channelId) {
        //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
        if (readStatusRepository.findAll().values().stream()
                .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId)
                        && readStatus.getUserId().equals(userId))) {
            return true;
        }
        return false;
    }


}


