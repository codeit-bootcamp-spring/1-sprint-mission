package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.channel.ChannelServiceFindDTO;
import com.sprint.mission.discodeit.dto.message.MessageServiceCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageServiceUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;

    private final ChannelService channelService;
    private final BinaryContentService binaryContentService;

    @Override
    public UUID create(MessageServiceCreateDTO dto) {
        Message message = new Message(dto.getContent(), dto.getUserId(), dto.getChannelId());

        if(dto.getFiles() != null && !dto.getFiles().isEmpty()){
            for(MultipartFile file : dto.getFiles()){
                UUID bId = binaryContentService.create(new BinaryContentCreateDTO(
                        dto.getUserId(),
                        dto.getChannelId(),
                        file));
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
        ChannelServiceFindDTO channelDTO = channelService.find(channelId);
        return messageRepository.findAllByChannelId(channelDTO.getId());
    }

    @Override
    public Message update(MessageServiceUpdateDTO dto) {
        Message findMessage = messageRepository.findOne(dto.getId());

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
