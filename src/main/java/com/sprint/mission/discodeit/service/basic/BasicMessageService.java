package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateResponse;
import com.sprint.mission.discodeit.dto.message.MessageFindBResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final InputHandler inputHandler;

    @Override
    public MessageCreateResponse createMessage(MessageCreateRequest messageCreateRequest,
                                               List<BinaryContentCreateRequest> binaryContentCreateRequests) {

        Message message = new Message(messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                messageCreateRequest.messageText(),
                binaryContentCreateRequests.stream()
                        .map(binaryContentService::createBinaryContent)
                        .map(BinaryContent::getId)
                        .toList()
                );

        messageRepository.saveMessage(message);

        return new MessageCreateResponse(
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                messageCreateRequest.messageText(),
                message.getAttachmentIds()
                );
    }

    @Override
    public Collection<MessageFindBResponse> findAllByChannelId(UUID channelId) {

        return messageRepository.findMessagesByChannelId(channelId).stream()
                        .map(message ->
                                new MessageFindBResponse(
                                        message.getId(),
                                message.getChannelId(),
                                message.getAuthorId(),
                                message.getMessageText()))
                        .toList();
    }

    @Override
    public MessageFindBResponse getMessageById(UUID id) {
        Message message =
        messageRepository.findMessageById(id)
                .orElseThrow(()-> new NoSuchElementException("해당 메세지가 없습니다."));
        return new MessageFindBResponse(
                message.getId(),
                message.getChannelId(),
        message.getAuthorId(),
        message.getMessageText());
    }

    @Override
    public void updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest) {
        // String messageText = inputHandler.getNewInput();
        Optional<Message> messageOptional = messageRepository.findMessageById(messageId);

        Message message = messageOptional.orElseThrow( () -> new NoSuchElementException("메세지가 존재하지 않습니다."));

        message.updateMessageText(messageUpdateRequest.newMessage());

        messageRepository.saveMessage(message);
    }

    @Override
    public void deleteMessageById(UUID id) {
        String keyword = inputHandler.getYesNOInput();
        if(keyword.equalsIgnoreCase("y")){
            messageRepository.findMessageById(id).stream()
                    .map(Message::getAttachmentIds)
                    .flatMap(List::stream) // List에서 하나씩
                    .forEach(binaryContentService::deleteBinaryContentById);
            messageRepository.deleteMessageById(id);
        }
    }
}
