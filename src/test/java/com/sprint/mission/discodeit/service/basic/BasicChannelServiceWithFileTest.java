//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.AppConfig;
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.file.json.JsonChannelRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.file.FileMessageService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class BasicChannelServiceWithFileTest {
//
//    ChannelRepository channelRepository = new JsonChannelRepository(); // file repo
//    MessageService messageService;
//    ChannelService channelService;
//
//    @BeforeEach
//    void init() {
//        AppConfig appConfig = new AppConfig();
//
//        messageService = new FileMessageService();
//        channelService = new BasicChannelService(channelRepository, messageService);
//    }
//
//    @Test
//    @DisplayName("채널 저장")
//    void create() {
//        User user = new User("user", "user1@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user, new HashMap<>());
//
//        assertEquals("ch.1", channel1.getChannelName());
//        assertEquals(user, channel1.getChannelOwnerUser());
//    }
//
//    @Test
//    @DisplayName("채널 이름으로 찾기")
//    void getChannelName() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());
//
//        // ch1 찾기
//        Channel findChannel = channelService.findChannelById(channel1.getId());
//
//        // ch1 이름 찾기
//        assertEquals("ch.1", findChannel.getChannelName());
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
//        assertEquals(channel1, findCh);
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
//        assertEquals(channel1, allChannels.get(channel1.getId()));
//        assertEquals(channel2, allChannels.get(channel2.getId()));
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
//        assertEquals("newChannelName", channelById.getChannelName());
//        assertEquals(user3, channelById.getChannelOwnerUser());
//    }
//
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
//        assertNull(channelRepository.findChannelById(channel1.getId()));
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
//        assertEquals(user3, findUser);
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
//        assertNull(findUser);
//    }
//
//    @Test
//    @DisplayName("채널에 메세지 추가")
//    void addMessage() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Message message = new Message("title", "content", user1, user2);
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        channelService.addUserChannel(channel1.getId(), user1);
//        channelService.addUserChannel(channel1.getId(), user2);
//
//        // 채널에 메세지 추가
//        channelService.addMessageInCh(channel1.getId(), message);
//
//        String messageTitle = channelService.getAllChannels().get(channel1.getId()).getChannelMessages().get(message.getId()).getMessageTitle();
//        String messageContent = channelService.getAllChannels().get(channel1.getId()).getChannelMessages().get(message.getId()).getMessageContent();
//        User messageSendUser = channelService.getAllChannels().get(channel1.getId()).getChannelMessages().get(message.getId()).getMessageSendUser();
//        User messageReceiveUser = channelService.getAllChannels().get(channel1.getId()).getChannelMessages().get(message.getId()).getMessageReceiveUser();
//
//
//        assertEquals("title", messageTitle);
//        assertEquals("content", messageContent);
//        assertEquals(user1, messageSendUser);
//        assertEquals(user2, messageReceiveUser);
//    }
//
//    @Test
//    @DisplayName("채널에서 메세지 조회")
//    void findMessage() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        Message message = new Message("안녕", "하이", user1, user2);
//
//        // 채널에 유저 추가
//        channelService.addUserChannel(channel1.getId(), user1);
//        channelService.addUserChannel(channel1.getId(), user2);
//
//        // 채널 내 메세지 추가
//        channelService.addMessageInCh(channel1.getId(), message);
//
//        // 메세지 찾기
//        Message channelMessageById = channelService.findChannelMessageById(channel1.getId(), message.getId());
//
//        assertEquals("안녕", channelMessageById.getMessageTitle());
//        assertEquals("하이", channelMessageById.getMessageContent());
//
//    }
//
//    @Test
//    @DisplayName("채널에서 모든 메세지 조회")
//    void findAllMessage() {
//        User user1 = new User("user1", "user1@gmail.com", "pass12341");
//        User user2 = new User("user2", "user2@gmail.com", "pass12341");
//        Message message1 = new Message("title", "content", user1, user2);
//        Message message2 = new Message("good", "hihihi", user2, user1);
//        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
//        channelService.addUserChannel(channel1.getId(), user1);
//        channelService.addUserChannel(channel1.getId(), user2);
//
//        //메세지 추가
//        channelService.addMessageInCh(channel1.getId(), message1);
//        channelService.addMessageInCh(channel1.getId(), message2);
//
//        // 모든 메세지 찾기
//        Map<UUID, Message> channelInMessageAll = channelService.findChannelInMessageAll(channel1.getId());
//
//        // 특정 메세지 뽑고 그 메세지 찾기
//        Message findMessage = channelService.findChannelMessageById(channel1.getId(), message1.getId());
//        assertEquals("title", findMessage.getMessageTitle());
//
//    }
//
////    @Test
////    @DisplayName("채널에서 메세지 삭제")
////    void removeMessage() {
////        User user1 = new User("user1", "user1@gmail.com", "pass12341");
////        User user2 = new User("user2", "user2@gmail.com", "pass12341");
////
////        Message message1 = messageService.createMessage("title", "content", user1, user2);
////        Message message2 = messageService.createMessage("good", "hihihi", user2, user1);
////
////        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
////        channelService.addUserChannel(channel1.getId(), user1);
////        channelService.addUserChannel(channel1.getId(), user2);
////
////        channelService.addMessageInCh(channel1.getId(), message1);
////        channelService.addMessageInCh(channel1.getId(), message2);
////
////        channelService.removeMessageInCh(channel1.getId(), message1);
////
////        //assertThrows(MessageNotFoundException.class,() -> channelService.findChannelMessageById(channel1.getId(), message1.getId()));
////    }
//}
