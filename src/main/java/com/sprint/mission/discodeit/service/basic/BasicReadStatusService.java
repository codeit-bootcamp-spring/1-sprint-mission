package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserService userService;
    private final ChannelService channelService;

    @Override
    public ReadStatusDto create(CreateReadStatusRequestDto createReadStatusRequestDto) {

        UUID userId = createReadStatusRequestDto.userId();
        UUID channelId = createReadStatusRequestDto.channelId();

        User user = userService.find(userId);
        Channel channel = channelService.find(channelId);

        ReadStatus readStatus = new ReadStatus(user, channel);

        readStatusRepository.save(readStatus);

        return ReadStatusMapper.INSTANCE.toDto(readStatus);
    }

    @Override
    public ReadStatusDto find(UUID id) {

        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 read status입니다."));

        return ReadStatusMapper.INSTANCE.toDto(readStatus);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {

        userService.find(userId);

        return readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getUser().getId().equals(userId))
                .map(ReadStatusMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public ReadStatusDto update(UUID id) {

        ReadStatus readStatus = readStatusRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 read status입니다."));
        readStatus.updateLastReadTime();
        readStatusRepository.save(readStatus);

        return ReadStatusMapper.INSTANCE.toDto(readStatus);
    }

    @Override
    public void delete(UUID id) {

        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {

        readStatusRepository.findAll().stream()
                .filter(readStatus -> readStatus.getChannel().getId().equals(channelId))
                .forEach(readStatusRepository::delete);
    }
}
