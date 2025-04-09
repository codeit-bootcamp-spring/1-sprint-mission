package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MessageRepository messageRepository;

    private User user;
    private Channel channel;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "test@email.com", "password123!", null);
        channel = new Channel("general", ChannelType.PUBLIC);
        entityManager.persist(user);
        entityManager.persist(channel);
        entityManager.flush();
    }

    @Test
    @DisplayName("채널 ID로 메시지 목록 조회 성공")
    void findAllByChannelIdSuccess() {
        // Given
        Message message1 = new Message(user, channel, "First message");
        Message message2 = new Message(user, channel, "Second message");
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        // When
        List<Message> messages = messageRepository.findAllByChannelId(channel.getId());

        // Then
        assertThat(messages).hasSize(2);
        assertThat(messages).extracting("content")
            .containsExactlyInAnyOrder("First message", "Second message");
    }

    @Test
    @DisplayName("존재하지 않는 채널 ID로 메시지 목록 조회")
    void findAllByChannelIdEmpty() {
        // Given
        UUID nonExistentChannelId = UUID.randomUUID();

        // When
        List<Message> messages = messageRepository.findAllByChannelId(nonExistentChannelId);

        // Then
        assertThat(messages).isEmpty();
    }

    @Test
    @DisplayName("사용자 ID로 메시지 목록 조회")
    void findAllByUserId() {
        // Given
        Message message1 = new Message(user, channel, "First message");
        Message message2 = new Message(user, channel, "Second message");
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        // When
        List<Message> messages = messageRepository.findAllByUserId(user.getId());

        // Then
        assertThat(messages).hasSize(2);
        assertThat(messages).extracting("content")
            .containsExactlyInAnyOrder("First message", "Second message");
    }

    @Test
    @DisplayName("페이징을 사용한 메시지 목록 조회")
    void findAllWithPaging() {
        // Given
        for (int i = 1; i <= 20; i++) {
            Message message = new Message(user, channel, "Message " + i);
            entityManager.persist(message);
        }
        entityManager.flush();

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        // When
        Page<Message> messagePage = messageRepository.findAll(pageRequest);

        // Then
        assertThat(messagePage.getContent()).hasSize(10);
        assertThat(messagePage.getTotalElements()).isEqualTo(20);
        assertThat(messagePage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("채널 ID로 메시지 삭제")
    void deleteAllByChannelId() {
        // Given
        Message message1 = new Message(user, channel, "First message");
        Message message2 = new Message(user, channel, "Second message");
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.flush();

        // When
        messageRepository.deleteAllByChannelId(channel.getId());
        entityManager.flush();
        entityManager.clear();

        // Then
        List<Message> remainingMessages = messageRepository.findAllByChannelId(channel.getId());
        assertThat(remainingMessages).isEmpty();
    }
} 