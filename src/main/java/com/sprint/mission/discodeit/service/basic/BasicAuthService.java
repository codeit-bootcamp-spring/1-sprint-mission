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

@Service("basicAuthService") // 기본 인증 서비스로 설정
@RequiredArgsConstructor
public class BasicAuthService implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<AuthResponseDTO> login(AuthRequestDTO authRequestDTO) {
        return userRepository.findByUsername(authRequestDTO.getUsername())
                .filter(user -> passwordEncoder.matches(authRequestDTO.getPassword(), user.getPassword()))  // 비밀번호 비교
                .map(user -> new AuthResponseDTO(user.getId(), "로그인 성공"));
    }
}
