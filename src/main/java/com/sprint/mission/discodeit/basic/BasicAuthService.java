package com.sprint.mission.discodeit.basic;

import com.sprint.mission.discodeit.dto.AuthDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    @Override
    public UserDTO login(AuthDTO authDTO) {
        log.info("[로그인 시도] 유저네임: " + authDTO.getUsername());

        User user = userRepository.findAll().stream()
                .filter(u -> u.getName().equals(authDTO.getUsername()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("[유저 검색 결과] 유저 찾음: " + user.getName());
        log.info("[비밀번호 비교] 입력된 비밀번호: " + authDTO.getPassword());
        log.info("[비밀번호 비교] 저장된 비밀번호: " + user.getPassword());

        if (!authDTO.getPassword().equals(user.getPassword())) {
            log.info("[로그인 실패] 비밀번호 불일치");
            throw new IllegalArgumentException("Invalid password");
        }

        log.info("[로그인 성공] 유저네임: " + user.getName());

        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}