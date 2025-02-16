package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final ChannelMapper channelMapper;
    private final MessageSource messageSource;


    @Override
    public CreatePrivateChannelResponse createPrivateChannel(CreatePrivateChannelRequest request) {
        Channel channel = Channel.builder().
                type(ChannelType.PRIVATE)
                .build();
        Channel savedChannel = channelRepository.save(channel);

        request.getChannelMembersIds().forEach(memberId ->
                readStatusRepository.save(new ReadStatus(memberId, channel.getId()))
        );

        Set<UUID> usersByChannelId = readStatusRepository.findUsersByChannelId(savedChannel.getId());
        return channelMapper.toPrivateResponse(savedChannel,usersByChannelId);
    }

    @Override
    public CreatePublicChannelResponse createPublicChannel(CreatePublicChannelRequest request) {
        Channel channel = Channel.builder()
                .name(request.getName())
                .topic(request.getTopic())
                .type(ChannelType.PUBLIC).build();
        Channel savedChannel = channelRepository.save(channel);

        return channelMapper.toPublicResponse(channel);
    }

    public FindPrivateChannelResponse findPrivateChannel(UUID privateChannelId){
        Optional<Channel> optChannel = channelRepository.findById(privateChannelId);
        if (optChannel.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.channel.id.notfound", new Object[]{privateChannelId}, LocaleContextHolder.getLocale()));
        }

        Channel channel = optChannel.get();
        Optional<Instant> recentMessageTime = messageRepository.findRecentMessageByChannelId(channel.getId());

        Set<UUID> membersIds = readStatusRepository.findUsersByChannelId(channel.getId());
        return channelMapper.toFindPrivateResponse(channel, membersIds);
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public boolean editChannel(UUID id, String name, String topic, ChannelType type) {
        Optional<Channel> byId = channelRepository.findById(id);
        if (byId.isPresent()) {
            Channel channel = byId.get();
            channel.update(name, topic, type);
            channelRepository.save();
        }
        return false;
    }

    @Override
    public Channel getChannelDetails(UUID id) {
        return null;
    }

    @Override
    public boolean deleteChannel(UUID id) {
        return channelRepository.delete(id);
    }
}
