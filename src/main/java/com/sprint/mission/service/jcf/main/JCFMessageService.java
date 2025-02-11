package com.sprint.mission.service.jcf.main;

import com.sprint.mission.entity.addOn.BinaryMessageContent;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFChannelRepository;
import com.sprint.mission.repository.jcf.main.JCFMessageRepository;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.BinaryMessageContentDto;
import com.sprint.mission.service.dto.request.MessageDtoForCreate;
import com.sprint.mission.service.dto.request.MessageDtoForUpdate;
import com.sprint.mission.service.exception.NotFoundId;
import com.sprint.mission.service.jcf.addOn.BinaryMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JCFMessageService {

    private final JCFMessageRepository messageRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFUserRepository userRepository;
    private final BinaryMessageService binaryMessageService;


    public void create(MessageDtoForCreate responseDto) {
        // FindChannelDto byId = channelService.findById(dto.getChannelId());
        // 아직 컨트롤러가 없어서 Sevice 이용하면 DTO 반환...
        // [ ] 선택적으로 여러 개의 첨부파일을 같이 등록할 수 있습니다.
        // [ ] DTO를 활용해 파라미터를 그룹화합니다.
        Channel writtenChannel = channelRepository.findById(responseDto.getChannelId())
                .orElseThrow(() -> new NotFoundId("wrong channelId"));
        User writer = userRepository.findById(responseDto.getUserId())
                .orElseThrow(() -> new NotFoundId("wrong userId"));

        Message createdMessage = Message.createMessage(writtenChannel, writer, responseDto.getContent()); // 첨부파일 미포함 메시지

        // 첨부파일 적용
        List<BinaryMessageContent> bmc = responseDto.getBinaryMessageContent();
        if (!bmc.isEmpty()) {
            createdMessage.setBinaryContent(bmc);
            bmc.forEach((binaryMessageContent)
                    -> binaryMessageService.create(new BinaryMessageContentDto(binaryMessageContent)));
        }
        messageRepository.save(createdMessage);
    }

    public void update(MessageDtoForUpdate updateDto) {
        Message updatingMessage = messageRepository.findById(updateDto.getMessageId())
                .orElseThrow(() -> new NotFoundId("Fail to update : wrong messageId"));

        List<BinaryMessageContent> bmc = updateDto.getBinaryMessageContent();
        if (!bmc.isEmpty()) {
            // message에 첨부파일들 등록
            updatingMessage.setBinaryContent(bmc);
            // binaryMessage 저장소에 저장
            bmc.forEach((binaryMessageContent) -> {
                binaryMessageService.create(new BinaryMessageContentDto(binaryMessageContent));
            });
        }

        updatingMessage.setContent(updateDto.getChangeContent());

        messageRepository.save(updatingMessage);
    }

    //@Override
    public Optional<Message> findById(UUID messageId) {
        return messageRepository.findById(messageId);
    }

    //@Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return channelRepository.findById(channelId).map(messageRepository::findAllByChannel)
                .orElseGet(ArrayList::new);
    }

    //@Override
    public void delete(UUID messageId) {
        binaryMessageService.delete(messageId);
        messageRepository.delete(messageId);
    }
}
