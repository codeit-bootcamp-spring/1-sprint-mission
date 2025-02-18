package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final MessageValidator messageValidator;

    @Override
    public UUID create(MessageCreateDTO dto) {
        messageValidator.validateMessage(dto.getContent(), dto.getUserId(), dto.getChannelId());
        Message message = new Message(dto.getContent(), dto.getUserId(), dto.getChannelId());

        if(dto.getFiles() != null && !dto.getFiles().isEmpty()){
            for(MultipartFile file : dto.getFiles()){
                UUID bId = binaryContentService.create(new BinaryContentCreateDTO(file));
                message.addBinaryContent(bId);
            }
        }
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID id) {
        Message findMessage = messageRepository.findOne(id);
        return Optional.ofNullable(findMessage)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지가 없습니다."));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    @Override
    public Message update(UUID id, MessageUpdateDTO dto) {
        Message findMessage = messageRepository.findOne(id);

        findMessage.setMessage(dto.getContent());
        messageRepository.update(findMessage);
        return findMessage;
    }

    @Override
    public UUID delete(UUID id) {
        Message findMessage = messageRepository.findOne(id);

        for(UUID binaryContentId : findMessage.getBinaryContentIds()){
            binaryContentService.delete(binaryContentId);
        }
        return messageRepository.delete(findMessage.getId());
    }
}
