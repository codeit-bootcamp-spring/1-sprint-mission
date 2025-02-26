package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageCreateDto messageCreateDto, List<BinaryContentCreateDto> binaryContentCreateDtos) {
        UUID channelId = messageCreateDto.channelId();
        UUID authorId = messageCreateDto.userId();

        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
        }
        if (!userRepository.existsById(authorId)) {
            throw new NoSuchElementException("Author with id " + authorId + " does not exist");
        }

        List<UUID> attachmentIds = binaryContentCreateDtos.stream()
                .map(attachments->{
                    String fileName = attachments.fileName();
                    String contentType = attachments.contentType();
                    byte[] bytes = attachments.bytes();

                    BinaryContent binaryContent = new BinaryContent(fileName,(long) bytes.length,contentType,bytes);
                    BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
                    return createdBinaryContent.getId();
                }).toList();

        Message message = new Message(
                messageCreateDto.content(),
                messageCreateDto.userId(),
                messageCreateDto.channelId(),
                attachmentIds
        );
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(()->new NoSuchElementException("Message not found with id: " + messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream().toList();
    }

    @Override
    public Message update(UUID messageId,MessageUpdateDto updateMessageDto) {
        Message message = messageRepository.findById(messageId)
                        .orElseThrow(()->new NoSuchElementException("Message not found"));
        message.updateContent(updateMessageDto.newContent());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
        //관련된 BinaryContent 삭제
        message.getAttachmentIds().forEach(id->binaryContentRepository.deleteById(id));
        //메시지 삭제
        messageRepository.deleteById(messageId);
    }
}
