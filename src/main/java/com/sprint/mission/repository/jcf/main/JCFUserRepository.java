package com.sprint.mission.repository.jcf.main;

import com.sprint.mission.aop.annotation.TraceAnnotation;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.exception.NotFoundId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
@TraceAnnotation
public class JCFUserRepository implements UserRepository {

    private final Map<UUID, User> data = new HashMap<>();

    @Override
    // 나중에 void 반환할 것 : 임시로 user반환
    public User save(User user) {
        data.put(user.getId(), user); // 바로 반환하면 null
        return user;
    }

    @Override
    public User findById(UUID userId) {
        User findUser = data.get(userId);
        if (findUser == null) throw new NotFoundId("ID를 찾지 못했습니다.");
        else return findUser;
        // 여기서 null 처리 안하고 Optional쓰면 다른 곳에서 코드가 지저분해짐
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(UUID userId) {
        User deletedUser = data.remove(userId);
        if (deletedUser != null) log.info("닉네임 {}는 사라집니다.", deletedUser.getName());
    }

    public boolean existsById(UUID id){
        return data.containsKey(id);
    }
}
