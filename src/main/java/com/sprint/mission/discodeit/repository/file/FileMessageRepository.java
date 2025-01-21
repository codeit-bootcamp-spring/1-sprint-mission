package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

/**
 * 파일 기반의 MessageRepository 구현체. 데이터를 파일에 저장합니다.
 */
public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "messages.ser";

    @Override
    public void saveAll(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(messages); // 직렬화하여 파일에 저장
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 저장 중 오류 발생", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Message> loadAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<Message>) ois.readObject(); // 역직렬화하여 리스트 반환
        } catch (EOFException e) {
            return new ArrayList<>(); // 파일이 비어있으면 빈 리스트 반환
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 목록 읽기 중 오류 발생", e);
        }
    }

    @Override
    public void reset() {
        File file = new File(FILE_PATH);
        if (file.exists() && file.delete()) {
            System.out.println("메시지 파일이 초기화되었습니다.");
        }
        saveAll(new ArrayList<>()); // 빈 리스트로 초기화
    }
}
