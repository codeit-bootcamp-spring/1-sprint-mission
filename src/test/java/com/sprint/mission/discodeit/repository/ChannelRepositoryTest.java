package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private EntityManager em;

    @Test
    void existsById() {
        Channel channel1 = new Channel(ChannelType.PUBLIC, "테스트 채널", "테스트");
        Channel channel2 = new Channel(ChannelType.PRIVATE, null, null);

        channelRepository.save(channel1);
        channelRepository.save(channel2);
        em.flush();
        em.clear();

        boolean exists = channelRepository.existsById(channel1.getId());
        boolean exists2 = channelRepository.existsById(channel2.getId());
        assertTrue(exists);
        assertTrue(exists2);
    }

    @Test
    void 존재하지_않는_채널_채널_테스트() {
        UUID channelId = UUID.randomUUID();
        boolean exists = channelRepository.existsById(channelId);
        assertFalse(exists);
    }

    @Test
    void findAllByUserId() {
        User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));
        Channel channel1 = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "테스트"));
        Channel channel2 = channelRepository.save(new Channel(ChannelType.PRIVATE, "비공개 채널", null));
        ReadStatus readStatus = readStatusRepository.save(
                new ReadStatus(user, channel2, Instant.now()));

        em.flush();
        em.clear();

        List<Channel> result = channelRepository.findAllByUserId(user.getId());

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Channel::getName)
                .containsExactlyInAnyOrder("공개 채널", "비공개 채널");
    }

    @Test
    void findAllByUserId_조회결과없음() {
        User user = userRepository.save(new User("홍길동", "hong@naver.com", "1234", null));

        em.flush();
        em.clear();

        List<Channel> result = channelRepository.findAllByUserId(user.getId());

        assertThat(result).isEmpty();
    }
}