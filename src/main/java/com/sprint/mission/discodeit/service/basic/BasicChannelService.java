package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelFindAllByUserIdDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserFindDTO;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    private final ChannelValidator channelValidator;


    @Override
    public UUID createPublic(String name, String description) {
        channelValidator.validateChannel(name, description);
        return channelRepository.save(new Channel(name, description, ChannelType.PUBLIC));
    }

    @Override
    public UUID createPrivate(UUID userId){
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);

        User findUser = userRepository.findOne(userId);
        Optional.ofNullable(findUser).orElseThrow(() -> new NoSuchElementException("사용자가 없습니다"));

        ReadStatus readStatus = new ReadStatus(findUser.getId(), channel.getId());
        readStatusRepository.save(readStatus);

        return channel.getId();
    }

    @Override
    public ChannelFindDTO find(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));

        return toDTO(findChannel);
    }

    @Override
    public List<ChannelFindDTO> findAllByUserId(UUID userId){
        List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId).toList();

        return channelRepository.findAll().stream()
                .filter(ch -> ch.getType().equals(ChannelType.PUBLIC) ||
                        channelIds.contains(ch.getId()))
                .map(this::toDTO)
                .toList();
    }

    public Channel update(UUID id, ChannelUpdateDTO dto){
        Channel findChannel = channelRepository.findOne(id);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));
        if(findChannel.getType() == ChannelType.PRIVATE){
            throw new IllegalStateException("PRIVATE 채널은 수정 불가능 합니다.");
        }
        findChannel.setChannel(dto.getName(), dto.getDescription());
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public UUID delete(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));

        readStatusRepository.deleteByChannelId(findChannel.getId());
        messageRepository.deleteByChannelId(findChannel.getId());

        return channelRepository.delete(findChannel.getId());
    }

    private ChannelFindDTO toDTO(Channel channel){
        List<UUID> ids = new ArrayList<>();
        if(channel.getType() == ChannelType.PRIVATE){
            readStatusRepository.findAllByChannelId(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .forEach(ids::add);
        }

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder()).orElse(Instant.MIN);

        return new ChannelFindDTO(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                ids,
                lastMessageAt
        );
    }

}
