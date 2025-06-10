package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {


    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;


    @Override
    @Transactional
    public ChannelDto createPublicChannel(PublicChannelCreateRequestDto request) {
        log.info("Create public channel: name={},Description={}", request.getName(),
                request.getDescription());
        Channel channel = new Channel(PUBLIC, request.getName(), request.getDescription());
        Channel save = channelRepository.save(channel);
        log.info("Save channel: id={}, name={}", save.getId(), channel.getName());
        return channelMapper.toDto(save);
    }

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequestDto request) {
        log.info("Create private channel: 참여자 수 ={}, ids={}", request.getParticipantIds().size(),
                request.getParticipantIds());
        List<User> participants = userRepository.findAllById(request.getParticipantIds());

        if (participants.size() != request.getParticipantIds().size()) {
            log.warn("creat private failed: 사용자 수={}", request.getParticipantIds().size());
            throw new UserNotFoundException();
        }

        Channel channel = new Channel(PRIVATE, null, null);
        Channel savedChannel = channelRepository.save(channel);

        participants.forEach(user -> {
            ReadStatus readStatus = ReadStatus.createWithDefaultNotification(user, savedChannel,
                    Instant.now());
            readStatusRepository.save(readStatus);
        });

        log.info("Save channel: id={}, 참여자 수={}", savedChannel.getId(), participants.size());
        return channelMapper.toDto(savedChannel);
    }

    @Override
    public ChannelDto getChannelById(UUID id) {
        return channelRepository.findById(id)
                .map(channelMapper::toDto)
                .orElseThrow(ChannelNotFoundException::new);
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        log.debug("Find all channels by userId: {}", userId);
        List<ChannelDto> channels = channelRepository.findAllByUserId(userId)
                .stream()
                .map(channelMapper::toDto)
                .toList();
        log.info("Find all channels by userId: userId={},조회된 채널 수={}", userId, channels.size());
        return channels;
    }

    @Override
    @Transactional
    public ChannelDto updateChannel(UUID id, ChannelUpdateRequestDto request) {
        log.info("channel update: name={},description={}", request.getNewName(),
                request.getNewDescription());
        Channel channel = channelRepository.findById(id)
                .orElseThrow(ChannelNotFoundException::new);

        if (channel.getType().equals(PRIVATE)) {
            log.warn("private channel reject: id={}", id);
            throw new PrivateChannelUpdateException();
        }
        channel.update(request.getNewName(), request.getNewDescription());
        log.info("update channel: id={}, name={}", id, channel.getName());
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID id) {
        log.info("channel delete: {}", id);
        if (!channelRepository.existsById(id)) {
            log.warn("channel not found: {}", id);
            throw new ChannelNotFoundException();
        }
        channelRepository.deleteById(id);
        log.info("delete channel: id={}", id);
    }
}
