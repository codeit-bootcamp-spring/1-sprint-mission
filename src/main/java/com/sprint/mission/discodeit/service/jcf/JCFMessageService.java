package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileIOHandler;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    JCFMessageRepository JCFMessageRepositoryInstance = JCFMessageRepository.getInstance();
    JCFUserRepository JCFUserRepositoryInstance = JCFUserRepository.getInstance();
    JCFChannelRepository JCFChannelRepositoryInstance = JCFChannelRepository.getInstance();
    FileIOHandler IOHandlerInstance = FileIOHandler.getInstance();
    //생성자 접근 불가능하도록 함.
    private JCFMessageService() {
    }
    //LazyHolder 싱글톤
    private static class JCFMessageServiceHolder {
        private static final JCFMessageService INSTANCE = new JCFMessageService();
    }
    //외부에서 접근 가능한 인스턴스
    public static JCFMessageService getInstance() { //외부에서 호출 가능한 싱글톤 인스턴스.
        return JCFMessageService.JCFMessageServiceHolder.INSTANCE;
    }





    //모든 메세지 관리 맵 'MessagesMap' 반환
    @Override
    public HashMap<UUID, Message> getMessagesMap() {
        return JCFMessageRepositoryInstance.getMessagesMap();
    }

    //해당 메세지 리턴
    @Override
    public Message getMessageById(UUID messageId) {
        if (messageId == null || JCFMessageRepositoryInstance.isMessageExist(messageId)==false){
            System.out.println("메세지 반환 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFMessageRepositoryInstance.getMessage(messageId);
    }

    //메세지 생성. 'MessagesMap'에 uuid-메세지객체 주소 넣어줌.
    @Override
    public UUID createMessage(UUID fromUserId, UUID channelId, String content) {
        if (fromUserId == null || channelId == null || content == null || JCFUserRepositoryInstance.isUserExist(fromUserId) == false || JCFChannelRepositoryInstance.isChannelExist(channelId) == false) {
            System.out.println("메세지 생성 실패. 입력값을 확인해주세요.");
            return null;
        }
        Message newMessage = new Message(JCFUserRepositoryInstance.getUser(fromUserId), JCFChannelRepositoryInstance.getChannel(channelId), content);
        JCFMessageRepositoryInstance.addMessage(newMessage);
        System.out.println("메세지 생성 성공!");
        return newMessage.getId();
    }

    //UUID를 통해 메세지 객체를 찾아 삭제.
    @Override
    public boolean deleteMessage(UUID MessageId) {
        if (MessageId == null || JCFMessageRepositoryInstance.isMessageExist(MessageId) == false) {
            System.out.println("메세지 삭제 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFMessageRepositoryInstance.deleteMessage(MessageId);
        System.out.println("메세지 삭제 성공!");
        return true;
    }

    //메세지 내용 변경.
    @Override
    public boolean reviseMessageContent(UUID MessageId, String newContent) {
        if (MessageId == null || newContent == null || JCFMessageRepositoryInstance.isMessageExist(MessageId) == false) {
            System.out.println("메세지 수정 실패. 입력값을 확인해주세요.");
            return false;
        }
        JCFMessageRepositoryInstance.getMessage(MessageId).setContent(newContent);
        System.out.println("메세지 수정 성공!");
        return true;
    }

    //메세지 존재여부 확인
    @Override
    public boolean isMessageExist(UUID messageId) {
        if (messageId == null) {
            return false;
        }
        return JCFMessageRepositoryInstance.isMessageExist(messageId);
    }

    //메세지 객체 상세정보 출력(보낸이/채널/내용/생성시간과 수정시간 중 최근시간)
    @Override
    public boolean printMessageDetails(UUID messageId) {
        if (messageId == null
                || JCFMessageRepositoryInstance.isMessageExist(messageId) == false
                || JCFMessageRepositoryInstance.getMessage(messageId) == null
                || JCFMessageRepositoryInstance.getMessage(messageId).getFromUser() == null
                || JCFMessageRepositoryInstance.getMessage(messageId).getChannel() == null)
        {
            System.out.println("메세지 내용 및 상세정보 확인 실패. 입력값을 확인해주세요.");
            return false;
        }



        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS");
        Date date = new Date();
        if (JCFMessageRepositoryInstance.getMessage(messageId).getUpdatedAt() != 0){
            date.setTime(JCFMessageRepositoryInstance.getMessage(messageId).getUpdatedAt());
        } else {
            date.setTime(JCFMessageRepositoryInstance.getMessage(messageId).getCreatedAt());
        }

        String userName = JCFMessageRepositoryInstance.getMessage(messageId).getFromUser().getUserName();
        String channelName = JCFMessageRepositoryInstance.getMessage(messageId).getChannel().getChannelName();
        String messageContent = JCFMessageRepositoryInstance.getMessage(messageId).getContent();
        String messageLatestEditTime = simpleDateFormat.format(date);

        System.out.println("[보낸이] " + userName);
        System.out.println("[채널] " + channelName);
        System.out.println("[내용] " + messageContent);
        System.out.println("[최근 수정시간] " + messageLatestEditTime);
        return true;
    }


    //메세지의 내용 리턴
    @Override
    public String getMessageContent(UUID messageId) {
        if (messageId == null) {
            System.out.println("메세지 내용 확인 실패. 입력값을 확인해주세요.");
            return null;
        }
        return JCFMessageRepositoryInstance.getMessage(messageId).getContent();
    }

    //현재 메세지맵에 있는 객체정보 직렬화
    public boolean exportMessagesMap(String fileName) {
        if (fileName==null){
            System.out.println("메세지 직렬화 실패. 입력값을 확인해주세요.");
            return false;
        }
        IOHandlerInstance.serializeHashMap(JCFMessageRepositoryInstance.getMessagesMap(), fileName);
        System.out.println("메세지 직렬화 성공!");
        return true;
    }

}
