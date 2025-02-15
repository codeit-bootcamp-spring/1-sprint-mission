package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
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
    public ReadStatus create(ReadStatusDto readStatusDto) {
        channelRepository.existsById(readStatusDto.channelId());
        userRepository.existsById(readStatusDto.userId());

        findAllByUserId(readStatusDto.userId())
                .forEach(readStatus -> {
                    if (readStatus.isSameChannelId(readStatusDto.channelId())) {
                        throw new IllegalArgumentException("[ERROR] 이미 존재하는 데이터입니다.");
                    }
                });

        return readStatusRepository.save(new ReadStatus(readStatusDto.channelId(), readStatusDto.userId()));
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return null;
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void delete(UUID readStatusId) {

    }

    @Override
    public void deleteByChannelId(UUID channelId) {

    }
}
