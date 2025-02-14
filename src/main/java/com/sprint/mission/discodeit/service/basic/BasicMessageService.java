package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    //해당 메세지 객체 리턴
    @Override
    public Message findMessageById(UUID channelId, UUID messageId) {
        if (messageId == null){
            System.out.println("메세지 반환 실패. 입력값이 null인 상태입니다.");
            return null;
        }
        try{
            return messageRepository.getMessage(channelId, messageId);
        }catch (Exception e){
        e.printStackTrace();
        System.out.println(e.getMessage());
            System.out.println("메세지 반환 실패. ");
            return null;
        }
    }

    @Override
    public LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId) {
        if (channelId == null){
            System.out.println("메세지 반환 실패. 입력값이 null인 상태입니다.");
            return null;
        }
        try{
            return messageRepository.getChannelMessagesMap(channelId);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("채널별 메세지맵 반환 실패. ");
            return null;
        }
    }

    //메세지 생성. 'MessagesMap'에 uuid-메세지객체 주소 넣어줌.
    @Override
    public UUID createMessage(UUID authorId, UUID channelId, String content) {
        if (authorId == null || channelId == null || content == null) {
            System.out.println("메세지 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        try {
            if (userRepository.isUserExistenceByUUID(authorId) == false || channelRepository.isChannelExist(channelId) == false){
                System.out.println("메세지 생성 실패. 입력한 id를가진 유저나 채널이 존재하지 않습니다.");
            }
            Message newMessage = new Message(userRepository.getUserById(authorId), channelRepository.getChannel(channelId), content);
            messageRepository.saveMessage(channelId, newMessage);
            System.out.println("메세지 생성 성공!");
            return newMessage.getId();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("메세지 생성 실패");
            return null;
        }
    }

    //UUID를 통해 메세지 객체를 찾아 삭제
    @Override
    public boolean deleteMessage(UUID channelId, UUID messageId) {
        if (messageId == null || channelId == null ) {
            System.out.println("메세지 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        try {
            messageRepository.deleteMessage(channelId, messageId);
            System.out.println("메세지 삭제 성공!");
            return true;
        }catch (Exception e){
        e.printStackTrace();
        System.out.println(e.getMessage());
        return false;
        }
    }

    //메세지 내용 변경
    @Override
    public boolean reviseMessageContent(UUID channelId, UUID messageId, String newContent) {
        if (channelId == null|| messageId == null || newContent == null) {
            System.out.println("메세지 수정 실패. 입력값을 확인해주세요.");
            return false;
        }

        try {
            Message message = messageRepository.getMessage(channelId, messageId);
            message.setContent(newContent);
            messageRepository.saveMessage(channelId, message);
            System.out.println("메세지 수정 성공!");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("메세지 수정 실패");
            return false;
        }
    }

    //메세지 존재여부 확인
    @Override
    public boolean isMessageExist(UUID channelId, UUID messageId) {
        if (messageId == null || channelId == null ) {
            return false;
        }
        try{
            return messageRepository.isMessageExist(channelId, messageId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("메세지 존재여부 확인 실패");
            return false;
        }
    }


}
