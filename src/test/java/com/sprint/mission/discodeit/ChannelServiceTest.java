package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.*;
import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.channel.CreateChannel;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileIOUtil;
import com.sprint.mission.discodeit.util.type.ChannelFormat;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class ChannelServiceTest {
    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService;

    private List<UUID> joinUser;

    private UserDTO testUser1DTO;
    private UserDTO testUser2DTO;
    private UserDTO testUser3DTO;
    private UserDTO testUser4DTO;

    @BeforeEach
    void before() {
        FileIOUtil.initializeFiles();
        CreateUserRequest request1 = new CreateUserRequest("테스트유저1", "010-1111-1111", "Abcdefg1234!!", null);
        CreateUserRequest request2 = new CreateUserRequest("테스트유저2", "010-2222-1111", "Abcdefg1234!!", null);
        CreateUserRequest request3 = new CreateUserRequest("테스트유저3", "010-3333-1111", "Abcdefg1234!!", null);
        CreateUserRequest request4 = new CreateUserRequest("테스트유저4", "010-4444-1111", "Abcdefg1234!!", null);
        testUser1DTO = userService.create(request1);
        testUser2DTO = userService.create(request2);
        testUser3DTO = userService.create(request3);
        testUser4DTO = userService.create(request4);

    }

    @AfterEach
    void after() {
        FileIOUtil.convertSerToJson(Path.of("./result/users.ser"), Path.of("./json/users.json"), User.class);
        FileIOUtil.convertSerToJson(Path.of("./result/channels.ser"), Path.of("./json/channels.json"), Channel.class);
        FileIOUtil.convertSerToJson(Path.of("./result/binarycontent.ser"), Path.of("./json/binarycontent.json"), Channel.class);
        FileIOUtil.convertSerToJson(Path.of("./result/readstatus.ser"), Path.of("./json/readstatus.json"), Channel.class);
        FileIOUtil.convertSerToJson(Path.of("./result/userstatus.ser"), Path.of("./json/userstatus.json"), Channel.class);
        FileIOUtil.initializeFiles();
    }

    @DisplayName("Public 채널 테스트하기")
    @Test
    void createPublicChannel() {
        joinUser = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser2DTO.getId(), testUser3DTO.getId(), testUser4DTO.getId()));
        CreateChannel.PublicRequest request = new CreateChannel.PublicRequest("백엔드 개발자 오픈 채널", "지식 공유를 위해 만든 채널입니다.", ChannelFormat.TEXT, joinUser);
        PublicChannel publicChannel = (PublicChannel) channelService.createPublicChannel(request);

        Assertions.assertEquals(request.getName(), publicChannel.getName());
    }

    @DisplayName("Private 채널 테스트하기")
    @Test
    void createPrivateChannel() {
        joinUser = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser2DTO.getId(), testUser3DTO.getId(), testUser4DTO.getId()));
        CreateChannel.PrivateRequest request = new CreateChannel.PrivateRequest(joinUser, ChannelFormat.TEXT);
        Channel privateChannel = channelService.createPrivateChannel(request);

        Assertions.assertNotNull(privateChannel);
    }

    @DisplayName("Public 채널 조회하기")
    @Test
    void findPublicChannel() {
        // 채널 만들고
        joinUser = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser2DTO.getId(), testUser3DTO.getId(), testUser4DTO.getId()));
        CreateChannel.PublicRequest request = new CreateChannel.PublicRequest("프론트앤드 개발자 오픈 채널", "지식 공유를 위해 만든 채널입니다.", ChannelFormat.TEXT, joinUser);
        PublicChannel publicChannel = (PublicChannel) channelService.createPublicChannel(request);

        ChannelDTO.PublicChannelDTO publicChannel1 = channelService.findPublicChannel(publicChannel.getId());

        Assertions.assertEquals(publicChannel.getId(), publicChannel1.getId());
    }

    @DisplayName("Private 채널 만들고 조회하기")
    @Test
    void findPrivateChannel() {
        //채널 만들기
        joinUser = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser2DTO.getId(), testUser3DTO.getId(), testUser4DTO.getId()));
        CreateChannel.PrivateRequest request = new CreateChannel.PrivateRequest(joinUser, ChannelFormat.TEXT);
        Channel privateChannel = channelService.createPrivateChannel(request);

        ChannelDTO.PrivateChannelDTO privateChannel1 = channelService.findPrivateChannel(privateChannel.getId());

        Assertions.assertEquals(privateChannel.getId(), privateChannel1.getId());
    }

    @DisplayName("특정 회원이 들어간 Private 채널 조회하기")
    @Test
    void findAllPrivate() {
        List<UUID> joinUser1 = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser2DTO.getId()));
        List<UUID> joinUser2 = new ArrayList<>(Arrays.asList(testUser2DTO.getId(), testUser3DTO.getId()));
        List<UUID> joinUser3 = new ArrayList<>(Arrays.asList(testUser3DTO.getId(), testUser4DTO.getId()));
        List<UUID> joinUser4 = new ArrayList<>(Arrays.asList(testUser1DTO.getId(), testUser4DTO.getId()));

        CreateChannel.PrivateRequest request1 = new CreateChannel.PrivateRequest(joinUser1, ChannelFormat.TEXT);
        CreateChannel.PrivateRequest request2 = new CreateChannel.PrivateRequest(joinUser2, ChannelFormat.TEXT);
        CreateChannel.PrivateRequest request3 = new CreateChannel.PrivateRequest(joinUser3, ChannelFormat.TEXT);
        CreateChannel.PrivateRequest request4 = new CreateChannel.PrivateRequest(joinUser4, ChannelFormat.TEXT);

        Channel privateChannel1 = channelService.createPrivateChannel(request1);
        Channel privateChannel2 = channelService.createPrivateChannel(request2);
        Channel privateChannel3 = channelService.createPrivateChannel(request3);
        Channel privateChannel4 = channelService.createPrivateChannel(request4);

        List<Channel> user1PrivateChannels = channelService.findAllPrivate(testUser1DTO.getId());
        Assertions.assertEquals(user1PrivateChannels.size(), 2); // testUser1DTO 회원이 들어가 있는 채널은 2개이다.
    }

}
