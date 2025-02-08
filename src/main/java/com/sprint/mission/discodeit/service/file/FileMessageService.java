package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.*;

public class FileMessageService implements MessageService {


    private static final String FILE_PATH = "messageData.ser";
    private Map<UUID, Message> data;

    public FileMessageService() {
        this.data = loadDataFromFile();
    }

    // 데이터 파일 읽기
    @SuppressWarnings("unchecked")
    private Map<UUID, Message> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<UUID, Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 데이터 파일 쓰기
    private void saveDataToFile() {
        try (FileOutputStream fos = new FileOutputStream(FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //파일 삭제
    public void deleteFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("파일 삭제에 실패했습니다.");
            }
        }
    }

    // 파일을 빈 파일로 만드는 메서드
    public void clearFile() {
        File file = new File(FILE_PATH);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // 파일을 비우는 방법
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UUID save(Message message) {
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    public Message findOne(UUID id) {
        return data.get(id);
    }

    public List<Message> findAll() {
/*        if(data.isEmpty()){
            return Collections.emptyList(); // 빈 리스트 반환
        }*/
        return new ArrayList<>(data.values());
    }

    public UUID update(Message message){
        data.put(message.getId(), message);
        saveDataToFile();
        return message.getId();
    }

    public UUID delete(UUID id) {
        data.remove(id);
        saveDataToFile();
        return id;
    }


    @Override
    public UUID create(String content, UUID authorId, UUID channelId) {
        if(!Message.validation(content)){
            throw new IllegalArgumentException("잘못된 형식입니다.");
        }
        Message message = new Message(content, authorId, channelId);
        return save(message);
    }

    @Override
    public Message read(UUID id) {
        Message findMessage = findOne(id);
        return Optional.ofNullable(findMessage)
                .orElseThrow(() -> new NoSuchElementException("해당 메시지가 없습니다."));
    }

    @Override
    public List<Message> readAll() {
        return findAll();
    }

    @Override
    public Message updateMessage(UUID id, String message, User user) {
        Message findMessage = findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 변경 권한이 없습니다.");
        }
        findMessage.setMessage(message);
        update(findMessage);
        return findMessage;
    }

    @Override
    public UUID deleteMessage(UUID id, User user) {
        Message findMessage = findOne(id);
        if(!user.userCompare(findMessage.getAuthorId())){
            throw new IllegalStateException("message 삭제 권한이 없습니다.");
        }
        return delete(findMessage.getId());
    }
}
