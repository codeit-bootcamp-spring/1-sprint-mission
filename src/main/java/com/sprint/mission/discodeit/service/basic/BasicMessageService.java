package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentService binaryContentService;

    private final MessageMapper messageMapper;
    private final PageResponseMapper<MessageResponseDto> pageResponseMapper;

    private void addAttachmentToMessage(Message message, List<MultipartFile> attachments) throws IOException {
        for (MultipartFile attachment : attachments) {
            // 1. BinaryContent 저장
            UUID id = binaryContentService.create(attachment).id();
            BinaryContent binaryContent = binaryContentRepository.findById(id)
                    .orElseThrow(() -> new BinaryContentNotFoundException(id));

            // 2. MessageAttachment 생성
            MessageAttachment messageAttachment = new MessageAttachment(message, binaryContent);

            // 3. Message 엔티티에 추가
            message.getAttachments().add(messageAttachment);
        }
        log.info("첨부파일이 등록되었습니다. - id: {}", message.getAttachments().stream().map(MessageAttachment::getId).collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public MessageResponseDto create(MessageRequestDto messageReqDTO, List<MultipartFile> attachments) throws IOException {
        log.debug("메시지 생성 요청 - 메시지 요청 데이터: {}, 첨부파일 수: {}", messageReqDTO, attachments.size());
        Message message = messageMapper.toEntity(messageReqDTO);
        if (attachments != null && !attachments.isEmpty()) {
            addAttachmentToMessage(message, attachments);
        }
        Message savedMessage = messageRepository.save(message);

        log.info("메시지가 생성되었습니다. - id: {}", savedMessage.getId());
        return messageMapper.toResponseDto(savedMessage);
    }

    @Override
    public MessageResponseDto find(UUID messageId) {
        log.debug("메시지 조회 요청 - id: {}", messageId);
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));

        log.info("메시지 조회에 성공하였습니다. - id: {}", messageId);
        return messageMapper.toResponseDto(message);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        log.debug("채널 메시지 전체 조회 요청 - channelId: {}", channelId);
        return messageRepository.findAll().stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .map(messageMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResponse<MessageResponseDto> findMessagesByChannelId(UUID channelId, UUID cursor) {
        log.debug("채널 메시지 페이지 조회 요청 - channelId: {}, cursor: {}", channelId, cursor);
        int MESSAGE_PAGE_SIZE = 50;
        Pageable pageable = PageRequest.of(0, MESSAGE_PAGE_SIZE);
        Slice<Message> messages;

        if (cursor == null) {
            messages = messageRepository.findMessagesFirstPage(channelId, pageable);
        } else {
            messages = messageRepository.findMessagesNextPage(channelId, cursor, pageable);
        }

        UUID nextCursor = messages.hasNext() ? messages.getContent().get(messages.getNumberOfElements() - 1).getId() : null;

        log.info("채널 메시지 페이지 조회가 완료되었습니다. - channelId: {}, 메시지 수: {}, 다음 커서: {}",
                channelId, messages.getNumberOfElements(), nextCursor);
        return pageResponseMapper.fromSlice(messages.map(messageMapper::toResponseDto), nextCursor);
    }

    @Transactional
    @Override
    public MessageResponseDto update(UUID messageId, String content, List<MultipartFile> attachments) throws IOException {
        log.debug("메시지 수정 요청 - 수정 요청 id: {}, 수정 내용: {}", messageId, content);

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));
        message.updateContent(content);

        if (attachments != null && !attachments.isEmpty()) {
            log.info("메시지 첨부파일 업데이트 - id: {}, 첨부파일 수: {}", message.getId(), attachments.size());
            if (!message.getAttachments().isEmpty()) {
                message.clearAttachments();
                messageRepository.save(message);
                log.info("메시지에 첨부파일이 추가되었습니다. - messageId: {}, 첨부파일 수: {}", message.getId(), attachments.size());
            }
            addAttachmentToMessage(message, attachments);
        }

        log.info("메시지가 수정되었습니다. - id: {}", message.getId());
        return messageMapper.toResponseDto(message);
    }

    @Transactional
    @Override
    public MessageResponseDto delete(UUID messageId) {
        log.debug("메시지 삭제 요청 - 삭제 요청 id: {}", messageId);

        Message deletedMessage = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException(messageId));
        messageRepository.delete(deletedMessage);

        log.info("메시지가 삭제되었습니다. - id: {}", deletedMessage.getId());
        return messageMapper.toResponseDto(deletedMessage);
    }
}
