package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelRequest;
import com.sprint.mission.discodeit.dto.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelValidator channelValidator;
//    private final MessageService messageService; // 순환 참조 발생함.
    private final MessageRepository messageRepository;
    private final ReadStatusService readStatusService;

    @Override
    public ChannelResponse createPublicChannel(ChannelRequest.CreatePublic request) {
        if (channelValidator.isValidTitle(request.title())) {
            Channel newChannel = Channel.createChannel(Channel.ChannelType.PUBLIC, request.title(), request.description());
            channelRepository.save(newChannel);
            log.info("Create Public Channel: {}", newChannel);
            return  ChannelResponse.entityToDto(newChannel, null, null);
        }
        return null;
    }

    @Override
    public ChannelResponse createPrivateChannel(ChannelRequest.CreatePrivate request) {
        Channel newChannel = Channel.createChannel(Channel.ChannelType.PRIVATE, null, null);
        channelRepository.save(newChannel);

        for (UUID userId : request.joinUsers()) {
            readStatusService.create(userId, newChannel.getId());
        }

        log.info("Create Private Channel: {}", newChannel);
        return ChannelResponse.entityToDto(newChannel, null, request.joinUsers());
    }

    @Override
    public List<ChannelResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .map(channel ->
                        ChannelResponse.entityToDto(channel, getLastMessageTime(channel.getId()), findJoinUsersById(channel.getId())))
                .filter(channel -> channel.channelType() == Channel.ChannelType.PUBLIC || channel.joinUsers().contains(userId))
                .collect(Collectors.toList());
    }

    @Override
    public ChannelResponse findById(UUID id) {
        Channel channel = findByIdOrThrow(id);
        return ChannelResponse.entityToDto(channel, getLastMessageTime(id), findJoinUsersById(id));
        // ChannelResponse.entityToDto(channel) 로 한다면
        // dto 에서 service 를 사용해서 getLastMessageTime(id), getJoinUsers(id)를 직접 호출하여 값을 넣어주기??
    }

    @Override
    public ChannelResponse update(UUID id, ChannelRequest.Update request) {
        Channel channel = findByIdOrThrow(id);

        if (channel.getChannelType() == Channel.ChannelType.PRIVATE) {
            log.error("Private Channel can not be changed");
        } else if (channelValidator.isValidTitle(request.title()) && channelValidator.isValidTitle(request.description())) {
            channel.update(request.title(), request.description());
            Channel updatedChannel = channelRepository.save(channel);
            log.info("Update Channel : {}", channel);
            return ChannelResponse.entityToDto(updatedChannel, null, findJoinUsersById(updatedChannel.getId()));
        }
        return null;
    }

    @Override
    public void deleteById(UUID id) {
//        messageService.deleteAllByChannelId(id); // 굳이 서비스 단에서 불러와야할까?
        messageRepository.findAllByChannelId(id);
        readStatusService.deleteAllByChannelId(id);
        channelRepository.deleteById(id);
    }

    @Override
    public Channel findByIdOrThrow(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Channel does not exist"));
    }

    private Instant getLastMessageTime(UUID id) {
        //List<Message> channelMessages = messageService.findAllByChannelId(id);
        List<Message> channelMessages = messageRepository.findAllByChannelId(id);
        if  (channelMessages.isEmpty()) {
            return null;
        }
        return channelMessages.get(-1).getCreatedAt();
    }

    private List<UUID> findJoinUsersById(UUID id) {
        return readStatusService.findAllByChannelId(id).stream().map(ReadStatus::getUserId).collect(Collectors.toList());
    }
}
