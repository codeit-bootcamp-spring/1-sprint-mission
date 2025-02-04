package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    /**
     [ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
     JCF 이용하지 않고, 각 도메인 모델 이름(users, chnnels, messages)을 딴
     폴더에 ser파일을 개별 객체로 저장합니다.
     **/

    // FileUserRepository 는 FileIO save/load 분리를 안했는데, 여기서는 하겠습니다!!

    // 폴더 주소
    private final String MESSAGES_PATH = Paths.get("messages").toString();

    public FileMessageRepository(){
        // 초기화 시 폴더의 존재 유무 확인
        File dir = new File(MESSAGES_PATH);
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public boolean saveFile(Path filePath, Message message){
        try(
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ) {
            oos.writeObject(message);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public Message loadFile(Path filePath){
        if(Files.exists(filePath)){
            try(FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                Message message = (Message) ois.readObject();
                return message;
            }catch (IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }


    @Override
    public void saveMessage(Message message) {
        saveFile(Path.of(MESSAGES_PATH + message.getId().toString() + ".ser"), message);
    }

    @Override
    public Optional<Message> findMessageById(UUID id) {
        return Optional.ofNullable(loadFile(Path.of(MESSAGES_PATH + id.toString() + ".ser")));
    }

    @Override
    public Collection<Message> findAllMessages() {
        //Map<UUID, Message> messageMap = new HashMap<>();
        List<Message> MessageList = new ArrayList<>();
        File messageDir = new File("messages");
        if(messageDir.exists() && messageDir.isDirectory()){
            File[] files = messageDir.listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile() && file.getName().endsWith(".ser")){
                        UUID id = UUID.fromString(file.getName().replace(".ser", ""));
                        //messageMap.put(id, findMessageById(id).orElse(null));
                        MessageList.add(findMessageById(id).orElse(null));
                    }
                }
            }
        }
        return MessageList.isEmpty() ? null : MessageList;
    }

    @Override
    public void deleteMessageById(UUID id) {
        String fileName =  MESSAGES_PATH + id.toString() + ".ser";
        File messageFile = new File(fileName);
        if(messageFile.delete()){
            System.out.println("deleteMessageById 삭제 완료");
        }
    }

    @Override
    public void deleteAllMessages() {
        File file = new File(MESSAGES_PATH );
        File[] fileList = file.listFiles();
        for(File fileName : fileList){
            fileName.delete();
        }
        System.out.println("deleteAllMessages 삭제 완료");

    }
}
