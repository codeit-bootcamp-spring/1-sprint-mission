package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.security.SecurityUtil;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.UserAlreadyMemberException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusMapper readStatusMapper;

    @Override
    @Transactional
    public ReadStatusDto create(CreateReadStatusRequestDto request) {
        Channel channel = channelRepository.findById(request.getChannelId())
                .orElseThrow(ChannelNotFoundException::new);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if (readStatusRepository.existsByUserIdAndChannelId(request.getChannelId(),
                request.getUserId())) {
            throw new UserAlreadyMemberException();
        }

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        if (!user.getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인만 수정 가능");
        }

        Instant now = request.getLastReadAt();
        ReadStatus readStatus = new ReadStatus(user, channel, now);

        return readStatusMapper.toDto(readStatusRepository.save(readStatus));
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusRepository.findById(id)
                .orElseThrow(ReadStatusNotFoundException::new);
    }

    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId)
                .stream().map(readStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequestDto request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(ReadStatusNotFoundException::new);

        UUID currentUserId = SecurityUtil.getCurrentUserId();

        if (!readStatus.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인만 수정 가능");
        }
        readStatus.update(request.getNewLastReadAt());
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new ReadStatusNotFoundException();
        }
        readStatusRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByChannelId(UUID id) {
        readStatusRepository.deleteByChannelId(id);
    }
}
