package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.login.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    //username,password과 일치하는 유저가 있는지 확인
    public LoginDto loginUer(LoginDto loginDto){
        User user = userRepository.findByUsername(loginDto.username()).orElseThrow(NoSuchElementException::new);
        if(user != null && user.getPassword().equals(loginDto.password())){
            return new LoginDto(loginDto.username(), loginDto.password());
        }else{
            throw new NoSuchElementException("Username or password is incorrect");
        }
    }
}
