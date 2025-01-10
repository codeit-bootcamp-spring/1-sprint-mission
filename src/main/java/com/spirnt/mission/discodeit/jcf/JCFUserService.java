package com.spirnt.mission.discodeit.jcf;

import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.service.UserService;

import java.beans.Expression;
import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }


    @Override
    public User create(User user) {
        data.put(user.getId(), user);
        System.out.println(user.getUsername() + "님의 사용자 등록이 완료되었습니다.");
        return user;
    }

    @Override
    public User read(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID id, User updatedUser) {
        if(data.containsKey(id)){
            User existingUser = data.get(id);
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            return existingUser;
        }
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        try{
            System.out.println(data.get(id).getUsername() +" 삭제가 완료되었습니다.");
            data.remove(id);
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 ID 입니다..\n" + e);
        }
        return false;
    }


}
