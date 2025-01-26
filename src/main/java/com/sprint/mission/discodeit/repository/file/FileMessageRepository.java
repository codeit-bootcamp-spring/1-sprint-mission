package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "tmp/message.ser";
    private Map<UUID, Message> messageMap;
    public FileMessageRepository(){
        this.messageMap = loadFromFile();
    }
    private Map<UUID,Message> loadFromFile(){
        messageMap = new HashMap<>();
        File chatFile = new File(FILE_PATH);

       if(!chatFile.exists()) {
           System.out.println("파일이 존재하지 않아 새로 생성합니다.");
           return new HashMap<>();
       }

       if (chatFile.length() == 0) {
           System.out.println("파일이 비어 있으므로 빈 맵을 반환합니다.");
           return new HashMap<>();
       }

       try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))){
           return (Map<UUID, Message>) ois.readObject();
       }catch (IOException | ClassNotFoundException e) {
           System.out.println("데이터 로드 중 오류 발생 : " + e.getMessage());
           return new HashMap<>();
       }
    }
    //정보 파일에 저장 메소드
    private void saveToFile() {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
            oos.writeObject(messageMap);
        } catch (IOException e) {
            System.out.println("저장 실패! " + e.getMessage());
        }
    }

    @Override
    public Message createMessage(UUID channelId, UUID authorId, String content) {
       Message message = new Message(content,channelId,authorId);
       messageMap.put(message.getId(), message);
       saveToFile();
        System.out.println("메시지가 작성되었습니다");
        return message;
    }

    @Override
    public List<Message> getAllMessageList() {
        return new ArrayList<>(messageMap.values());
    }

    @Override
    public Message searchById(UUID messageId) {
        Message message = messageMap.get(messageId);
        if(message == null) {
            System.out.println("해당 메세지를 검색할 수 없습니다 : " + messageMap);
        }
        return message;
    }


    @Override
    public void updateMessage(UUID messageId, String content) {
        Message message = messageMap.get(messageId);
        if(message == null) {
            System.out.println("수정할 메세지가 존재하지 않습니다 : " + messageMap);
            return;
        }
        message.setContent(content);
        saveToFile();
        System.out.println("메세지가 성공적으로 수정되었습니다 : " + message.getContent());
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Message removeMessage = messageMap.remove(messageId);
        if(removeMessage == null) {
            System.out.println("해당 메세지를 찾을 수 없습니다 : " + messageMap);
        } else  {
            saveToFile();
            System.out.println("메세지가 삭제되었습니다 : "+ messageMap);
        }

    }

}
