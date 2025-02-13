package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.dto.user.UserLoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User login(UserLoginRequest userLoginRequest){
        return userRepository.getAllUsers().stream()
                .filter(user -> user.getUsername().equals(userLoginRequest.username())
                        && user.getPassword().equals(userLoginRequest.password()))
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException("알 수 없는 사용자입니다."));
    }
}
