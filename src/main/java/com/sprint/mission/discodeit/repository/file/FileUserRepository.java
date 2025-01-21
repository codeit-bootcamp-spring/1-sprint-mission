package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    @Override
    public void saveAll(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("사용자 목록 저장 중 오류 발생", e);
        }
    }

    @Override
    public List<User> loadAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("사용자 목록 읽기 중 오류 발생", e);
        }
    }

    @Override
    public void reset() {
        File file = new File(FILE_PATH);
        if (file.exists() && file.delete()) {
            System.out.println("파일이 초기화되었습니다.");
        }
        saveAll(new ArrayList<>()); // 빈 리스트로 초기화
    }
}
