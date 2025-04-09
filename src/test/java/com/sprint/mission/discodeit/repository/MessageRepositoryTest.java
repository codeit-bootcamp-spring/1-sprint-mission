package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.TestAuditConfig;
import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestAuditConfig.class)
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager em;

    private User owner;
    private User member1;

    private Channel publicChannel;
    private Channel privateChannel;

    private Message message;

    @BeforeEach
    void setUp() {
        owner = new User("owner", "owner", "a@test.com", "pass1",
                new Phone("010-1111-1111", Phone.RegionCode.KR),
                User.UserType.COMMON, "", null);

        member1 = new User("member1", "member1", "member1@test.com", "member1",
                new Phone("010-2222-1111", Phone.RegionCode.KR),
                User.UserType.COMMON, "", null);

        em.persist(owner);
        em.persist(member1);

        publicChannel = new Channel("channel1", Channel.ChannelType.PUBLIC, "desc1", owner);
        privateChannel = new Channel("channel2", Channel.ChannelType.PRIVATE, "desc2", owner);

        em.persist(publicChannel);
        em.persist(privateChannel);

        em.persist(new ChannelMember(member1, privateChannel));

        Message message1 = new Message("message1", publicChannel, member1);
        em.persist(message1);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("요청한 채널의 마지막 메시지를 보낸 시간을 성공적으로 전달한다.")
    void findLastMessageAtByChannelId() {
        // given
        Instant result = messageRepository.findLastMessageAtByChannelId(publicChannel.getId());
        // when
        assertThat(result).isNotNull();
        assertThat(result).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    @DisplayName("해당 채널에 메시지가 존재하지 않으면 빈 값을 반환한다")
    void findLastMessageAtByChannelIdFailed() {
        // given
        Instant result = messageRepository.findLastMessageAtByChannelId(privateChannel.getId());
        // when
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("메시지 조회 시 채널에서 첫 번째 페이지를 등록 최신 순으로 조회한다.")
    void findMessagesFirstPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Slice<Message> messages = messageRepository.findMessagesFirstPage(publicChannel.getId(), pageable);

        // then
        assertThat(messages).isNotNull();
        assertThat(messages.getContent()).hasSize(1);
        assertThat(messages.getContent().get(0).getContent()).isEqualTo("message1");
    }

    @Test
    @DisplayName("메시지 조회 시 첫 번째 페이지를 조회할 때 조회할 데이터가 없을 시 빈 값을 반환한다.")
    void findMessagesFirstPageFailedCauseEmptyMessage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Slice<Message> messages = messageRepository.findMessagesFirstPage(privateChannel.getId(), pageable);

        // then
        assertThat(messages).isNotNull();
        assertThat(messages).hasSize(0);
    }
}