package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserResDTO;
import com.sprint.mission.discodeit.dto.UserUpdateDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    // 유일한 지 찾는거

    // 사용자 생성
    public Long createUserData(String username, String nickname, String email, String password, String regionCode, String phone, String imgPath);

    // 사용자 조회
    public UserResDTO getUser(Long id);

    public UserResDTO getUser(String userName);

    public List<UserResDTO> getAllUser();   // TODO: Paging 넣어 말아? <- 시간 상 가능하다면!

    public User findUserById(Long id);

    public Optional<Map.Entry<Long, User>> findUserByUserName(String userName);

    // 사용자 정보 업데이트
    // 비밀번호랑 Active 수정은 따로 빼기
    public boolean updateUser(Long id, UserUpdateDTO updateInfo);

    // 사용자 삭제
    public UserResDTO deleteUser(Long id);

    public UserResDTO deleteUser(String userName);
}
