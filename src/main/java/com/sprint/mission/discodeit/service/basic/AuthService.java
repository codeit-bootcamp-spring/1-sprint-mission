package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserInfoDto;
import com.sprint.mission.discodeit.dto.user.UserLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.AuthenticationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserStatusService userStatusService;

    public UserInfoDto login(UserLoginDto dto) {
        User user = userRepository.findByName(dto.name());

        String password = generatePassword(dto.password());
        if (!user.getName().equals(dto.name()) || !user.getPassword().equals(password)) {
            throw new AuthenticationException("로그인 실패");
        }

        UserStatus userStatus = userStatusService.findById(user.getId());
        return UserInfoDto.of(user.getId(), user.getCreatedAt(), user.getUpdatedAt(), user.getName(), user.getEmail(), userStatus.isOnline());
    }

    public String generatePassword(String password) {
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes());

            for (byte b : digest) {
                builder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("비밀번호 암호화 오류");
        }
        return builder.toString();
    }
}
