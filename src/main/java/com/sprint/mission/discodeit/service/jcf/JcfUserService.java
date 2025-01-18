package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JcfUserService implements UserService {

    private final Map<UUID,User> data=new HashMap<>();
    Set<UUID> keySet= data.keySet();
    @Override
    public void createUser(User user) {
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> userList(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> allUserList() {
         //보여주기만 하니까 원본을 return시켰습니다. 해당 내용을 복사해서 보여줘야하나요? 방어적 복사 사용
        List<User> lists=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            lists.add(data.get(uuid));
        }
        return lists;
    }

    @Override
    public void updateUser(UUID id,User user) {
        if(data.containsKey(id)) {
            data.put(id, user);
            /*User oldUser=data.get(id);
            oldUser.updateUserEmail(user.getUserEmail());
            oldUser.updateUserName(user.getUserName());*/
        }
        /*for (User dataList : data) {
            if(dataList.getId().equals(id)){
                dataList.updateUserEmail(user.getUserEmail());
                dataList.updateUserName(user.getUserName());
            }
        }*/
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
        /*for (int i=0; i < data.size(); i++) {
            if(data.get(i).getId().equals(id)){
                data.remove(i);
            }
        }*/
    }
}
