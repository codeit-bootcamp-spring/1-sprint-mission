package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    //데이터 저장
    private final File filePath;
    private final Map<UUID,User> userData;

    public FileUserRepository(File filePath) {
        this.filePath = filePath;
        this.userData = loadUserData();
    }
    //데이터 로드
    private Map<UUID,User> loadUserData(){
        if (!filePath.exists()) return new HashMap<>(); // 파일이 없으면 빈 Map 반환
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
           return (Map<UUID,User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    //데이터 저장
    private void saveUserData(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(userData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //생성,업데이트
    public User create(User user){
        userData.put(user.getId(),user);
        saveUserData();
        return user;
    }
    //단일조회
    public User findById(UUID userId){
        return userData.get(userId);
    }
    //다중조회
    public List<User> findAll(){
        return new ArrayList<>(userData.values());
    }
    //삭제
    public void delete(UUID userId){
        if(!userData.containsKey(userId)){
            throw new NoSuchElementException("UserId not found");
        }
        userData.remove(userId);
        saveUserData();
    }
}
