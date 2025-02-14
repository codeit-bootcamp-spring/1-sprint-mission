package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BasicAuthServiceTest {
  
  @Test
  void login() {
    // given
    UserRepository userRepository = mock(JCFUserRepository.class);
    UserStatusRepository userStatusRepository = mock(JCFUserStatusRepository.class);
    Encryptor encryptor = mock(Encryptor.class);
    
    BasicAuthService authService = new BasicAuthService(userRepository, userStatusRepository, encryptor);
    String salt = encryptor.getSalt();
    String encryptedPassword = encryptor.encryptPassword("testPassword", salt);
    
    // Mock 설정
    when(encryptor.getSalt()).thenReturn("randomSalt");
    when(encryptor.encryptPassword("testPassword", "randomSalt")).thenReturn("encryptedPassword");
    
    
    User user = new User("encryptedPassword", "randomSalt", "testUser", "testEmail@gmail.com", null);
    UserStatus userStatus = new UserStatus(user.getId());
    
    when(userRepository.findByName("testUser")).thenReturn(Optional.of(user));
    when(userStatusRepository.findByUserId(user.getId())).thenReturn(Optional.of(userStatus));
    
    // when
    FindUserDto result = authService.login("testUser", "testPassword");
    
    // then
    assertNotNull(result);
    assertEquals("testUser", result.name());
    assertEquals("testEmail@gmail.com", result.email());
    assertNull(result.profileImage());
    assertEquals(userStatus, result.status());
  }
}