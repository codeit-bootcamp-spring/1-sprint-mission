package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.*;

@Repository
public class FileMessageRepository implements MessageRepository {
    //todo UUID로 검색할일이 거의 없을것같아서 LinkedHashMap보다 그냥 ArrayList쓰는게 나을것같은데 시간이없어서 일단 수정 못함.. 나중에수정하기
    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainMessageRepository = "Message\\mainOIMessageRepository\\";


    // 채널의 모든 메세지 객체가 담기는 해쉬맵 반환
    @Override
    public LinkedHashMap<UUID, Message> getChannelMessagesMap(UUID channelId){
        LinkedHashMap<UUID, Message> channelMessagesMap = (LinkedHashMap<UUID, Message>) fileIOHandler.deserializeLinkedHashMap(mainMessageRepository + channelId.toString());
        return channelMessagesMap;
    }

    // 특정 메세지 객체 반환
    @Override
    public Message getMessage(UUID channelId, UUID messageId) {
        return getChannelMessagesMap(channelId).get(messageId);
    }

    // 메세지 객체 삭제
    @Override
    public boolean deleteMessage(UUID channelId, UUID messageId){
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        if (messagesMap.remove(messageId) == null) {
            return false;
        }
        return fileIOHandler.serializeLinkedHashMap(messagesMap, mainMessageRepository + channelId);
    }

    // 채널 메세지맵 삭제
    @Override
    public boolean deleteChannelMessagesMap(UUID channelId){
        return fileIOHandler.deleteFile(mainMessageRepository + channelId);
    }

    // 전달받은 메세지 객체 null 여부 확인 후 메세지 해쉬맵에 추가.
    @Override
    public boolean saveMessage(UUID channelId, Message message){
        if (message == null) {
            return false;
        }
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        messagesMap.put(message.getId(), message);
        return fileIOHandler.serializeLinkedHashMap(messagesMap, mainMessageRepository + channelId);
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID channelId, UUID messageId) {
        return getChannelMessagesMap(channelId).containsKey(messageId);
    }

    //채널 만들면 메세지앱도 같이 만들어져서 이 메서드 호출됨. 파라미터로 받은 메세지맵을 전체메세지맵에 추가.
    @Override
    public boolean addChannelMessagesMap(UUID channelId, LinkedHashMap<UUID, Message> messagesMap) {
        return fileIOHandler.serializeLinkedHashMap(messagesMap, mainMessageRepository + channelId);
    }

    //todo 이 메서드만 우선 IO예외 캐치해서 여기서 죽임.
    //가장 최근에 생성된 메세지 생성시간 반환. 메세지가 비어있으면 null 가능해서 옵셔널 반환.
    @Override
    public Optional<Instant> getLastMessageCreatedAt(UUID channelId) {
        LinkedHashMap<UUID, Message> messagesMap = getChannelMessagesMap(channelId);
        if (messagesMap.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(new ArrayList<>(messagesMap.values()).get(messagesMap.size() - 1).getCreatedAt());
    }
}


