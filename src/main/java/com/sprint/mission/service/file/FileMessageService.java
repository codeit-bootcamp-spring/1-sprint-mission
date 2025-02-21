package com.sprint.mission.service.file;

import com.sprint.mission.dto.request.BinaryMessageContentDto;
import com.sprint.mission.dto.request.MessageDtoForCreate;
import com.sprint.mission.dto.request.MessageDtoForUpdate;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.file.main.FileChannelRepository;
import com.sprint.mission.repository.file.main.FileMessageRepository;
import com.sprint.mission.repository.file.main.FileUserRepository;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.jcf.addOn.BinaryMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileMessageService implements MessageService{

    private final FileMessageRepository fileMessageRepository;
    private final FileChannelRepository fileChannelRepository;
    private final FileUserRepository fileUserRepository;
    private final BinaryMessageService binaryMessageService;

    @Override
    public void create(MessageDtoForCreate responseDto) {
        Channel writtenChannel = fileChannelRepository.findById(responseDto.getChannelId())
                .orElseThrow(() -> new NotFoundId("wrong channelId"));
        User writer = fileUserRepository.findById(responseDto.getUserId())
                .orElseThrow(() -> new NotFoundId("wrong userId"));

        // 첨부파일 미포함 메시지
        Message createdMessage = Message.createMessage(writtenChannel, writer, responseDto.getContent());

        // 첨부파일 적용
        List<byte[]> bmc = responseDto.getAttachments();
        if (!bmc.isEmpty()) {
            createdMessage.setAttachments(bmc);
            binaryMessageService.create(new BinaryMessageContentDto(createdMessage.getId(), bmc));
        }

        writtenChannel.updateLastMessageTime();
        fileChannelRepository.save(writtenChannel);

        fileMessageRepository.save(createdMessage);
    }

    @Override
    public void update(MessageDtoForUpdate updateDto) {
        Message updatingMessage = fileMessageRepository.findById(updateDto.getMessageId())
                .orElseThrow(() -> new NotFoundId("Fail to update : wrong messageId"));

        updatingMessage.setContent(updateDto.getChangeContent());
        // 메시지 binary data는 수정 불가
        // 수정해도 채널의 lastMessageTime은 불변?
        fileMessageRepository.save(updatingMessage);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return fileMessageRepository.findMessagesInChannel(channelId);
    }

    @Override
    public void delete(UUID messageId) {
        binaryMessageService.delete(messageId);
        if (fileMessageRepository.existsById(messageId)) fileMessageRepository.delete(messageId);
        else throw new NotFoundId("message 삭제 실패 : wrong id");
    }

    @Override
    public Message findById(UUID messageId) {
        return fileMessageRepository.findById(messageId).orElseThrow(NotFoundId::new);
    }

    /**
     * 메시지 디렉토리 생성
     */
    public void createDirectory() {
        fileMessageRepository.createDirectory();
    }
}