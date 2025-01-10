package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ChannelTest {
    ChannelService channelService = new JCFChannelService();
    List<User> userList = new ArrayList<>();

    @BeforeEach
    void init() {
        userList = new ArrayList<>();
    }

    @Test
    @DisplayName("채널이 제대로 생성되었는지 확인")
    void checkChannel() {
        String chName = "ch.1";
        User user = new User("user1", "user1@mail.com", "user12345");

        Channel ch1 = channelService.createChannel(chName, user, userList);

        Assertions.assertEquals(chName, ch1.getChannelName());
        Assertions.assertEquals(user, ch1.getChannelOwnerUser());
        Assertions.assertEquals(userList, ch1.getChannelUsers());
    }

    @Test
    @DisplayName("채널명이 공백일때 검증")
    void checkChannelNoneName() {
        String chName = "";
        User user = new User("user1", "user1@mail.com", "user12345");

        Assertions.assertThrows(IllegalArgumentException.class, () -> channelService.createChannel(chName, user, userList));
    }

    @Test
    @DisplayName("채널 주인장이 없을때 검증")
    void checkChannelNoneOwnerUser() {
        String chName = "test";
        User user = new User("user1", "user1@mail.com", "user12345");

        Assertions.assertThrows(IllegalAccessError.class, () -> channelService.createChannel(chName, null, userList));
    }

    @Test
    @DisplayName("채널에서 강퇴")
    void kickUser() {
        User user = new User("user1", "user1@mail.com", "user12345");
        channelService.createChannel("test", user, new ArrayList<>(List.of(user)));

        Assertions.assertThrows(IllegalArgumentException.class, () ->channelService.kickUserChannel("test", user));

    }
}
