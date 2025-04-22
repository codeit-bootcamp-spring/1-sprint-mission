package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag("db")
@ActiveProfiles("test")
@SpringBootTest
class BasicMessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("요청을 받아 첨부파일이 포함되지 않은 메시지를 생성합니다.")
    @Transactional
    void createMessageTest() throws IOException {
        // given
        User user = getUser("cloudsoda", "구름소다", "010-1111-2222");
        Channel channel = getChannel(user);

        MessageRequestDto messageRequestDto = new MessageRequestDto(
                "네모네모 네모네모 싸인", user.getId(), channel.getId());

        // when
        MessageResponseDto result = messageService.create(messageRequestDto, List.of());

        // then
        Assertions.assertThat(result).isNotNull()
                .extracting("content", "channelId")
                .contains(messageRequestDto.content(), channel.getId());
    }

    @Test
    @DisplayName("요청을 받아 채널 메시지 페이지를 조회합니다.")
    @Transactional
    void getMessagesByChannel() throws IOException {
        // given
        User user = getUser("cloudsoda", "구름소다", "010-1111-2222");
        Channel channel = getChannel(user);

        createMessages(user.getId(), channel.getId(), 20);

        // when
        PageResponse<MessageResponseDto> result = messageService.findMessagesByChannelId(channel.getId(), null);

        // then
        Assertions.assertThat(result)
                .isNotNull()
                .extracting("hasNext", "size")
                .contains(false, 50);

        Assertions.assertThat(result.content)
                .isNotEmpty()
                .allSatisfy(dto -> {
                    Assertions.assertThat(dto.id()).isNotNull();
                    Assertions.assertThat(dto.content()).isNotBlank();
                    Assertions.assertThat(dto.channelId()).isEqualTo(channel.getId());
                });
    }

    private User getUser(String username, String nickname, String phone) {
        User user = new User(
                username, nickname, nickname + "@mail.com",
                "!@A445" + nickname, new Phone(phone, Phone.RegionCode.KR),
                User.UserType.COMMON, "", null
        );
        userRepository.save(user);

        UserStatus status = new UserStatus(Instant.now(), user);
        user.updateUserStatus(status);
        userRepository.save(user);
        return user;
    }

    private Channel getChannel(User owner) {
        Channel channel = new Channel(
                "test", Channel.ChannelType.PUBLIC, "channelDescription", owner
        );
        channelRepository.save(channel);

        return channel;
    }

    private void createMessages(UUID authorId, UUID channelId, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            MessageRequestDto messageRequestDto = new MessageRequestDto(
                    "네모네모 네모네모 싸인 " + i + "회차", authorId, channelId);
            messageService.create(messageRequestDto, List.of());
        }
    }
}