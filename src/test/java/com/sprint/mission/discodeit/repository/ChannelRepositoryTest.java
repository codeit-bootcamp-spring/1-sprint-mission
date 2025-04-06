package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
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
class ChannelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("사용자 ID로 채널 목록 조회 성공")
    void findAllByUserIdSuccess() {
        // Given
        User user = new User("testuser", "test@email.com", "password123!", null);
        entityManager.persist(user);

        Channel publicChannel = new Channel("general", ChannelType.PUBLIC);
        Channel privateChannel = new Channel(ChannelType.PRIVATE);
        publicChannel.addUser(user);
        privateChannel.addUser(user);
        
        entityManager.persist(publicChannel);
        entityManager.persist(privateChannel);
        entityManager.flush();

        // When
        List<Channel> channels = channelRepository.findAllByUserId(user.getId());

        // Then
        assertThat(channels).hasSize(2);
        assertThat(channels).extracting("type")
            .containsExactlyInAnyOrder(ChannelType.PUBLIC, ChannelType.PRIVATE);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 채널 목록 조회")
    void findAllByUserIdEmpty() {
        // Given
        UUID nonExistentUserId = UUID.randomUUID();

        // When
        List<Channel> channels = channelRepository.findAllByUserId(nonExistentUserId);

        // Then
        assertThat(channels).isEmpty();
    }

    @Test
    @DisplayName("채널 타입으로 채널 목록 조회")
    void findAllByType() {
        // Given
        Channel publicChannel1 = new Channel("general", ChannelType.PUBLIC);
        Channel publicChannel2 = new Channel("announcements", ChannelType.PUBLIC);
        Channel privateChannel = new Channel(ChannelType.PRIVATE);

        entityManager.persist(publicChannel1);
        entityManager.persist(publicChannel2);
        entityManager.persist(privateChannel);
        entityManager.flush();

        // When
        List<Channel> publicChannels = channelRepository.findAllByType(ChannelType.PUBLIC);

        // Then
        assertThat(publicChannels).hasSize(2);
        assertThat(publicChannels).extracting("type")
            .containsOnly(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("페이징을 사용한 채널 목록 조회")
    void findAllWithPaging() {
        // Given
        for (int i = 1; i <= 20; i++) {
            Channel channel = new Channel("channel" + i, ChannelType.PUBLIC);
            entityManager.persist(channel);
        }
        entityManager.flush();

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name").ascending());

        // When
        Page<Channel> channelPage = channelRepository.findAll(pageRequest);

        // Then
        assertThat(channelPage.getContent()).hasSize(10);
        assertThat(channelPage.getTotalElements()).isEqualTo(20);
        assertThat(channelPage.getTotalPages()).isEqualTo(2);
    }
} 