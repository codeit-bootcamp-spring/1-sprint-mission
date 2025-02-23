package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.Channel;
import com.sprint.mission.discodeit.domain.entity.Message;
import com.sprint.mission.discodeit.domain.entity.Participant;
import com.sprint.mission.discodeit.domain.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ChannelRepository channelRepository;

    @Override
    public Message messageSave(UUID senderId,Message message) {
        if (message.getContent().trim().isEmpty()) {
            log.info("메세지 내용을 입력해주세요.");
            throw new IllegalArgumentException("메세지 내용을 입력해주세요.");
        }
        User user = userRepository.findById(senderId).orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Channel channel = channelRepository.findById(message.getChannelId()).orElseThrow(()->new IllegalArgumentException("채널을 찾을 수 없습니다."));

        Participant findParticipant=null;
        for (Participant participant : channel.getParticipants()) {
            if (participant.getUser().getId().equals(senderId)) {
                findParticipant = participant;
                break;
            }
        }
        if(findParticipant == null) {
            throw new IllegalStateException("채널에 가입하지 않았습니다.");
        }
        findParticipant.setMessage(message);
        return messageRepository.createMessage(message);
    }

    @Override
    public Message messageSaveWithContents(UUID senderId,Message message, List<BinaryContent> files) {
        if (message.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("메세지 내용을 입력해주세요.");
        }
        User user = userRepository.findById(senderId).orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Channel channel = channelRepository.findById(message.getChannelId()).orElseThrow(()->new IllegalArgumentException("채널을 찾을 수 없습니다."));

        Participant findParticipant=null;
        for (Participant participant : channel.getParticipants()) {
            if (participant.getUser().getId().equals(senderId)) {
                findParticipant = participant;
                break;
            }
        }
        if(findParticipant == null) {
            throw new IllegalStateException("채널에 가입하지 않았습니다.");
        }
        List<UUID> fileIds=new ArrayList<>();
        for (BinaryContent file : files) {
            BinaryContent savedFile = binaryContentRepository.save(file);
            fileIds.add(savedFile.getId());
        }
        Message fileMessage = new Message(message.getId(),message.getContent(), senderId, message.getChannelId(), fileIds);
        findParticipant.setMessage(fileMessage);
        return messageRepository.createMessage(fileMessage);
    }


    @Override
    public Optional<Message> findMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }

    @Override
    public Optional<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public void updateMessage(UUID id,String updateMessage) {
        validateMessageExits(id);
        //업로드 파일은 수정 불가하니깐 업데이트는 그대로 두었습니다.
        messageRepository.updateMessage(id, updateMessage);
    }


    @Override
    public void deleteMessage(UUID id) {
        validateMessageExits(id);
        Message message = messageRepository.findById(id).get();
        List<UUID> messageFiles = message.getMessageFiles();
        for (UUID messageFile : messageFiles) {
            binaryContentRepository.deleteById(messageFile);
        }
        messageRepository.deleteMessage(id);

    }
    private void validateMessageExits(UUID uuid) {
        if (messageRepository.findById(uuid).isEmpty()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
