package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Participant;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



public class FileUserRespository implements UserRepository{
    private static final String FILE_PATH = "temp/users-obj.dat";
    private final Map<UUID,User> data=new HashMap<>();

    @Override
    public User createUser(User user) {
        data.put(user.getId(),user);
        save();
        return user;
    }

    @Override
    public void updateUser(UUID id, UserUpdateDto userParam) {
        Map<UUID, User> lists = load();
        User findUser = lists.get(id);
        findUser.setUserName(userParam.getUserName());
        findUser.setUserEmail(userParam.getUserEmail());
        findUser.setLoginId(userParam.getLoginId());
        findUser.setPassword(userParam.getPassword());
        findUser.setUpdatedAt(Instant.now());
        save();
    }


    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
        save();
    }

    @Override
    public Optional<User> findById(UUID id) {
        Map<UUID, User> loadUsers = load();
        return Optional.ofNullable(loadUsers.get(id));
    }

    @Override
    public Optional<User> findByloginId(String loginId) {
        List<User> all=findAll();
        for (User user : all) {
            if(user.getLoginId().equals(loginId)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findUsersInChannel(Channel channel) {
        List<User> userLists=new ArrayList<>();
        List<Participant> participants = channel.getParticipants();
        for (Participant participant : participants) {
            userLists.add(participant.getUser());
        }
        return userLists;
    }

    @Override
    public List<User> findAll() {
        Map<UUID, User> loadUsers = load();
        return new ArrayList<>(loadUsers.values());
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

    public Map<UUID,User> load() {
        try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(FILE_PATH))){
            return (Map<UUID, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("파일을 찾을 수 없습니다."+e.getMessage());
            return null;
        } catch (IOException|ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

