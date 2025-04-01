package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ChannelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("공개 채널 저장 후 조회 테스트")
    void saveAndFindPublicChannel() {
        // Given
        Channel channel = Channel.builder()
                .name("테스트 공개 채널")
                .description("테스트 설명")
                .type(ChannelType.PUBLIC)
                .build();

        // When
        Channel savedChannel = entityManager.persistAndFlush(channel);
        Optional<Channel> foundChannel = channelRepository.findById(savedChannel.getId());

        // Then
        assertTrue(foundChannel.isPresent());
        assertEquals(channel.getName(), foundChannel.get().getName());
        assertEquals(channel.getDescription(), foundChannel.get().getDescription());
        assertEquals(channel.getType(), foundChannel.get().getType());
    }

    @Test
    @DisplayName("비공개 채널 저장 후 조회 테스트")
    void saveAndFindPrivateChannel() {
        // Given
        Channel channel = Channel.builder()
                .name("테스트 비공개 채널")
                .description("테스트 설명")
                .type(ChannelType.PRIVATE)
                .build();

        // When
        Channel savedChannel = entityManager.persistAndFlush(channel);
        Optional<Channel> foundChannel = channelRepository.findById(savedChannel.getId());

        // Then
        assertTrue(foundChannel.isPresent());
        assertEquals(channel.getName(), foundChannel.get().getName());
        assertEquals(channel.getDescription(), foundChannel.get().getDescription());
        assertEquals(channel.getType(), foundChannel.get().getType());
    }

    @Test
    @DisplayName("모든 채널 찾기 테스트")
    void findAllChannels() {
        // Given
        Channel channel1 = Channel.builder()
                .name("테스트 채널 1")
                .description("테스트 설명 1")
                .type(ChannelType.PUBLIC)
                .build();
        
        Channel channel2 = Channel.builder()
                .name("테스트 채널 2")
                .description("테스트 설명 2")
                .type(ChannelType.PRIVATE)
                .build();

        entityManager.persist(channel1);
        entityManager.persist(channel2);
        entityManager.flush();

        // When
        List<Channel> channels = channelRepository.findAll();

        // Then
        assertFalse(channels.isEmpty());
        assertTrue(channels.size() >= 2);
        assertTrue(channels.stream().anyMatch(c -> c.getName().equals("테스트 채널 1")));
        assertTrue(channels.stream().anyMatch(c -> c.getName().equals("테스트 채널 2")));
    }

    @Test
    @DisplayName("채널 삭제 테스트")
    void deleteChannel() {
        // Given
        Channel channel = Channel.builder()
                .name("삭제할 채널")
                .description("삭제 테스트")
                .type(ChannelType.PUBLIC)
                .build();

        Channel savedChannel = entityManager.persistAndFlush(channel);

        // When
        channelRepository.deleteById(savedChannel.getId());
        entityManager.flush();
        
        Optional<Channel> foundChannel = channelRepository.findById(savedChannel.getId());

        // Then
        assertFalse(foundChannel.isPresent());
    }

    @Test
    @DisplayName("채널 타입으로 채널 찾기 테스트")
    void findChannelsByType() {
        // Given
        Channel publicChannel1 = Channel.builder()
                .name("공개 채널 1")
                .description("공개 채널 설명 1")
                .type(ChannelType.PUBLIC)
                .build();
        
        Channel publicChannel2 = Channel.builder()
                .name("공개 채널 2")
                .description("공개 채널 설명 2")
                .type(ChannelType.PUBLIC)
                .build();
        
        Channel privateChannel = Channel.builder()
                .name("비공개 채널")
                .description("비공개 채널 설명")
                .type(ChannelType.PRIVATE)
                .build();

        entityManager.persist(publicChannel1);
        entityManager.persist(publicChannel2);
        entityManager.persist(privateChannel);
        entityManager.flush();

        // When
        List<Channel> publicChannels = channelRepository.findByType(ChannelType.PUBLIC);
        List<Channel> privateChannels = channelRepository.findByType(ChannelType.PRIVATE);

        // Then
        assertTrue(publicChannels.size() >= 2);
        assertTrue(privateChannels.size() >= 1);
        assertTrue(publicChannels.stream().allMatch(c -> c.getType() == ChannelType.PUBLIC));
        assertTrue(privateChannels.stream().allMatch(c -> c.getType() == ChannelType.PRIVATE));
    }

    @Test
    @DisplayName("채널 이름으로 채널 찾기 테스트")
    void findChannelsByName() {
        // Given
        String channelName = "특별한 채널 이름";
        
        Channel channel = Channel.builder()
                .name(channelName)
                .description("특별한 설명")
                .type(ChannelType.PUBLIC)
                .build();

        entityManager.persist(channel);
        entityManager.flush();

        // When
        List<Channel> foundChannels = channelRepository.findByNameContaining(channelName);

        // Then
        assertFalse(foundChannels.isEmpty());
        assertTrue(foundChannels.stream().anyMatch(c -> c.getName().equals(channelName)));
    }
} 