package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.interfaces.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Service
public class BasicMessageService implements MessageService {

    private final FileMessageRepository messageRepository;
    private final BasicBinaryContentService binaryContentService;
    private final BasicReadStatusService statusService;

    @Autowired
    public BasicMessageService(FileMessageRepository messageRepository, BasicBinaryContentService binaryContentService, BasicReadStatusService statusService) {
        this.messageRepository = messageRepository;
        this.binaryContentService = binaryContentService;
        this.statusService = statusService;
    }

    @Override
    public void saveMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        messageRepository.createMessage(messageDto);
        if(messageDto.getHaveContent()) binaryContentService.createMessageContent(messageDto);
        statusService.saveMessage(messageDto);
    }

    @Override
    public void deleteMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        if(messageRepository.checkMessageContains(messageDto.getMessageId())){
            Message message = messageRepository.getMessage(messageDto);
            messageRepository.deleteMessage(messageDto.getMessageId());
            if(message.getIsContainContent()) binaryContentService.deleteMessageContent(messageDto);
            messageDto.setChannelId(message.getChannelId());
            statusService.deleteMessage(messageDto);
            System.out.println("메세지 삭제 성공!");
        }
        else System.out.println("메세지 삭제에 실패하였습니다");
    }

    @Override
    public void modifyMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        if(messageRepository.checkMessageContains(messageDto.getMessageId())){
            MessageDto dto = new MessageDto();
            dto.setText(messageDto.getNewContent());
            dto.setUserId(messageDto.getUserId());
            dto.setMessageId(messageDto.getMessageId());
            messageRepository.modifyMessage(dto);

            System.out.println("메세지 수정 성공!");
            return;
        }

        System.out.println("메세지 수정에 실패하였습니다");
    }

    @Override
    public void showInfoMessage(MessageDto messagedto) throws IOException, ClassNotFoundException {
        Message message = messageRepository.getMessage(messagedto);
        System.out.println(message.getText() + " (" + message.getUserName() + ") [" +
                (message.getUpdatedAt()) + "]" + " - " + message.getChannelName());
    }

    public void showInfoChannelMessage(MessageDto messageDto) throws IOException, ClassNotFoundException {
        System.out.println(messageRepository.getMessagesInChannel(messageDto));
    }

}