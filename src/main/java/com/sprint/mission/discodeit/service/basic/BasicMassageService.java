package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.dto.response.MessageResponse;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.NewMessageEvent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.global.interceptor.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMassageService implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentService binaryContentService;
    private final ReadStatusRepository readStatusRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public MessageResponse createMessage(MessageRequest.Create request,
        List<MultipartFile> messageFiles) {

        UUID userId = request.getAuthorId();
        UUID channelId = request.getChannelId();
        UUID requestId = UUID.fromString(MDC.get(MDCLoggingInterceptor.REQUEST_ID));

        User user = userRepository.findById(userId).orElseThrow(() ->
            new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

        Channel channel = channelRepository.findById(channelId).orElseThrow(() ->
            new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId)));

        Message message = Message.createMessage(request.getContent(), channel, user);
        Optional.ofNullable(messageFiles).ifPresent(files ->
            files.forEach(file -> {
                    BinaryContent newFile = binaryContentService.save(file, userId, requestId);
                    message.insertAttachments(newFile);
                }
            )
        );
        messageRepository.save(message);

        // 유저 ID만 필요하니까
        List<UUID> receiverIds = readStatusRepository.findReceiverIdsByChannelIdAndNotificationEnabledTrue(
            channelId);

        NewMessageEvent event = NewMessageEvent.builder()
            .authorId(userId)
            .authorName(user.getUsername())
            .channelId(channelId)
            .channelName(channel.getName())
            .channelType(channel.getType())
            .receiverIds(receiverIds)
            .content(request.getContent())
            .build();
        eventPublisher.publishEvent(event);

        log.info("Created message - id: {}", message.getId());
        return messageMapper.entityToDto(message);
    }

    @Override
    public PageResponse<MessageResponse> findAllByChannelId(UUID channelId, Instant createdAt,
        Pageable pageable) {
        channelRepository.findById(channelId).orElseThrow(() ->
            new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId)));

        Slice<Message> slice = messageRepository.findAllByChannelIdWithAuthor(channelId,
            Optional.ofNullable(createdAt).orElse(Instant.now()), pageable);
        Slice<MessageResponse> responseSlice = slice.map(messageMapper::entityToDto);

        Instant nextCursor = null;
        if (!slice.getContent().isEmpty()) {
            nextCursor = slice.getContent().get(slice.getContent().size() - 1)
                .getCreatedAt();
        }

        return PageResponseMapper.fromSlice(responseSlice, nextCursor);
    }

    @Override
    public MessageResponse findById(UUID id) {
        return messageMapper.entityToDto(findByIdOrThrow(id));
    }

    @Override
    @Transactional
    public MessageResponse update(UUID id, MessageRequest.Update request) {
        Message message = findByIdOrThrow(id);
        message.updateContent(request.getNewContent());
        messageRepository.save(message);

        log.info("Updated message - id: {}", message.getId());
        return messageMapper.entityToDto(message);
    }

    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        messageRepository.deleteById(id);
        log.info("Deleted message - id: {}", id);
    }

    private Message findByIdOrThrow(UUID id) {
        return messageRepository.findById(id)
            .orElseThrow(
                () -> new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND, Map.of("id", id)));
    }
}
