package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    //해당 메세지 객체 리턴
    @Override
    public Message getMessageById(UUID messageId) {
        if (messageId == null || messageRepository.isMessageExist(messageId)==false){
            System.out.println("메세지 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return messageRepository.getMessage(messageId);
    }

    //메세지 생성. 'MessagesMap'에 uuid-메세지객체 주소 넣어줌.
    @Override
    public UUID createMessage(UUID author, UUID channel, String content) {
        if (author == null || channel == null || content == null || userRepository.isUserExistByUUID(author) == false || channelRepository.isChannelExist(channel)==false) {
            System.out.println("메세지 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Message newMessage = new Message(userRepository.getUserById(author), channelRepository.getChannel(channel), content);
        messageRepository.addMessage(newMessage);
        System.out.println("메세지 생성 성공!");
        return newMessage.getId();
    }

    //UUID를 통해 메세지 객체를 찾아 삭제
    @Override
    public boolean deleteMessage(UUID MessageId) {
        if (MessageId == null || messageRepository.isMessageExist(MessageId) == false) {
            System.out.println("메세지 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        messageRepository.deleteMessage(MessageId);
        System.out.println("메세지 삭제 성공!");
        return true;
    }

    //메세지 내용 변경
    @Override
    public boolean reviseMessageContent(UUID MessageId, String newContent) {
        if (MessageId == null || newContent == null || messageRepository.isMessageExist(MessageId) == false) {
            System.out.println("메세지 수정 실패. 입력값을 확인해주세요.");
            return false;
        }
        Message message = messageRepository.getMessage(MessageId);
        message.setContent(newContent);
        messageRepository.addMessage(message);

        System.out.println("메세지 수정 성공!");
        return true;
    }

    //메세지 존재여부 확인
    @Override
    public boolean isMessageExist(UUID messageId) {
        if (messageId == null) {
            return false;
        }
        return messageRepository.isMessageExist(messageId);
    }

    //메세지 객체 상세정보 출력(보낸이/채널/내용/생성시간과 수정시간 중 최근시간)
    @Override
    public boolean printMessageDetails(UUID messageId) {
        if (messageId == null
                || messageRepository.isMessageExist(messageId) == false
                || messageRepository.getMessage(messageId) == null
                || messageRepository.getMessage(messageId).getAuthor() == null
                || messageRepository.getMessage(messageId).getChannel() == null)
        {
            System.out.println("메세지 내용 및 상세정보 확인 실패. 입력값을 확인해주세요.");
            return false;
        }



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        Date date = new Date();

        date.setTime(messageRepository.getMessage(messageId).getUpdatedAt().toEpochMilli());

        String authorName = messageRepository.getMessage(messageId).getAuthor().getUserName();
        String channelName = messageRepository.getMessage(messageId).getChannel().getChannelName();
        String messageContent = messageRepository.getMessage(messageId).getContent();
        String messageLatestEditedTime = simpleDateFormat.format(date);

        System.out.println("[보낸이] " + authorName);
        System.out.println("[채널] " + channelName);
        System.out.println("[내용] " + messageContent);
        System.out.println("[최근 수정시간] " + messageLatestEditedTime);
        return true;
    }

    @Override
    public String getMessageContent(UUID messageId) {
        if (messageId == null) {
            System.out.println("메세지 내용 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return messageRepository.getMessage(messageId).getContent();
    }


}
