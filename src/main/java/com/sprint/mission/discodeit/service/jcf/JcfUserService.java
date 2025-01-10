package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JcfUserService implements UserService {

    List<User> data=new ArrayList<>();//Map이 더 성능이 좋을까요?
    @Override
    public void createUser(User user) {
        data.add(user);
    }

    @Override
    public Optional<User> userList(UUID id) {
        for (User dataList : data) {
            if(dataList.getId().equals(id)){
                return Optional.of(dataList);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> allUserList() {
         return data; //보여주기만 하니까 원본을 return시켰습니다. 해당 내용을 복사해서 보여줘야하나요?
    }

    @Override
    public void updateUser(UUID id,User user) {
        for (User dataList : data) {
            if(dataList.getId().equals(id)){
              /*  if (dataList.updateUserName(user.getUserName())) {
                    dataList.getUserName()=user.getUserName();
                }*/
                dataList.updateUserEmail(user.getUserEmail());
                dataList.updateUserName(user.getUserName());
            }
        }
    }

    @Override
    public void deleteUser(UUID id) {
        for (User dataList : data) {
            if (dataList.getId().equals(id)) {
                data.remove(dataList);
            }
        }
    }
}
