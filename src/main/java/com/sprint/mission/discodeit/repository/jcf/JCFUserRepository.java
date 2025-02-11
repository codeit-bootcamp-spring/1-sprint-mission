package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.IdNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "repository.type", havingValue = "jcf")
public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data;

    public JCFUserRepository() { this.data = new HashMap<>(); }

    @Override
    public boolean save(User user) {
        data.put(user.getId(), user);
        System.out.println(user.getUsername() + "님의 사용자 등록이 완료되었습니다.");
        return true;
    }

    @Override
    public User findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User modify(UUID id, User modifiedUser){
        return data.replace(id, modifiedUser);
    }

    @Override
    public boolean deleteById(UUID id) {
        try{
            String removeUserName = data.get(id).getUsername();
            data.remove(id);
            System.out.println(removeUserName +" 삭제가 완료되었습니다.");
            return true;
        } catch (NullPointerException e){
            System.out.println("유효하지 않은 ID 입니다..\n" + e);
        }
        return false;
    }

}
