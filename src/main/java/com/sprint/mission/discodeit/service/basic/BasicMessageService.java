package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ServiceException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusService userStatusService;

    @Override
    public Message create(CreateMessageRequest request) {
        if (request.content().isEmpty()) {
            throw new ServiceException(ErrorCode.EMPTY_CONTENT);
        }

        // 작성자와 채널에 대한 검증
        validUser(request.writerID());
        validChannel(request.channelID());

        // User가 메시지를 만들면 UserStatus 업데이트하기
        UserStatus userStatus = userStatusService.findByUserId(request.writerID()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        UpdateUserStatusRequest updateRequest = new UpdateUserStatusRequest(userStatus.getId(), Instant.now());
        userStatusService.update(updateRequest);

        // 해당 첨부자료가 레포지토리에 저장되어 있는지 확인
        if (request.attachmentsID() != null) {
            for (UUID attachmentID : request.attachmentsID()) {
                Optional<BinaryContent> attachment = binaryContentRepository.findById(attachmentID);
                if (attachment.isEmpty()) {
                    throw new ServiceException(ErrorCode.CANNOT_FOUND_ATTACHMENT);
                }
            }
        }


        Message message = new Message(request.content(), request.writerID(), request.channelID(), request.attachmentsID());
        messageRepository.save(message);
        return message;
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelID) {
        validChannel(channelID);

        List<Message> messages = messageRepository.findAll();
        return messages.stream()
                .filter(message -> message.getChannelID().equals(channelID))
                .collect(Collectors.toList());

    }

    @Override
    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public Message updateMessageContent(UpdateMessageRequest request) {
        validMessage(request.messageID());
        validUser(request.writerID());

        Message message = messageRepository.findById(request.messageID())
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));

        // 메시지 수정은 작성자만이 할 수 있다.
        if (!message.getWriterID().equals(request.writerID())) {
            throw new ServiceException(ErrorCode.MESSAGE_EDIT_NOT_ALLOWED);
        }

        // User가 메시지를 수정하면 UserStatus 업데이트하기
        UserStatus userStatus = userStatusService.findByUserId(request.writerID()).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_USERSTATUS));
        UpdateUserStatusRequest updateRequest = new UpdateUserStatusRequest(userStatus.getId(), Instant.now());
        userStatusService.update(updateRequest);

        message.update(request.newContent(), request.newAttachment());

        return messageRepository.save(message);
    }


    @Override
    public void deleteMessage(UUID messageID) {
        Message deleteMessage = messageRepository.findById(messageID).orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));

        List<UUID> attachmentsID = deleteMessage.getAttachmentsID();
        if (attachmentsID != null && !attachmentsID.isEmpty()) {
            attachmentsID.forEach(id -> {
                binaryContentRepository.findById(id)
                        .ifPresent(binaryContent -> binaryContentRepository.deleteById(id));
            });
        }

        messageRepository.delete(deleteMessage);
    }

    private void validUser(UUID userId) {
        if (!userRepository.userExistById(userId)) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_USER);
        }
    }

    private void validChannel(UUID channelId) {
        if (!channelRepository.channelExistById(channelId)) {
            throw new ServiceException(ErrorCode.CANNOT_FOUND_CHANNEL);
        }
    }

    private void validMessage(UUID messageId) {
        messageRepository.findById(messageId)
                .orElseThrow(() -> new ServiceException(ErrorCode.CANNOT_FOUND_MESSAGE));
    }
}