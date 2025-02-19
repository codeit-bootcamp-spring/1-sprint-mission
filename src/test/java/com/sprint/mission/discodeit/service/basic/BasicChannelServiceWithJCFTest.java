//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//class BasicChannelServiceWithJCFTest {
//
//    ChannelRepository channelRepository = new JCFChannelRepository();
//
//    MessageService messageService = new BasicMessageService(new JCFMessageRepository());
//    ChannelService channelService = new BasicChannelService(channelRepository, messageService);
//
//    @BeforeEach
//    void init() {
//        channelService = new BasicChannelService(channelRepository, messageService);
//    }
//
//    @Test
//    @DisplayName("채널 저장")
//    void create() {
//        User user = new User("user", "user1@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user, new HashMap<>());
//
//        Assertions.assertEquals("ch.1", channel1.getChannelName());
//        Assertions.assertEquals(user, channel1.getChannelOwnerUser());
//    }
//
//    @Test
//    @DisplayName("채널 이름으로 찾기")
//    void getChannelName() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        Map<UUID, Channel> findChannel = channelService.getChannelByName("ch.2");
//
//        Assertions.assertEquals("ch.2", findChannel.get(channel2.getId()).getChannelName());
//    }
//
//    @Test
//    @DisplayName("채널 아이디로 찾기")
//    void findChannel() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        Channel findCh = channelService.findChannelById(channel1.getId());
//
//        Assertions.assertEquals(channel1, findCh);
//    }
//
//    @Test
//    @DisplayName("모든 채널 찾기")
//    void getAllChannels() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        Map<UUID, Channel> allChannels = channelService.getAllChannels();
//
//        Assertions.assertEquals(channel1, allChannels.get(channel1.getId()));
//        Assertions.assertEquals(channel2, allChannels.get(channel2.getId()));
//    }
//
//    @Test
//    @DisplayName("채널 업데이트")
//    void updateChannel() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        User user3 = new User("newOwner", "user3@gmail.com", "333ass12341");
//        channelService.updateChannel(channel2.getId(), "newChannelName",user3);
//
//        Channel channelById = channelService.findChannelById(channel2.getId());
//
//        Assertions.assertEquals("newChannelName", channelById.getChannelName());
//        Assertions.assertEquals(user3, channelById.getChannelOwnerUser());
//    }
//    @Test
//    @DisplayName("채널 아이디로 삭제")
//    void removeChannelById() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        channelService.removeChannelById(channel1.getId());
//
//        Assertions.assertNull(channelRepository.findChannelById(channel1.getId()));
//    }
//
//    @Test
//    @DisplayName("채널에 유저 추가")
//    void addUserChannel() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//
//        // 채널에 user3 추가
//        User user3 = new User("user3", "use33@gmail.com", "33pass12341");
//        channelService.addUserChannel(channel1.getId(), user3);
//
//
//        Channel findChannel = channelService.findChannelById(channel1.getId());
//        User findUser = findChannel.getChannelUsers().get(user3.getId());
//
//        Assertions.assertEquals(user3, findUser);
//    }
//
//    @Test
//    @DisplayName("채널에서 유저 추방")
//    void kickUserChannel() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        User newU1 = new User("newUser", "test@mmm.com", "dqdwqdwqdwqd");
//        User newU2 = new User("newUser2", "tes22112t@mmm.com", "213dqdwqdwqdwqd");
//        channelService.addUserChannel(channel1.getId(), newU1);
//        channelService.addUserChannel(channel1.getId(), newU2);
//
//        channelService.kickUserChannel(channel1.getId(), newU1);
//        User findUser = channelService.getAllChannels().get(channel1.getId()).getChannelUsers().get(newU1.getId()); // newU1 찾기
//
//        Assertions.assertNull(findUser);
//
//
//    }
//}