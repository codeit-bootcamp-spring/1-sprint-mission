package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.config.JpaAuditingConfiguration;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.channel.ChannelRepositoryImpl;
import com.sprint.mission.discodeit.repository.jpa.message.MessageRepositoryImpl;
import com.sprint.mission.discodeit.repository.jpa.user.UserRepositoryImpl;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({UserRepositoryImpl.class, MessageRepositoryImpl.class, ChannelRepositoryImpl.class,
    JpaAuditingConfiguration.class})
public class MessageRepositoryTest {

    @Autowired
    private MessageRepositoryImpl messageRepository;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private ChannelRepositoryImpl channelRepository;

    private User saveUser(int num) {
        User user = User.of("name" + num, "email" + num,
            "password" + num);
        return userRepository.saveUser(user);
    }

    private Channel saveChannel(int num) {
        Channel channel = Channel.of(
            "name" + num,
            "description" + num,
            ChannelType.PUBLIC
        );
        return channelRepository.saveChannel(channel);
    }

    private Message saveMessage(User user, Channel channel, int num) {
        Message message = Message.of(user, channel, "content" + num);
        return messageRepository.saveMessage(message);
    }

    private Pageable getPageable() {
        return PageRequest.of(0, 10);
    }

    @Nested
    class SaveMessageTest {

        @Test
        void 메세지_저장_성공() {
            // given
            User user = saveUser(0);
            Channel channel = saveChannel(0);
            Message message = Message.of(user, channel, "content");

            // when
            Message savedMessage = messageRepository.saveMessage(message);

            // then
            assertEquals(user, savedMessage.getSender());
            assertEquals(channel, savedMessage.getChannel());
            assertEquals("content", savedMessage.getContent());
        }
    }

    @Nested
    class FindMessageByIdTest {

        @Test
        void 메세지_조회_성공() {
            // given
            int num = 0;
            User user = saveUser(num);
            Channel channel = saveChannel(num);
            UUID messageId = saveMessage(user, channel, num).getId();

            // when
            Optional<Message> messageOptional = messageRepository.findMessageById(messageId);

            // then
            assertTrue(messageOptional.isPresent());

            Message message = messageOptional.get();
            assertEquals(user, message.getSender());
            assertEquals(channel, message.getChannel());
            assertEquals("content" + num, message.getContent());
        }

        @Test
        void 메세지_조회_실패() {
            // given
            int num = 0;
            User user = saveUser(num);
            Channel channel = saveChannel(num);
            UUID messageId = saveMessage(user, channel, num).getId();

            // when
            Optional<Message> messageOptional = messageRepository.findMessageById(
                UUID.randomUUID());

            // then
            assertTrue(messageOptional.isEmpty());
        }
    }

    @Nested
    class FindPagedMessagesByChannelTest {

        @Test
        void 메세지_목록_조회_성공() {
            // given
            User user = saveUser(0);
            Channel channel = saveChannel(0);
            Pageable pageable = getPageable();

            int size = 20;
            for (int i = 0; i < size; i++) {
                saveMessage(user, channel, i);
            }

            // when
            Page<Message> messagePage = messageRepository.findPagedMessagesByChannel(channel,
                pageable);

            // then
            assertNotNull(messagePage);
            assertEquals(size, messagePage.getTotalElements());
            assertEquals(2, messagePage.getTotalPages());
        }
    }

    @Nested
    class FindSlicedMessagesByChannelTest {

        @Test
        void 메세지_목록_조회_성공() {
            // given
            User user = saveUser(0);
            Channel channel = saveChannel(0);
            Pageable pageable = getPageable();

            int size = 20;
            for (int i = 0; i < size; i++) {
                saveMessage(user, channel, i);
            }

            // when
            Slice<Message> messageSlice
                = messageRepository.findSlicedMessagesByChannel(channel, Instant.MIN, pageable);

            // then
            assertNotNull(messageSlice);
            assertEquals(10, messageSlice.getNumberOfElements());
        }

        @Test
        void 메세지_목록_조회_실패() {
            // given
            User user = saveUser(0);
            Channel channel = saveChannel(0);
            Pageable pageable = getPageable();

            int size = 20;
            for (int i = 0; i < size; i++) {
                saveMessage(user, channel, i);
            }

            // when
            Slice<Message> messageSlice
                = messageRepository.findSlicedMessagesByChannel(channel, Instant.now(), pageable);

            // then
            assertEquals(0, messageSlice.getNumberOfElements());
        }
    }

}
