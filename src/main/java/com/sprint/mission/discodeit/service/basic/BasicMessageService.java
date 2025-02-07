package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public UUID create(MessageDto messageDto) {
        UUID savedId = messageRepository.save(messageDto.senderId(),messageDto.channelId(), messageDto.content()); //메시지 저장
        if(messageDto.fileList()!=null && !messageDto.fileList().isEmpty()) {
            List<File> files = messageDto.fileList();
            for(File file : files) {
                binaryContentRepository.save(new BinaryContentDto(savedId, file)); // 메시지와 함께 저장될 파일들
            }
        }
        return savedId;
    }
    @Override
    public Message findById(UUID id) {
        return messageRepository.findMessageById(id);
    }

    @Override
    public List<Message> findAllByUserId(UUID userId) {
        return messageRepository.findMessagesById(userId);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findMessagesById(channelId);
    }

    @Override
    public void update(MessageDto messageDto) {
        messageRepository.update(messageDto.id(), messageDto.content());
    }

    @Override
    public void delete(UUID id) {
        //같이 등록된 첨부파일도 삭제되어야 함
        List<BinaryContent> binaryContents = binaryContentRepository.findByDomainId(id);
        for (BinaryContent binaryContent : binaryContents) {
            binaryContentRepository.delete(binaryContent.getId());
        }
        messageRepository.delete(id);
    }
}
