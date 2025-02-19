package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public Message create(String content, UUID channelId, UUID writerId) {
        if (!channelRepository.existsId(channelId)) {
            throw new NoSuchElementException("채널이 존재하지 않습니다.");
        }
        if (!userRepository.existsId(writerId)) {
            throw new NoSuchElementException("유저가 존재하지 않습니다.");
        }

        Channel channel = channelRepository.findById(channelId).get();
        boolean isMember = channel.getMemberList().stream()
                .anyMatch(user -> user.getId().equals(writerId));

        if (!isMember) {
            throw new IllegalArgumentException("해당 채널의 멤버만 메시지를 작성할 수 있습니다.");
        }

        Message message = new Message(channelId, content ,writerId);
        return messageRepository.save(message);
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));
    }

    @Override
    public Message findByChannel(UUID channelId) {
        return null;
    }

    @Override
    public Message findByUser(UUID userId) {
        return null;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, UUID writerId, String newContent) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

        if (!message.getWriterId().equals(writerId)) {
            throw new IllegalArgumentException("작성자만 수정 할 수 있습니다.");
        }

        message.update(newContent);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId, UUID writerId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("메시지가 존재하지 않습니다."));

        if (!message.getWriterId().equals(writerId)) {
            throw new IllegalArgumentException("작성자만 삭제 할 수 있습니다.");
        }

        messageRepository.delete(messageId);
    }
}
