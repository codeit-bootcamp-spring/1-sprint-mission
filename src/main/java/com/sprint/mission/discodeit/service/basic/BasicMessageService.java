package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.LoginStatusChecker;
import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.CreateMessageEvent;
import com.sprint.mission.discodeit.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.execption.message.NotMessageCreatorException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    private final UserValidator userValidator;
    private final ChannelValidator channelValidator;

    private final MessageMapper messageMapper;
    private final PageResponseMapper<MessageDto> pageResponseMapper;
    private final UserMapper userMapper;

    private final MultipartFileConverter multipartFileConverter;
    private final BinaryContentStorage binaryContentStorage;
    private final LoginStatusChecker loginStatusChecker;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public MessageDto createMessage(CreateMessageRequest createMessageRequest,
        List<MultipartFile> multipartFileList) {
        User sender = userValidator.validateUserExistsByUserId(createMessageRequest.authorId());
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(
            createMessageRequest.channelId());
        Message message = messageRepository.saveMessage(
            Message.of(sender, foundChannel, createMessageRequest.content())
        );

        Optional.ofNullable(multipartFileList)
            .orElse(Collections.emptyList())
            .forEach(multipartFile -> {
                BinaryContent savedContent = binaryContentRepository.saveBinaryContent(
                    BinaryContent.of(multipartFile.getOriginalFilename(),
                        multipartFile.getSize(),
                        multipartFile.getContentType())
                );

                TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            CompletableFuture<UUID> uuidCompletableFuture =
                                null;
                            try {
                                uuidCompletableFuture = binaryContentStorage.putAsync(
                                    savedContent.getId(),
                                    multipartFileConverter.toByteArray(multipartFile));
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            uuidCompletableFuture.whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    savedContent.updateBinaryContentUploadStatus(
                                        BinaryContentUploadStatus.FAILED);
                                } else {
                                    savedContent.updateBinaryContentUploadStatus(
                                        BinaryContentUploadStatus.SUCCESS);
                                }
                                binaryContentRepository.saveBinaryContent(savedContent);
                            });
                        }
                    }
                );

                message.addAttachment(savedContent);
            });

        List<ReadStatus> readStatuses = readStatusRepository.findAllReadStatusByChannel(
            foundChannel);
        readStatuses.forEach(
            readStatus -> {
                if (readStatus.isNotificationEnabled()) {
                    publisher.publishEvent(new CreateMessageEvent(
                        NotificationType.NEW_MESSAGE,
                        foundChannel,
                        message
                    ));
                }
            }
        );

        return messageMapper.toMessageDto(message,
            loginStatusChecker.getOnline(message.getSender()), userMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageDto> findAllMessagesByChannelId(UUID channelId, Instant cursor,
        Pageable pageable) {
        Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);
        Slice<Message> messageSlice =
            cursor == null ? messageRepository.findSlicedMessagesByChannel(channel, pageable)
                : messageRepository.findSlicedMessagesByChannel(channel, cursor, pageable);
        List<MessageDto> messageDtoList = messageSlice.getContent().stream()
            .map(message -> messageMapper.toMessageDto(message,
                loginStatusChecker.getOnline(message.getSender()),
                userMapper))
            .toList();

        Slice<MessageDto> messageDtoPage = new SliceImpl<>(messageDtoList, pageable,
            messageSlice.hasNext());

        return pageResponseMapper.fromSlice(messageDtoPage);
    }

    @Override
    @Transactional
    public MessageDto updateMessage(UUID messageId, UpdateMessageRequest updateMessageRequest,
        UserDetails userDetails) {
        Message foundMessage = findMessageById(messageId);

        validateMessageSender(foundMessage, userDetails);

        foundMessage.updateContent(updateMessageRequest.newContent());

        return messageMapper.toMessageDto(foundMessage,
            loginStatusChecker.getOnline(foundMessage.getSender()),
            userMapper);
    }

    @Override
    @Transactional
    public void deleteMessage(UUID messageId, UserDetails userDetails) {
        Message foundMessage = findMessageById(messageId);

        validateMessageAuthority(foundMessage, userDetails);

        messageRepository.removeMessage(messageId);
    }

    private Message findMessageById(UUID messageId) {
        return messageRepository.findMessageById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(Map.of("messageId", messageId)));
    }

    private void validateMessageSender(Message message, UserDetails userDetails) {
        if (!message.getSender().getName().equals(userDetails.getUsername())) {
            throw new NotMessageCreatorException(
                Map.of("messageId", message.getId(), "username", userDetails.getUsername()));
        }
    }

    private void validateMessageAuthority(Message message, UserDetails userDetails) {
        boolean isSender = message.getSender().getName().equals(userDetails.getUsername());
        boolean isAdmin = userDetails.getAuthorities().stream()
            .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isSender && !isAdmin) {
            throw new NotMessageCreatorException(
                Map.of("messageId", message.getId(), "username", userDetails.getUsername()));
        }
    }
}
