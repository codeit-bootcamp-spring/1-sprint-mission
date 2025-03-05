package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.CommonDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.util.EntryUtils;
import com.sprint.mission.discodeit.util.validation.MessageValidator;
import com.sprint.mission.discodeit.util.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final Validator<Message, MessageDTO.request> messageValidator = new MessageValidator();
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    private void saveAttachmentList(UUID messageId, List<MultipartFile> attachments) throws IOException {
        for (MultipartFile attachment : attachments) {
            binaryContentRepository.save(new BinaryContent(
                    messageId, attachment.getBytes(), BinaryContent.ContentType.PICTURE, attachment.getContentType(), attachment.getOriginalFilename()
            ));
        }
    }

    @Override
    public CommonDTO.idResponse create(MessageDTO.request messageReqDTO, List<MultipartFile> attachments) {
        try {
            messageValidator.validateCreate(messageReqDTO);
            Long messageId = messageRepository.save(new Message(messageReqDTO.author(), messageReqDTO.channel(), messageReqDTO.content()));
            UUID messageUUID = messageRepository.load(messageId).getId();
            // 첨부파일이 존재하면 저장
            if (attachments != null && !attachments.isEmpty()) {
                saveAttachmentList(messageUUID, attachments);
            }

            return CommonDTO.idResponse.from(messageId, messageUUID);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessageDTO.response find(Long id) {
        Message msg = messageRepository.load(id);
        if (msg == null) throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        return MessageDTO.response.from(EntryUtils.of(id, msg));
    }

    @Override
    public MessageDTO.response find(UUID uuid) {
        Map.Entry<Long, Message> msg = messageRepository.load(uuid);
        if (msg == null) throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
        return MessageDTO.response.from(msg);
    }

    @Override
    public List<MessageDTO.response> findAllByChannelId(UUID channelUUID) {
        Map<Long, Message> messageList = messageRepository.findMessagesByChannelId(channelUUID);
        return messageList.entrySet().stream()
                .map(MessageDTO.response::from)
                .collect(Collectors.toList());
    }

    @Override
    public CommonDTO.idResponse update(Long messageId, String content, List<MultipartFile> attachments) {
        boolean isUpdated = false;
        MessageDTO.update updateDTO = MessageDTO.update.of(messageId, content, attachments);
        try {
            Message msg = messageRepository.load(updateDTO.id());   // 기존 메시지 조회
            if (msg == null) {
                throw new CustomException(ErrorCode.MESSAGE_NOT_FOUND);
            }

            // 메시지 내용 업데이트
            if (updateDTO.content() != null && !updateDTO.content().isEmpty()) {
                msg.updateContent(updateDTO.content());
            }

            // 첨부파일이 존재하면 저장
            if (updateDTO.attachments() != null && !updateDTO.attachments().isEmpty()) {
                saveAttachmentList(msg.getId(), updateDTO.attachments());
                binaryContentRepository.deleteAllFileByReferenceId(msg.getId());
            }

            messageRepository.update(updateDTO.id(), msg);
            return CommonDTO.idResponse.from(updateDTO.id(), msg.getId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommonDTO.idResponse delete(Long id) {
        UUID uuid = messageRepository.load(id).getId();
        // 관련 도메인(첨부파일)도 함께 삭제
        if (binaryContentRepository.hasBinaryContent(uuid)) {
            Map<Long, BinaryContent> binaryContents = binaryContentRepository.findMessageImageByMessageId(uuid);
            binaryContents.keySet().forEach(binaryContentRepository::delete);
        }
        messageRepository.delete(id);
        return CommonDTO.idResponse.from(id, uuid);
    }

    @Override
    public CommonDTO.idResponse delete(UUID uuid) {
        // 관련 도메인(첨부파일)도 함께 삭제
        if (binaryContentRepository.hasBinaryContent(uuid)) {
            Map<Long, BinaryContent> binaryContents = binaryContentRepository.findMessageImageByMessageId(uuid);
            binaryContents.keySet().forEach(binaryContentRepository::delete);
        }
        Long deletedId = messageRepository.load(uuid).getKey();
        messageRepository.delete(deletedId);
        return CommonDTO.idResponse.from(deletedId, uuid);
    }
}
