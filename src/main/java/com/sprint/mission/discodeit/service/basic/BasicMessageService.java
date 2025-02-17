package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.entity.Message;
import com.sprint.mission.discodeit.dto.form.MessageWithContents;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void messageSaveWithContents(MessageWithContents messageParam, BinaryContent binaryContent) {
        if (binaryContent == null) {
            log.info("파일을 하나라도 등록하세요.");
            return;
        }
        binaryContentRepository.save(binaryContent);
        List<MultipartFile> files = new ArrayList<>();
        files.add((MultipartFile) binaryContent);
        Message message = new Message(messageParam.getContent(), messageParam.getSenderId(), messageParam.getChannelId());
        message.getMessageFiles().addAll(files);
        messageRepository.createMessage(message.getId(), message);
    }
    @Override
    public void messageSave(Message message) {
        if (message.getContent().trim().isEmpty()) {
            log.info("메세지 내용을 입력해주세요.");
            return;
        }
        messageRepository.createMessage(message.getId(), message);
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
        message.getMessageFiles().clear();
        messageRepository.deleteMessage(id);

    }
    private void validateMessageExits(UUID uuid) {
        if (!messageRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
