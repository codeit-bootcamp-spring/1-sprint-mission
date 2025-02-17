package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
    //todo UUID로 검색할일이 거의 없을것같아서 LinkedHashMap보다 그냥 ArrayList쓰는게 나을것같은데 시간이없어서 일단 수정 못함.. 나중에수정하기
    private static final LinkedHashMap<UUID, LinkedHashMap> allChannelmessageMaps = new LinkedHashMap<UUID, LinkedHashMap>();

    // 모든 채널별 해시맵이 담기는 해시맵 반환
    @Override
    public LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId){
        return allChannelmessageMaps.get(channelId);
    }

    // 특정 메세지 객체 반환
    @Override
    public Message getMessage(UUID channelId, UUID messageId) {
        return getChannelMessagesMap(channelId).get(messageId);
    }

    // 메세지 객체 삭제.
    @Override
    public boolean deleteMessage(UUID channelId, UUID messageId) {
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        if (messagesMap.containsKey(messageId)==false){
            System.out.println("해당 채널이 존재하지 않아, 메세지 삭제가 실패하였습니다.");
            return false; }
        return messagesMap.remove(messageId) != null;
    }

    //채널별 메세지맵 삭제.
    @Override
    public boolean deleteChannelMessagesMap(UUID channelId) {
        return allChannelmessageMaps.remove(channelId) != null;
    }

    // 전달받은 메세지 객체 null 여부 확인 후 메세지 해쉬맵에 추가.
    @Override
    public boolean saveMessage(UUID channelId, Message message){
        if (message == null){
            System.out.println("전달된 message객체가 null인 상태로, 메세지 저장 실패.");
            return false;}
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        messagesMap.put(message.getId(), message);
        return true;
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID channelId, UUID messageId) {
        return getChannelMessagesMap(channelId).containsKey(messageId);
    }

    //채널 만들면 메세지앱도 같이 만들어져서 이 메서드 호출됨. 파라미터로 받은 메세지맵을 전체메세지맵에 추가
    @Override
    public boolean addChannelMessagesMap(UUID channelId, LinkedHashMap<UUID, Message> messagesMap) {
        allChannelmessageMaps.put(channelId, messagesMap);
        return true;
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
