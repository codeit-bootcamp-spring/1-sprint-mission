package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.TestAuditConfig;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestAuditConfig.class)
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private TestEntityManager em;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User("user1", "nickname1", "a@test.com", "pass1",
                new Phone("010-1111-1111", Phone.RegionCode.KR),
                User.UserType.COMMON, "", null);

        user2 = new User("user2", "nickname2", "b@test.com", "pass2",
                new Phone("010-2222-2222", Phone.RegionCode.KR),
                User.UserType.COMMON, "", null);

        em.persist(user1);
        em.persist(user2);

        Channel channel1 = new Channel("channel1", Channel.ChannelType.PUBLIC, "desc1", user1);
        Channel channel2 = new Channel("channel2", Channel.ChannelType.PRIVATE, "desc2", user1);

        Channel channel3 = new Channel("channel3", Channel.ChannelType.PUBLIC, "desc3", user2);

        em.persist(channel1);
        em.persist(channel2);
        em.persist(channel3);

        em.persist(new ChannelMember(user1, channel2));

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("사용자 아이디를 이용하여 해당 사용자가 속한 채널 전체를 반환한다.")
    void findAllByUserId() {
        List<Channel> result = channelRepository.findAllByUserId(user1.getId());

        assertThat(result).hasSize(2);
        for (Channel channel : result) {
            assertThat(channel.getOwner().getUsername()).isEqualTo("user1");
            assertThat(channel.getMembers()).isNotNull();
            for (ChannelMember member : channel.getMembers()) {
                assertThat(member.getUser()).isNotNull();
            }
        }
    }

    @Test
    @DisplayName("존재하지 않는 사용자 아이디를 이용하면 빈 값을 반환한다.")
    void findAllByUserIdFailed() {
        List<Channel> result = channelRepository.findAllByUserId(UUID.randomUUID());
        assertThat(result).isEmpty();
    }
}