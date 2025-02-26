package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class FileUserStatusRepository implements UserStatusRepository {

    private static final String FILE_PATH = "temp/userStatus-obj.dat";
    private final Map<UUID,UserStatus> data=new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        data.put(userStatus.getId(), userStatus);
        save();
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        Map<UUID, UserStatus> loadStatus = load();
        return Optional.ofNullable(loadStatus.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Map<UUID, UserStatus> loadStatus = load();
        return Optional.ofNullable(loadStatus.get(userId));
    }

    @Override
    public List<UserStatus> findAll() {
        Map<UUID, UserStatus> loadStatus = load();
        return new ArrayList<>(loadStatus.values());
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, UserStatus> loadStatus = load();
        loadStatus.remove(id);
        data.remove(id);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> loadStatus = load();
        loadStatus.remove(userId);
        data.remove(userId);
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        }catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다." + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<UUID, UserStatus> load() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, UserStatus>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다."+e.getMessage());
            return null;
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
