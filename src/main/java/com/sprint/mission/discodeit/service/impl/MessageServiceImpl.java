package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private BinaryContentService binaryContentService;

    @Override
    public Message create(MessageDTO.CreateMessageDTO createMessageDTO) {
        // 메시지 생성
        Message message = new Message();
        message.setContent(createMessageDTO.getContent());
        message.setChannelId(createMessageDTO.getChannelId());
        message.setAuthorId(createMessageDTO.getAuthorId());

        // 첨부파일 처리
        List<UUID> attachedFileIds = createMessageDTO.getAttachedFileIds();
        if (attachedFileIds != null && !attachedFileIds.isEmpty()) {
            attachedFileIds.forEach(fileId -> {
                BinaryContent file = binaryContentRepository.findById(fileId)
                        .orElseThrow(() -> new RuntimeException("File not found"));
                message.addAttachment(file);
            });
        }

        // 메시지 저장
        return messageRepository.save(message);
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public Message update(UUID messageId, MessageDTO.UpdateMessageDTO updateMessageDTO) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // 메시지 내용 수정
        message.setContent(updateMessageDTO.getNewContent());

        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        // 첨부파일 삭제
        List<BinaryContent> attachedFiles = message.getAttachments();
        if (attachedFiles != null) {
            attachedFiles.forEach(file -> binaryContentService.delete(file.getId()));
        }

        // 메시지 삭제
        messageRepository.delete(message);
    }
}
