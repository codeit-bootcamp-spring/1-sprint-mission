package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.execption.message.NotMessageCreatorException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserValidator userValidator;
    private final ChannelValidator channelValidator;

    private final MessageMapper messageMapper;
    private final PageResponseMapper<MessageDto> pageResponseMapper;
    private final UserMapper userMapper;

    private final MultipartFileConverter multipartFileConverter;
    private final BinaryContentStorage binaryContentStorage;
    private final SessionRegistry sessionRegistry;

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

                binaryContentStorage.put(savedContent.getId(),
                    multipartFileConverter.toByteArray(multipartFile));
                message.addAttachment(savedContent);
            });

        return messageMapper.toMessageDto(message, getOnline(message.getSender()), userMapper);
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
            .map(message -> messageMapper.toMessageDto(message, getOnline(message.getSender()),
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

        return messageMapper.toMessageDto(foundMessage, getOnline(foundMessage.getSender()),
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

    private boolean getOnline(User user) {
        boolean online = false;
        List<Object> principals = sessionRegistry.getAllPrincipals();
        for (Object principal : principals) {
            if (Objects.equals(((UserDetails) principal).getUsername(), user.getName())) {
                online = true;
            }
        }
        return online;
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
