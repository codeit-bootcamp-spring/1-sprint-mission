package com.sprint.mission.discodit.Service;

import com.sprint.mission.discodit.Entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요
    public UUID createUser(String userName);
    public User getUser(UUID id);
    public List<User> getUsers();
    public void setUser(UUID id, String userName);
    public void deleteUser(UUID id);

}
