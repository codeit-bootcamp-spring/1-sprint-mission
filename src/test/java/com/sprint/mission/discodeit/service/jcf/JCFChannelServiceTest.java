package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JCFChannelServiceTest {

    private ChannelService channelService;
    private User ownerUser;
    private User user;
    private UUID channelId;
    private String channelName;
    private Map<UUID, User> userList;

    @BeforeEach
    void init() {
        channelService = new JCFChannelService();
        channelName = "test-channel";
        channelId = UUID.randomUUID();
        ownerUser = new User("owner", "owner@test.com", "password");
        user = new User("test", "test@test.com", "password");
        userList = new HashMap<>();
        userList.put(ownerUser.getUserId(), ownerUser);
    }


    @Test
    @DisplayName("채널 생성")
    void createChannelTest() {
        Channel channel = channelService.createChannel(channelName, ownerUser, userList);



        assertNotNull(channel);
        assertEquals(channelName, channel.getChannelName());
        assertEquals(ownerUser, channel.getChannelOwnerUser());
    }


    @Test
    @DisplayName("채널명이 null일 때 예외 발생")
    void createChannelWithNullNameTest() {
        assertThrows(IllegalArgumentException.class,
                () -> channelService.createChannel(null, ownerUser, userList));
    }

    @Test
    @DisplayName("채널 이름으로 채널 조회")
    void getChannelByNameTest() {
        Channel channel = channelService.createChannel(channelName, ownerUser, userList);
        Map<UUID, Channel> result = channelService.getChannelByName(channelName);

        assertFalse(result.isEmpty());
        assertTrue(result.containsValue(channel));
    }


    @Test
    @DisplayName("채널 업데이트")
    void updateChannelTest() {
        Channel channel = channelService.createChannel(channelName, ownerUser, userList);
        String newChannelName = "updated-channel";

        Channel updatedChannel = channelService.updateChannel(channel.getChannelId(), newChannelName, user);

        assertEquals(newChannelName, updatedChannel.getChannelName());
        assertEquals(user, updatedChannel.getChannelOwnerUser());
    }

    @Test
    @DisplayName("채널에 유저 추가")
    void addUserChannelTest() {
        Channel channel = channelService.createChannel(channelName, ownerUser, userList);
        channelService.addUserChannel(channel.getChannelId(), user);

        Channel updatedChannel = channelService.findChannelById(channel.getChannelId());
        assertTrue(updatedChannel.getChannelUsers().containsValue(user));
    }

    @Test
    @DisplayName("채널에서 유저 제거")
    void kickUserChannelTest() {
        Channel channel = channelService.createChannel(channelName, ownerUser, userList);
        channelService.addUserChannel(channel.getChannelId(), user);
        channelService.kickUserChannel(channel.getChannelId(), user);

        Channel updatedChannel = channelService.findChannelById(channel.getChannelId());
        assertFalse(updatedChannel.getChannelUsers().containsValue(user));
    }
}