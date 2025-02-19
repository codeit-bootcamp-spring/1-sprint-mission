//package com.sprint.mission.discodeit.entity;
//
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.factory.BaseEntityFactory;
//import com.sprint.mission.discodeit.factory.EntityFactory;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class ChannelTest {
//    EntityFactory entityFactory = BaseEntityFactory.getInstance();
//    ChannelService channelService = new JCFChannelService();
//    Map<UUID, User> userList = new HashMap<>();
//
//    @BeforeEach
//    void init() {
//        userList = new HashMap<>();
//    }
//
//    @Test
//    @DisplayName("채널이 제대로 생성되었는지 확인")
//    void checkChannel() {
//        String chName = "ch.1";
//        User user = new User("user1", "user1@mail.com", "user12345");
//
//        channelService.createChannel("tit", user, new HashMap<>());
//    }
//
//    @Test
//    @DisplayName("채널 업데이트 검증")
//    void checkUpdateChannel() {
//        User user1 = new User("user1", "user1@naver.com", "user12345");
//        User user2 = new User("user2", "user2@gmail.com", "2222user12345");
//
//        Channel ch1 = channelService.createChannel("ch.1", user1, Map.of(user1.getId(), user1, user2.getId(), user2));
//
//        channelService.updateChannel(ch1.getId(), "ch.999", user2);
//
//        Assertions.assertEquals("ch.999", ch1.getChannelName());
//        Assertions.assertEquals(user2, ch1.getChannelOwnerUser());
//    }
//
//    @Test
//    @DisplayName("채널명이 공백일때 검증")
//    void checkChannelNoneName() {
//        String chName = "";
//        User user = new User("user1", "user1@mail.com", "user12345");
//
//        Assertions.assertThrows(IllegalArgumentException.class, () -> channelService.createChannel(chName, user, userList));
//    }
//
//    @Test
//    @DisplayName("채널 주인장이 없을때 검증")
//    void checkChannelNoneOwnerUser() {
//        String chName = "test";
//        User user = new User("user1", "user1@mail.com", "user12345");
//
//        Assertions.assertThrows(UserNotFoundException.class, () -> channelService.createChannel(chName, null, userList));
//    }
//
//    @Test
//    @DisplayName("채널에서 강퇴")
//    void kickUser() {
//        User user = new User("user1", "user1@mail.com", "user12345");
//        Channel channel = channelService.createChannel("test", user, Map.of(user.getId(), user));
//
//        Assertions.assertThrows(UserNotFoundException.class, () -> channelService.kickUserChannel(channel.getId(), user));
//
//    }
//}
