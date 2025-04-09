package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MessageRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private MessageRepository messageRepository;
  
  private User testUser;
  private Channel testChannel;
  private Message testMessage;
  
  @BeforeEach
  void setUp() {
    // 테스트 데이터 생성
    testUser = User.builder()
        .name("testUser")
        .email("test@example.com")
        .password("password")
        .build();
    entityManager.persist(testUser);

    testChannel = Channel.builder()
        .name("testChannel")
        .description("test description")
        .type(ChannelType.PUBLIC)
        .build();
    entityManager.persist(testChannel);
    
    testMessage = Message.builder()
        .content("테스트 메시지")
        .author(testUser)
        .channel(testChannel)
        .createdAt(Instant.now())
        .build();
    
    entityManager.flush();
  }

  @Test
  @DisplayName("특정 채널의 메시지 조회 테스트")
  void findAllByChannelId() {
    // Given
    Message message1 = Message.builder()
        .content("첫 번째 메시지")
        .author(testUser)
        .channel(testChannel)
        .createdAt(Instant.now().minus(1, ChronoUnit.DAYS))
        .build();
    entityManager.persist(message1);

    Message message2 = Message.builder()
        .content("두 번째 메시지")
        .author(testUser)
        .channel(testChannel)
        .createdAt(Instant.now())
        .build();
    entityManager.persist(message2);

    entityManager.flush();

    // When
    List<Message> messages = messageRepository.findAllByChannelId(testChannel.getId());

    // Then
    assertThat(messages).hasSize(2);

    List<Message> sortedMessages = messages.stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .collect(Collectors.toList());

    assertThat(sortedMessages.get(0).getContent()).isEqualTo("두 번째 메시지");
    assertThat(sortedMessages.get(1).getContent()).isEqualTo("첫 번째 메시지");
  }
  
  @Test
  @DisplayName("메시지 저장 테스트")
  void save_Success() {
    // When
    Message savedMessage = messageRepository.save(testMessage);
    entityManager.flush();
    entityManager.clear();
    
    // Then
    Message foundMessage = entityManager.find(Message.class, savedMessage.getId());
    assertThat(foundMessage).isNotNull();
    assertThat(foundMessage.getContent()).isEqualTo("테스트 메시지");
    assertThat(foundMessage.getAuthor().getId()).isEqualTo(testUser.getId());
    assertThat(foundMessage.getChannel().getId()).isEqualTo(testChannel.getId());
  }
  
  @Test
  @DisplayName("메시지 ID로 조회 테스트")
  void findById_Success() {
    // Given
    Message savedMessage = messageRepository.save(testMessage);
    entityManager.flush();
    entityManager.clear();
    
    // When
    Optional<Message> foundMessage = messageRepository.findById(savedMessage.getId());
    
    // Then
    assertThat(foundMessage).isPresent();
    assertThat(foundMessage.get().getContent()).isEqualTo("테스트 메시지");
    assertThat(foundMessage.get().getAuthor().getId()).isEqualTo(testUser.getId());
  }
  
  @Test
  @DisplayName("메시지 삭제 테스트")
  void deleteById_Success() {
    // Given
    Message savedMessage = messageRepository.save(testMessage);
    entityManager.flush();
    entityManager.clear();
    
    // When
    messageRepository.deleteById(savedMessage.getId());
    entityManager.flush();
    entityManager.clear();
    
    // Then
    Message deletedMessage = entityManager.find(Message.class, savedMessage.getId());
    assertThat(deletedMessage).isNull();
  }
  
  @Test
  @DisplayName("메시지 내용 업데이트 테스트")
  void update_Success() {
    // Given
    Message savedMessage = messageRepository.save(testMessage);
    entityManager.flush();
    
    // When
    savedMessage.setContent("수정된 메시지");
    messageRepository.save(savedMessage);
    entityManager.flush();
    entityManager.clear();
    
    // Then
    Message updatedMessage = entityManager.find(Message.class, savedMessage.getId());
    assertThat(updatedMessage).isNotNull();
    assertThat(updatedMessage.getContent()).isEqualTo("수정된 메시지");
  }
  
  @Test
  @DisplayName("모든 메시지 조회 테스트")
  void findAll_Success() {
    // Given
    Message message1 = Message.builder()
        .content("메시지 1")
        .author(testUser)
        .channel(testChannel)
        .createdAt(Instant.now())
        .build();
    entityManager.persist(message1);
    
    Message message2 = Message.builder()
        .content("메시지 2")
        .author(testUser)
        .channel(testChannel)
        .createdAt(Instant.now())
        .build();
    entityManager.persist(message2);
    
    entityManager.flush();
    
    // When
    List<Message> messages = messageRepository.findAll();
    
    // Then
    assertThat(messages).isNotEmpty();
    assertThat(messages.size()).isGreaterThanOrEqualTo(2);
  }
}
