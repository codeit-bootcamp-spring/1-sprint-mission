package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.user.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.user.UserLoginResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import com.sprint.mission.discodeit.service.interfacepac.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;


    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        //사용자 확인
        User user = userRepository.findByEmail(userLoginRequestDTO.email())
                .orElseThrow(() -> new NoSuchElementException("Invalid email"));
        //비밀번호 확인
        if(!user.getPassword().equals(userLoginRequestDTO.password())){
            throw new IllegalArgumentException("Wrong password");
        }
        //사용자 상태 확인 및 업데이트
        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElse(new UserStatus(user, Instant.now()));
        //로그인 시 마지막 활동 시간 업데이트
        userStatus.updateLastSeenAt(Instant.now());
        userStatusRepository.save(userStatus);

        //온라인 상태 확인
        Boolean isOnline = userStatus.isOnline();

        return new UserLoginResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                isOnline
        );
    }
}
