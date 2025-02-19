package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageService.MessageCreateRequestDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageResponseDTO;
import com.sprint.mission.discodeit.dto.MessageService.MessageUpdateRequestDTO;
import com.sprint.mission.discodeit.dto.binaryContentService.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Qualifier("BasicMessageService")
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    @Override
    public Message create(MessageCreateRequestDTO request) {
       if(!channelRepository.existsById(request.channelId())) {
           throw new NoSuchElementException("Channel with id " + request.channelId() + " not found");
       }
       if(!userRepository.existsById(request.authorId())) {
           throw new NoSuchElementException("User with id " + request.authorId() + " not found");
       }

       Message message = new Message(request.content(), request.channelId(), request.authorId());
       messageRepository.save(message);
       //첨부파일 저장
       if (request.fileIds() != null && !request.fileIds().isEmpty()) {
           for (UUID fileId : request.fileIds()) {
               BinaryContent binaryContent = binaryContentRepository.findById(fileId)
                       .orElseThrow(() -> new NoSuchElementException("file with id" + fileId + "not found"));

               binaryContent.setMessageId(message.getId());
               binaryContentRepository.save(binaryContent);
           }

       }
       return message;
    }

    @Override
    public Message find(UUID messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));
    }

    @Override
    public List<MessageResponseDTO> findAllByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }

        List<Message> messages = messageRepository.findAllByChannelId(channelId);

        return messages.stream()
                .map(message -> {
                    // ✅ 직접 첨부파일 리스트 생성
                    List<BinaryContentDTO> attachments = binaryContentRepository.findAllByMessageId(message.getId()).stream()
                            .map(file -> new BinaryContentDTO(file.getId(), file.getFileName(), file.getMimeType()))
                            .toList();

                    return MessageResponseDTO.from(message, attachments);
                })
                .toList();
    }


    @Override
    public MessageResponseDTO update(MessageUpdateRequestDTO request) {
        Message message = messageRepository.findById(request.messageId())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + request.messageId() + " not found"));

        if (request.newContent() != null && !request.newContent().isBlank()) {
            message.update(request.newContent());
        }

        if (request.fileIds() != null) {
            // 기존 첨부파일 삭제
            binaryContentRepository.deleteByMessageId(request.messageId());

            // 새로운 첨부파일 등록
            for (UUID fileId : request.fileIds()) {
                BinaryContent file = binaryContentRepository.findById(fileId)
                        .orElseThrow(() -> new NoSuchElementException("File with id " + fileId + " not found"));
                file.setMessageId(message.getId());
                binaryContentRepository.save(file);
            }
        }

        messageRepository.save(message);

        // 여기서 직접 첨부파일 리스트 생성
        List<BinaryContentDTO> attachments = binaryContentRepository.findAllByMessageId(message.getId()).stream()
                .map(file -> new BinaryContentDTO(file.getId(), file.getFileName(), file.getMimeType()))
                .toList();

        return MessageResponseDTO.from(message, attachments);
    }


    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }

        binaryContentRepository.deleteByMessageId(messageId); // 첨부파일 삭제
        readStatusRepository.deleteByMessageId(messageId);    // ReadStatus 삭제
        messageRepository.deleteById(messageId);
    }
}
