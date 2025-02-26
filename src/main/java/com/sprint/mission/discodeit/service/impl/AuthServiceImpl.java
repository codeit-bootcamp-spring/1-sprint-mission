package com.sprint.mission.discodeit.service.impl;

import com.sprint.mission.discodeit.dto.LoginDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(LoginDTO loginDTO) {
        // username을 이용해 유저를 찾음
        Optional<User> optionalUser = userRepository.findByUsername(loginDTO.getUsername());

        // 유저가 존재하는지 확인
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 비밀번호 비교 (간단한 예시로, 실제로는 암호화된 비밀번호 비교가 필요함)
            if (user.getPassword().equals(loginDTO.getPassword())) {
                return user;  // 일치하는 유저 반환
            } else {
                throw new RuntimeException("Invalid password");  // 비밀번호 불일치 예외
            }
        } else {
            throw new RuntimeException("User not found");  // 유저가 없는 경우 예외
        }
    }
}
