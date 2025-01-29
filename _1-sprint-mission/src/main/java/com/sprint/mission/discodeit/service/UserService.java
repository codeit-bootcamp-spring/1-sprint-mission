package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(String username,String email,String password,String phoneNumber,String address); //사용자 생성
    User find(UUID userId); //사용자 단일 조회
    List<User> findAll(); //사용자 모두 조회
    User update(UUID userId,String field,Object value);//사용자 수정
    void delete(UUID userId); //사용자 삭제
}
