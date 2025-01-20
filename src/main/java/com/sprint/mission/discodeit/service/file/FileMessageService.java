package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "messages.data";

    public FileMessageService() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            resetFile(); // 파일이 없을 때만 초기화
        }
    }
    public void resetFile() {
        File file = new File(FILE_PATH);
        if(file.exists() && file.delete()) {
            System.out.println("파일이 초기화되었습니다.");
        }
        saveAllMessage(new ArrayList<>()); // 초기화 후 빈 사용자 목록 저장

    }

    private void saveAllMessage(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 저장 중 오류 발생", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Message> readAllMessages() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Message>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>(); // 파일이 비어있을 경우 빈 리스트 반환
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 목록 읽기 중 오류 발생", e);
        }
    }

    @Override
    public void createMessage(Message message) {
        List<Message> messages = readAllMessages();
        if (messages.stream().anyMatch(u -> u.getId().equals(message.getId()))) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다: " + message.getId());
        }
        messages.add(message);
        saveAllMessage(messages);
        System.out.println("사용자가 생성되었습니다: " + message);
    }

    @Override
    public Message readMessage(String id) {
        List<Message> messages = readAllMessages();

        System.out.println("\n파일에서 읽은 메시지 uuid 목록:");
        messages.forEach(message -> System.out.println("User ID: " + message.getId()));

        UUID uuid = UUID.fromString(id);
        System.out.println("\n검색하려는 UUID: " + uuid);

        return messages.stream()
                .filter(m -> m.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public void updateMessage(Message message) {
        List<Message> messages = readAllMessages();
        boolean updated = false;

        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(message.getId())) {
                messages.set(i, message);
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다: " + message.getId());
        }

        saveAllMessage(messages);
        System.out.println("메시지가 업데이트되었습니다: " + message);
    }

    @Override
    public void deleteMessage(String id) {
        List<Message> messages = readAllMessages();

        if (!messages.removeIf(message -> message.getId().equals(UUID.fromString(id)))) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다: " + id);
        }

        saveAllMessage(messages);
        System.out.println("메시지가 삭제되었습니다: " + id);
    }
}
