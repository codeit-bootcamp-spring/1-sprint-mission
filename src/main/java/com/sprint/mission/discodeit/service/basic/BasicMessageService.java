package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.domain.BinaryContentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository repository;
    private final BinaryContentRepository binaryContentRepository;
    @Override
    public UUID create(MessageDto messageDto) {
        UUID savedId = repository.save(messageDto.id(), messageDto.content()); //메시지 저장
        if(messageDto.fileList()!=null && !messageDto.fileList().isEmpty()) {
            List<File> files = messageDto.fileList();
            for(File file : files) {
                binaryContentRepository.save(new BinaryContent(savedId, file)); // 메시지와 함께 저장될 파일들
            }
        }
        return savedId;
    }
    @Override
    public Message findById(UUID id) {
        return repository.findMessageById(id);
    }

    @Override
    public List<Message> findAllByUserId(UUID userId) {
        return repository.findMessagesById(userId);
    }

    @Override
    public List<Message> findAll() {
        return repository.findAll();
    }
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return repository.findMessagesById(channelId);
    }

    @Override
    public void update(MessageDto messageDto) {
        repository.update(messageDto.id(), messageDto.content());
    }

    @Override
    public void delete(UUID id) {
        //같이 등록된 첨부파일도 삭제되어야 함
        List<BinaryContent> binaryContents = binaryContentRepository.findByDomainId(id);
        for (BinaryContent binaryContent : binaryContents) {
            binaryContentRepository.delete(binaryContent.getId());
        }
        repository.delete(id);
    }
}
