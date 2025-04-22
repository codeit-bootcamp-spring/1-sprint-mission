package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.Channel.ChannelType.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("db")
@ActiveProfiles("test")
@SpringBootTest
class BasicChannelServiceTest {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공개 채널 생성 요청을 받아 생성 및 DB에 저장합니다.")
    @Transactional
    void createPublicChannelTest() {
        // given
        User user = getUser("cloudsoda", "구름소다", "010-1111-2222");

        PublicChannelRequestDto publicChannelRequestDto = new PublicChannelRequestDto(
                "serverName", "description", user.getId()
        );

        // when
        ChannelResponseDto result = channelService.createPublicChannel(publicChannelRequestDto);

        // then
        assertThat(result).isNotNull()
                .extracting("name", "description")
                .contains("serverName", "description");
    }

    @Test
    @DisplayName("비공개 채널 생성 요청을 받아 생성 및 DB에 저장합니다.")
    @Transactional
    void createPrivateChannelTest() {
        // given
        User user = getUser("cloudsoda", "구름소다", "010-1111-2222");
        User member = getUser("izna", "이즈나", "010-3333-2222");

        List<UUID> participantIds = List.of(member.getId());

        PrivateChannelRequestDto privateChannelRequestDto = new PrivateChannelRequestDto(
                user.getId(), participantIds
        );

        // when
        ChannelResponseDto result = channelService.createPrivateChannel(privateChannelRequestDto);

        // then
        assertThat(result).isNotNull()
                .extracting("name", "type")
                .contains("", PRIVATE);
        assertThat(result.participants()).hasSize(1);
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


}