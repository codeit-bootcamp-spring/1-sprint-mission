package com.sprint.mission.service;

import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.jcf.main.JCFUserRepository;
import com.sprint.mission.service.dto.request.LoginDtoForRequest;
import com.sprint.mission.service.dto.response.FindUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    //login
    //[ ] username, password과 일치하는 유저가 있는지 확인합니다.
    //[ ] 일치하는 유저가 있는 경우: 유저 정보 반환
    //[ ] 일치하는 유저가 없는 경우: 예외 발생
    //[ ] DTO를 활용해 파라미터를 그룹화합니다.

    //private final UserService userService; 나중에 ㄱ
    private final JCFUserRepository repository;

    public FindUserDto isExistUserByNamePassword(LoginDtoForRequest dto) {
        List<User> allUser = repository.findAll();
        return allUser.stream()
                .filter((user) -> Objects.equals(dto.getName(), user.getName()) && Objects.equals(dto.getPassword(), user.getPassword()))
                .findFirst()
                .map(user -> new FindUserDto(user, false))
                .orElseThrow(NoSuchElementException::new);
    }
}
