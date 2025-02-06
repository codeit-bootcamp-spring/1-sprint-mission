package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.UserLoginRequestDTO;
import com.sprint.mission.discodeit.dto.response.UserLoginResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.interfacepac.UserRepository;
import com.sprint.mission.discodeit.repository.interfacepac.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;

public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    public BasicAuthService(UserRepository userRepository, UserStatusRepository userStatusRepository) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
    }

    @Override
    public UserLoginResponseDTO login(UserLoginRequestDTO userLoginRequestDTO) {
        //사용자 확인
        User user = userRepository.findByEmail(userLoginRequestDTO.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email"));
        //비밀번호 확인
        if(!user.getPassword().equals(userLoginRequestDTO.password())){
            throw new IllegalArgumentException("Invalid email or password");
        }
        //온라인 상태 확인
        Boolean isOnline = userStatusRepository.findByUser(user)
                .map(UserStatus::isOnline)
                .orElse(false);
        return new UserLoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(),user.getUpdatedAt(), isOnline);
    }
}
