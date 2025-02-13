package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.AuthRequestDTO;
import com.sprint.mission.discodeit.dto.AuthResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasicAuthServiceTest {

    private BasicAuthService authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class); // ✅ Mock UserRepository 추가
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authService = new BasicAuthService(userRepository, passwordEncoder); // ✅ 수정됨
    }

    @Test
    void testLogin_Success() {
        String username = "testUser";
        String password = "password123";
        UUID userId = UUID.randomUUID();

        User mockUser = new User(userId, username, "test@example.com", null, passwordEncoder.encode(password));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        Optional<AuthResponseDTO> response = authService.login(new AuthRequestDTO(username, password));

        assertTrue(response.isPresent());
        assertEquals(username, response.get().getUsername());
    }

    @Test
    void testLogin_Failure() {
        String username = "nonExistingUser";
        String password = "wrongPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<AuthResponseDTO> response = authService.login(new AuthRequestDTO(username, password));

        assertFalse(response.isPresent());
    }
}
