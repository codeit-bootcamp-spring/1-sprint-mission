package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.AppConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BasicChannelServiceWithFileTest {

    ChannelRepository channelRepository = new FileChannelRepository(); // file repo
    MessageService messageService;
    ChannelService channelService;

    @BeforeEach
    void init() {
        AppConfig appConfig = new AppConfig();

        messageService = new FileMessageService();
        channelService = new BasicChannelService(channelRepository, messageService);
    }

    @Test
    @DisplayName("채널 저장")
    void create() {
        User user = new User("user", "user1@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user, new HashMap<>());

        assertEquals("ch.1", channel1.getChannelName());
        assertEquals(user, channel1.getChannelOwnerUser());
    }

    @Test
    @DisplayName("채널 이름으로 찾기")
    void getChannelName() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");

        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        // ch1 찾기
        Channel findChannel = channelService.findChannelById(channel1.getChannelId());

        // ch1 이름 찾기
        assertEquals("ch.1", findChannel.getChannelName());
    }

    @Test
    @DisplayName("채널 아이디로 찾기")
    void findChannel() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        Channel findCh = channelService.findChannelById(channel1.getChannelId());

        assertEquals(channel1, findCh);
    }

    @Test
    @DisplayName("모든 채널 찾기")
    void getAllChannels() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        Map<UUID, Channel> allChannels = channelService.getAllChannels();

        assertEquals(channel1, allChannels.get(channel1.getChannelId()));
        assertEquals(channel2, allChannels.get(channel2.getChannelId()));
    }

    @Test
    @DisplayName("채널 업데이트")
    void updateChannel() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        User user3 = new User("newOwner", "user3@gmail.com", "333ass12341");
        channelService.updateChannel(channel2.getChannelId(), "newChannelName",user3);

        Channel channelById = channelService.findChannelById(channel2.getChannelId());

        assertEquals("newChannelName", channelById.getChannelName());
        assertEquals(user3, channelById.getChannelOwnerUser());
    }

    @Test
    @DisplayName("채널 아이디로 삭제")
    void removeChannelById() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        channelService.removeChannelById(channel1.getChannelId());

        assertNull(channelRepository.findChannelById(channel1.getChannelId()));
    }

    @Test
    @DisplayName("채널에 유저 추가")
    void addUserChannel() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());


        // 채널에 user3 추가
        User user3 = new User("user3", "use33@gmail.com", "33pass12341");
        channelService.addUserChannel(channel1.getChannelId(), user3);


        Channel findChannel = channelService.findChannelById(channel1.getChannelId());
        User findUser = findChannel.getChannelUsers().get(user3.getUserId());

        assertEquals(user3, findUser);
    }

    @Test
    @DisplayName("채널에서 유저 추방")
    void kickUserChannel() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Channel channel2 = channelService.createChannel("ch.2", user2, new HashMap<>());

        User newU1 = new User("newUser", "test@mmm.com", "dqdwqdwqdwqd");
        User newU2 = new User("newUser2", "tes22112t@mmm.com", "213dqdwqdwqdwqd");
        channelService.addUserChannel(channel1.getChannelId(), newU1);
        channelService.addUserChannel(channel1.getChannelId(), newU2);

        channelService.kickUserChannel(channel1.getChannelId(), newU1);
        User findUser = channelService.getAllChannels().get(channel1.getChannelId()).getChannelUsers().get(newU1.getUserId()); // newU1 찾기

        assertNull(findUser);
    }

    @Test
    @DisplayName("채널에 메세지 추가")
    void addMessage() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Message message = new Message("title", "content", user1, user2);
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        channelService.addUserChannel(channel1.getChannelId(), user1);
        channelService.addUserChannel(channel1.getChannelId(), user2);

        // 채널에 메세지 추가
        channelService.addMessageInCh(channel1.getChannelId(), message);

        String messageTitle = channelService.getAllChannels().get(channel1.getChannelId()).getChannelMessages().get(message.getMessageId()).getMessageTitle();
        String messageContent = channelService.getAllChannels().get(channel1.getChannelId()).getChannelMessages().get(message.getMessageId()).getMessageContent();
        User messageSendUser = channelService.getAllChannels().get(channel1.getChannelId()).getChannelMessages().get(message.getMessageId()).getMessageSendUser();
        User messageReceiveUser = channelService.getAllChannels().get(channel1.getChannelId()).getChannelMessages().get(message.getMessageId()).getMessageReceiveUser();


        assertEquals("title", messageTitle);
        assertEquals("content", messageContent);
        assertEquals(user1, messageSendUser);
        assertEquals(user2, messageReceiveUser);
    }

    @Test
    @DisplayName("채널에서 메세지 조회")
    void findMessage() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        Message message = new Message("안녕", "하이", user1, user2);

        // 채널에 유저 추가
        channelService.addUserChannel(channel1.getChannelId(), user1);
        channelService.addUserChannel(channel1.getChannelId(), user2);

        // 채널 내 메세지 추가
        channelService.addMessageInCh(channel1.getChannelId(), message);

        // 메세지 찾기
        Message channelMessageById = channelService.findChannelMessageById(channel1.getChannelId(), message.getMessageId());

        assertEquals("안녕", channelMessageById.getMessageTitle());
        assertEquals("하이", channelMessageById.getMessageContent());

    }

    @Test
    @DisplayName("채널에서 모든 메세지 조회")
    void findAllMessage() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");
        Message message1 = new Message("title", "content", user1, user2);
        Message message2 = new Message("good", "hihihi", user2, user1);
        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        channelService.addUserChannel(channel1.getChannelId(), user1);
        channelService.addUserChannel(channel1.getChannelId(), user2);

        //메세지 추가
        channelService.addMessageInCh(channel1.getChannelId(), message1);
        channelService.addMessageInCh(channel1.getChannelId(), message2);

        // 모든 메세지 찾기
        Map<UUID, Message> channelInMessageAll = channelService.findChannelInMessageAll(channel1.getChannelId());

        // 특정 메세지 뽑고 그 메세지 찾기
        Message findMessage = channelService.findChannelMessageById(channel1.getChannelId(), message1.getMessageId());
        assertEquals("title", findMessage.getMessageTitle());

    }

    @Test
    @DisplayName("채널에서 메세지 삭제")
    void removeMessage() {
        User user1 = new User("user1", "user1@gmail.com", "pass12341");
        User user2 = new User("user2", "user2@gmail.com", "pass12341");

        Message message1 = messageService.createMessage("title", "content", user1, user2);
        Message message2 = messageService.createMessage("good", "hihihi", user2, user1);

        Channel channel1 = channelService.createChannel("ch.1", user1, new HashMap<>());
        channelService.addUserChannel(channel1.getChannelId(), user1);
        channelService.addUserChannel(channel1.getChannelId(), user2);

        channelService.addMessageInCh(channel1.getChannelId(), message1);
        channelService.addMessageInCh(channel1.getChannelId(), message2);

        channelService.removeMessageInCh(channel1.getChannelId(), message1);

        //assertThrows(MessageNotFoundException.class,() -> channelService.findChannelMessageById(channel1.getChannelId(), message1.getMessageId()));
    }
}
