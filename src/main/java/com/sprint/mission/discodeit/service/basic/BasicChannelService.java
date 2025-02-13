package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindAllByUserIdDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceUpdateDTO;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.user.UserServiceFindDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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

    private final ReadStatusService readStatusService;
    private final UserService userService;
    private final MessageService messageService;
    private final ChannelValidator channelValidator;

    //서비스 로직
    @Override
    public UUID createPublic(String name, String description) {
        channelValidator.validateChannel(name, description);
        Channel channel = new Channel(name, description, ChannelType.PUBLIC);
        return channelRepository.save(channel);
    }

    @Override
    public UUID createPrivate(UUID userId){
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        channelRepository.save(channel);
        channel.addUsers(userId);

        UserServiceFindDTO findUserDTO = userService.find(userId);
        readStatusService.create(new ReadStatusCreateDTO(channel.getId(), findUserDTO.getId()));
        return channel.getId();
    }

    @Override
    public ChannelServiceFindDTO find(UUID id) {
        Channel findChannel = channelRepository.findOne(id);
        Optional.ofNullable(findChannel)
                .orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다."));

        Instant recentTime = messageService.findAllByChannelId(findChannel.getId()).stream()
                .map(Message::getCreatedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        List<UUID> userIds = (findChannel.getType() == ChannelType.PRIVATE)
                ? findChannel.getUserIds()
                : Collections.emptyList();

        return new ChannelServiceFindDTO(
                findChannel.getId(),
                findChannel.getName(),
                findChannel.getDescription(),
                recentTime,
                userIds);
    }

    @Override
    public List<ChannelServiceFindDTO> findAll() {
        List<ChannelServiceFindDTO> list = channelRepository.findAll().stream()
                .map(channel -> {

                    Instant recentTime = messageService.findAllByChannelId(channel.getId()).stream()
                            .map(Message::getCreatedAt)
                            .max(Comparator.naturalOrder())
                            .orElse(null);

                    List<UUID> userIds = (channel.getType() == ChannelType.PRIVATE)
                            ? channel.getUserIds()
                            : Collections.emptyList();

                    return new ChannelServiceFindDTO(
                            channel.getId(),
                            channel.getName(),
                            channel.getDescription(),
                            recentTime,
                            userIds);
                }).toList();
        return list;
    }

    @Override
    public List<ChannelServiceFindAllByUserIdDTO> findAllByUserId(UUID userId){
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() != ChannelType.PRIVATE
                || channel.getUserIds().contains(userId))
                .map(channel -> new ChannelServiceFindAllByUserIdDTO(
                        channel.getId(),
                        channel.getName(),
                        channel.getDescription(),
                        channel.getType()
                )).toList();
    }

    public Channel update(ChannelServiceUpdateDTO dto){
        Channel findChannel = channelRepository.findOne(dto.getId());
        if(findChannel.getType() == ChannelType.PRIVATE){
            throw new IllegalStateException("PRIVATE 채널은 수정 불가능 합니다.");
        }
        findChannel.setChannel(dto.getName(), dto.getDescription());
        channelRepository.update(findChannel);
        return findChannel;
    }

    @Override
    public UUID deleteChannel(UUID id) {
        Channel findChannel = channelRepository.findOne(id);

        readStatusService.findAll().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(findChannel.getId()))
                .forEach(readStatus -> readStatusService.delete(readStatus.getId()));

        messageService.findAll().stream()
                .filter(message -> message.getChannelId().equals(findChannel.getId()))
                .forEach(message -> messageService.delete(message.getId()));


        return channelRepository.delete(findChannel.getId());
    }
}
