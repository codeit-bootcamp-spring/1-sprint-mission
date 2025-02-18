package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.global.error.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.error.execption.message.NotMessageCreatorException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.DeleteMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    private final UserValidator userValidator;
    private final ChannelValidator channelValidator;

    private final MultipartFileConverter multipartFileConverter;

    private final MessageMapper messageMapper;

    @Override
    public Message createMessage(CreateMessageRequest createMessageRequest,
                                 List<MultipartFile> multipartFileList) {

        User sendUser = userValidator.validateUserExistsByUserId(createMessageRequest.sendUserId());
        Channel foundChannel = channelValidator.validateChannelExistsByChannelId(createMessageRequest.channelId());
        Message message = messageMapper.toEntity(sendUser, foundChannel, createMessageRequest.content());

        multipartFileList.forEach(
                multipartFile -> {
                    // binaryContentService의 create와 똑같이 동작
                    BinaryContent binaryContent = BinaryContent.of(multipartFileConverter.toByteArray(multipartFile));
                    binaryContentRepository.saveBinaryContent(binaryContent);
                    message.addBinaryContent(binaryContent);
                }
        );

        return messageRepository.saveMessage(message);
    }

    // 요구 사항에 없기에 시간 남으면 수정
    @Override
    public Message findMessageByIdOrThrow(UUID messageId) {
        // messageValidator 로 분리하는 것이 일관성 있지만 다른 클래스에서 참조 안하기에 일단 남겨둔다.
        return Optional.ofNullable(messageRepository.findMessageById(messageId))
                .orElseThrow(() -> new MessageNotFoundException("id: " + messageId));
    }

    @Override
    public List<Message> findAllMessagesByChannelId(UUID channelId) {
        Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);
        return messageRepository.findAllMessagesByChannel(channel);
    }

    @Override
    public Message updateMessage(UpdateMessageRequest updateMessageRequest, List<MultipartFile> multipartFileList) {
        UUID sendUserId = updateMessageRequest.sendUserId();
        userValidator.validateUserExistsByUserId(sendUserId);

        Message foundMessage = findMessageByIdOrThrow(updateMessageRequest.messageId());

        if (foundMessage.isNotOwner(sendUserId)) {
            throw new NotMessageCreatorException("id: " + sendUserId);
        }

        foundMessage.updateContent(updateMessageRequest.content());
        updateBinaryContentList(foundMessage, multipartFileList);

        return messageRepository.saveMessage(foundMessage);
    }

    @Override
    public void deleteMessage(DeleteMessageRequest deleteMessageRequest) {
        UUID messageId = deleteMessageRequest.messageId();
        UUID sendUserId = deleteMessageRequest.sendUserId();

        Message foundMessage = findMessageByIdOrThrow(messageId);

        if (foundMessage.isNotOwner(sendUserId)) {
            throw new NotMessageCreatorException("id: " + sendUserId);
        }

        foundMessage.getBinaryContentList()
                        .forEach(binaryContent -> binaryContentRepository.removeBinaryContent(binaryContent.getId()));

        messageRepository.removeMessage(messageId);
    }

    private void updateBinaryContentList(Message message, List<MultipartFile> multipartFileList) {
        List<BinaryContent> originBinaryContentList = message.getBinaryContentList();
        List<BinaryContent> newBinaryContentList =
                multipartFileList.stream()
                        .map(multipartFile -> BinaryContent.of(multipartFileConverter.toByteArray(multipartFile)))
                        .toList();

        // 두 리스트 비교 -> 새로운 파일은 repository save, 없어진 파일은 remove
        // 기존 리스트에 포함되지 않은 것 새로 저장
        newBinaryContentList.stream()
                .filter(binaryContent -> !originBinaryContentList.contains(binaryContent))
                .forEach(binaryContent -> {
                    binaryContentRepository.saveBinaryContent(binaryContent); // repository 저장
                    message.addBinaryContent(binaryContent); // message binaryContentList에 추가
                });


        // 새로운 리스트에 포함되지 않은 것을 삭제
        originBinaryContentList.stream()
                .filter(binaryContent -> !newBinaryContentList.contains(binaryContent))
                .forEach(binaryContent -> {
                    binaryContentRepository.removeBinaryContent(binaryContent.getId());
                    message.deleteBinaryContent(binaryContent);
                });
    }
}
