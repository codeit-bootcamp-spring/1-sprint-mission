package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.AuthRequestDTO;
import com.sprint.mission.discodeit.dto.AuthResponseDTO;
import java.util.Optional;

public interface AuthService {
    Optional<AuthResponseDTO> login(AuthRequestDTO authRequestDTO);
    void registerUser(String username, String email, String password); // ✅ 이메일 추가
}
