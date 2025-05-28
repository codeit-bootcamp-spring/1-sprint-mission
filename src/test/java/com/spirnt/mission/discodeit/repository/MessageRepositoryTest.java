package com.spirnt.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.entity.Message;
import com.spirnt.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findByChannelIdAndCreatedAtLessThan - 성공")
    void testFindByChannelIdAndCreatedAtLessThanSuccess() {
        // given
        // 테스트 데이터 저장
        Channel channel = new Channel("Ch 1", "ch1", ChannelType.PUBLIC);
        channel = channelRepository.save(channel);
        User user = new User("User1", "user@mail.com", "password");
        user = userRepository.save(user);
        Instant now = Instant.now();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("Content " + i, user, channel, null);
            messageRepository.save(msg);
        }

        em.flush();
        em.clear();

        // 5만큼 가져오기
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        Instant cursor = now.minusSeconds(100);

        // when
        Slice<Message> result = messageRepository.findByChannelIdAndCreatedAtLessThan(
            channel.getId(),
            cursor, pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().size() <= 5);
        for (Message message : result.getContent()) {
            assertTrue(message.getCreatedAt().isBefore(cursor));
            assertNotNull(message.getAuthor());
            assertNotNull(message.getChannel());
        }
    }
}
