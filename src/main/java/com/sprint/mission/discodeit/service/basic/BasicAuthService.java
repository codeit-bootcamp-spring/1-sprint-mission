package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthRequestDTO;
import com.sprint.mission.discodeit.dto.AuthResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service("basicAuthService")
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository; // ✅ UserRepository 주입
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<AuthResponseDTO> login(AuthRequestDTO authRequestDTO) {
        return userRepository.findByUsername(authRequestDTO.getUsername())
                .filter(user -> passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword()))
                .map(user -> new AuthResponseDTO(user.getId(), user.getUsername()));
    }

    @Override
    public void registerUser(String username, String email, String password) {
        // ✅ 중복 검사 추가
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        // ✅ User 생성 후 저장
        User user = new User(UUID.randomUUID(), username, email, null, passwordEncoder.encode(password));
        userRepository.save(user);
    }
}
