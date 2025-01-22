package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

import static com.sprint.mission.discodeit.util.MyLogger.log;

public class JcfUserService implements UserService {

    private final Map<UUID,User> data=new HashMap<>();
    @Override
    public void createUser(User user) {
        // user생성자에서 부모인 BaseEntity를 실행할때 null이 올 가능성이 있나요?
        if (user.getUserName().trim().isEmpty()) {
            log("이름을 입력해주세요.");
            return;
        }
        if (user.getUserEmail().trim().isEmpty()) {
            log("이메일을 입력해주세요.");
            return;
        }
        data.put(user.getId(), user);
    }

    @Override
    public Optional<User> findUser(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> findAllUsers() {
         //해당 내용을 복사해서 보여줘야하나요? 방어적 복사 사용
        List<User> lists=new ArrayList<>();
        for (UUID uuid : data.keySet()) {
            lists.add(data.get(uuid));
        }
        return lists;
    }

    @Override
    public void updateUserName(UUID id,String userName) {
        if(data.containsKey(id)) {
            data.get(id).updateUserName(userName);
        }
    }
    @Override
    public void updateUserEmail(UUID id,String userEmail) {
        if(data.containsKey(id)) {
            data.get(id).updateUserEmail(userEmail);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
