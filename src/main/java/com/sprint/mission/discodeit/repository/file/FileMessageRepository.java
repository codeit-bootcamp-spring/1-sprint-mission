package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.HashMap;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {

    FileIOHandler fileIOHandler = FileIOHandler.getInstance();
    String mainMessageRepository = "Message\\mainOIMessageRepository";



    // 외부에서 생성자 접근 불가
    private FileMessageRepository() {
        fileIOHandler.serializeHashMap(new HashMap<UUID, Message>(), mainMessageRepository);
    }
    // 레포지토리 객체 LazyHolder 싱글톤 구현.
    private static class FileMessageRepositoryHolder {
        private static final FileMessageRepository INSTANCE = new FileMessageRepository();
    }
    // 외부에서 호출 가능한 싱글톤 인스턴스.
    public static FileMessageRepository getInstance() {
        return FileMessageRepositoryHolder.INSTANCE;
    }




    // 모든 메세지 객체가 담기는 해쉬맵 반환
    @Override
    public HashMap<UUID, Message> getMessagesMap() {

        return (HashMap<UUID, Message>) fileIOHandler.deserializeHashMap(mainMessageRepository);
    }

    // 특정 메세지 객체 여부에 따라 객체 혹은 null 반환.
    @Override
    public Message getMessage(UUID messageId) {
        HashMap<UUID, Message> messagesMap = (HashMap<UUID, Message>) fileIOHandler.deserializeHashMap(mainMessageRepository);
        if (messagesMap.containsKey(messageId) == false) {
            return null;
        }
        return messagesMap.get(messageId);
    }

    // 특정 메세지 객체 여부 확인 후 삭제. 불값 반환
    @Override
    public boolean deleteMessage(UUID messageId) {
        HashMap<UUID, Message> messagesMap = (HashMap<UUID, Message>) fileIOHandler.deserializeHashMap(mainMessageRepository);
        if (messagesMap.containsKey(messageId) == false) {
            return false;
        }
        messagesMap.remove(messageId);
        fileIOHandler.serializeHashMap(messagesMap, mainMessageRepository);

        return true;
    }

    // 전달받은 메세지 객체 null 여부 확인 후 메세지 해쉬맵에 추가.
    @Override
    public boolean addMessage(Message message) {
        if (message == null) {
            return false;
        }
        HashMap<UUID, Message> messagesMap = (HashMap<UUID, Message>) fileIOHandler.deserializeHashMap(mainMessageRepository);
        messagesMap.put(message.getId(), message);
        fileIOHandler.serializeHashMap(messagesMap, mainMessageRepository);
        return true;
    }

    //메세지 존재여부 리턴
    @Override
    public boolean isMessageExist(UUID messageId) {
        HashMap<UUID, Message> messagesMap = (HashMap<UUID, Message>) fileIOHandler.deserializeHashMap(mainMessageRepository);
        if (messagesMap.containsKey(messageId) == false) {
            return false;
        }
        return true;
    }





}
