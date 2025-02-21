package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicMessageService implements MessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public Message create(MessageDto messageDto) {
        Channel channel = channelRepository.findById(messageDto.channelId()).orElseThrow(()->new NoSuchElementException("Channel not found"));
        User user = userRepository.findById(messageDto.userId()).orElseThrow(()->new NoSuchElementException("User not found"));

        List<BinaryContent> attachments = binaryContentRepository.findAllById(messageDto.binaryContentIds());

        Message message = new Message(messageDto.content(),user,channel,attachments);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(()->new NoSuchElementException("Message not found with id: " + messageId));
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAll().stream()
                .filter(m->m.getChannel().getId().equals(channelId))
                .toList();
    }

    @Override
    public Message update(UpdateMessageDto updateMessageDto) {
        Message message = messageRepository.findById(updateMessageDto.messageId())
                        .orElseThrow(()->new NoSuchElementException("Message not found"));
        message.updateContent(updateMessageDto.content());
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        //관련된 BinaryContent 삭제
        binaryContentRepository.deleteById(messageId);
        //메시지 삭제
        messageRepository.deleteById(messageId);
    }
}
