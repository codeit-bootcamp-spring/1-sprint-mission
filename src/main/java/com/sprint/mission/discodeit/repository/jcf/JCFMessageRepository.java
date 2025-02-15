package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    //todo UUID로 검색할일이 거의 없을것같아서 LinkedHashMap보다 그냥 ArrayList쓰는게 나을것같은데 시간이없어서 일단 수정 못함.. 나중에수정하기
    private static final LinkedHashMap<UUID, LinkedHashMap> allChannelmessageMaps = new LinkedHashMap<UUID, LinkedHashMap>();

    // 모든 채널별 해시맵이 담기는 해시맵 반환
    @Override
    public LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId) throws Exception {
        return allChannelmessageMaps.get(channelId);
    }

    // 특정 메세지 객체 반환
    @Override
    public Message getMessage(UUID channelId, UUID messageId) throws Exception {
        Message message = getChannelMessagesMap(channelId).get(messageId);
        if (message == null) {
            throw new NoSuchElementException("해당 메세지가 존재하지 않습니다.");
        }
        return message;
    }

    // 특정 메세지 객체 여부 확인 후 삭제.
    @Override
    public boolean deleteMessage(UUID channelId, UUID messageId) throws Exception {
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        if (messagesMap.containsKey(messageId) == false) {
            throw new NoSuchElementException("해당 메세지가 존재하지 않습니다.");
        }
        messagesMap.remove(messageId);
        return true;
    }

    //채널 메세지맵 삭제.
    @Override
    public boolean deleteChannelMessagesMap(UUID channelId){
        if (channelId == null) {
            return false;
        }
        if (allChannelmessageMaps.remove(channelId)==null){
            return false;
        }else{
            return true;
        }
    }

    // 전달받은 메세지 객체 null 여부 확인 후 메세지 해쉬맵에 추가.
    @Override
    public boolean saveMessage(UUID channelId, Message message) throws Exception {
        if (message == null || channelId == null) {
            throw new NullPointerException("파라미터로 전달된 channelId 혹은 message가 null인 상태입니다.");

        }
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        messagesMap.put(message.getId(), message);
        return true;
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID channelId, UUID messageId) throws Exception {
        if (messageId == null || channelId == null) {
            throw new NullPointerException("파라미터로 전달된 channelId 혹은 message가 null인 상태입니다.");

        }
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        if (messagesMap.containsKey(messageId) == false) {
            return false;
        }
        return true;
    }

    //채널 만들면 메세지앱도 같이 만들어져서 이 메서드 호출됨. 파라미터로 받은 메세지맵을 전체메세지맵에 추가
    @Override
    public boolean addChannelMessagesMap(UUID channelId, LinkedHashMap<UUID, Message> messagesMap) throws Exception {
        if (channelId == null || messagesMap == null) {
            throw new NullPointerException("파라미터로 전달된 channelId 혹은 messagesMap null인 상태입니다.");
        }
        allChannelmessageMaps.put(channelId, messagesMap);
        return false;

    }
    //가장 최근에 생성된 메세지 생성시간 반환. 메세지가 비어있으면 null 가능해서 옵셔널 반환.
    @Override
    public Optional<Instant> getLastMessageCreatedAt(UUID channelId){
        try {
            LinkedHashMap<UUID, Message> channelMap = getChannelMessagesMap(channelId);
            return Optional.ofNullable((Instant) channelMap.values().toArray()[channelMap.size() - 1]);
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
