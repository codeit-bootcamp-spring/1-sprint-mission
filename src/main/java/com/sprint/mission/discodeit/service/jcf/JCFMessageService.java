package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.HashMap;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    JCFUserService userService = JCFUserService.getInstance();
    JCFChannelService channelService = JCFChannelService.getInstance();

    //모든 메세지 객체를 담은 해쉬맵 싱글톤 객체 'MessageListsByChannel' 생성. LazyHolder 방식으로 스레드세이프 보장
    private static class JCFMessageServiceHolder {
        private static final JCFMessageService INSTANCE = new JCFMessageService();
        private static final HashMap<UUID, Message> MessageMap = new HashMap<UUID, Message>();
    }

    //외부에서 호출 가능한 싱글톤 인스턴스.
    public static JCFMessageService getInstance() {
        return JCFMessageService.JCFMessageServiceHolder.INSTANCE;
    }

    //모든 유저 관리 맵 'usersMap' 반환
    @Override
    public HashMap<UUID, Message> getMessageMap() {
        return JCFMessageServiceHolder.MessageMap;
    }

    //보내는 유저와 보내질 채널명, 보낼 내용 존재여부 검증 후 메세지 객체 생성. 성공하면 uuid, 실패하면 null 반환.
    @Override
    public UUID sendMessage(UUID fromUserId, UUID channelId, String content) {
        if (userService.isUserExist(fromUserId) == false || channelService.isChannelExist(channelId) == false || content == null) {
            return null;
        }
        Message newMessage = new Message(userService.getUser(fromUserId), channelService.getChannel(channelId), content);
        JCFMessageServiceHolder.MessageMap.put(newMessage.getId(), newMessage);
        return newMessage.getId();
    }

    //특정 메세지 객체 리턴. 없으면 null 반환.
    @Override
    public Message getMessage(UUID messageId){
        if (messageId == null){
            return null;
        }
        return JCFMessageServiceHolder.MessageMap.get(messageId);
    }

    //특정 메세지 내용 수정. 성공여부 리턴
    @Override
    public boolean reviseMessage(UUID messageId, String content){
        if (messageId == null || content == null) {
            return false;
        }
        JCFMessageServiceHolder.MessageMap.get(messageId).setContent(content);
        return true;
    }

    //특정 메세지 삭제. 성공여부 리턴
    @Override
    public boolean deleteMessage(UUID messageId){
        if (messageId == null) {
            return false;
        }
        JCFMessageServiceHolder.MessageMap.remove(messageId);
        return true;
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID messageId){
        return JCFMessageServiceHolder.MessageMap.containsKey(messageId);
    }

    @Override
    public String getMessageContent(UUID messageId){
        if (messageId == null) {
            return null;
        }
        return JCFMessageServiceHolder.MessageMap.get(messageId).getContent();
    }


}
