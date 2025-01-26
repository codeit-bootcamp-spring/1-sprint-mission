package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.test.ChannelTest;
import com.sprint.mission.discodeit.test.MessageTest;
import com.sprint.mission.discodeit.test.UserTest;

import java.io.IOException;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) throws IOException {

        /**
         * User Test
         */

        // 필요한 인스턴스 생성
        UserRepository userRepository = new FileUserRepository();
        UserService userService = new BasicUserService(userRepository);

        // 유저 생성
        User user1 = UserTest.setUpUser(userService, userRepository);
        User user2 = UserTest.setUpUser(userService, userRepository);

        // 테스트용 유저 UUID
        UUID user1Id = user1.getId();
        UUID user2Id = user2.getId();

        // 유저 정보 변경
        UserTest.updateUser(user1Id, userService);

        // 유저 삭제
        UserTest.deleteUser(user2Id, userService);


        /**
         * Channel Test
         */

        // 필요한 인스턴스 생성
        ChannelRepository channelRepository = new FileChannelRepository();
        ChannelService channelService = new BasicChannelService(channelRepository);

        // 채널 생성
        Channel channel1 = ChannelTest.setUpChannel(user1, channelService, userService);
        Channel channel2 = ChannelTest.setUpChannel(user1, channelService, userService);

        // 테스트용 채널 UUID
        UUID channel1Id = channel1.getId();
        UUID channel2Id = channel2.getId();

        // 채널 정보 변경
        ChannelTest.updateChannel(channel1Id, channelService, userService);

        // 채널 멤버 변경
        ChannelTest.addMember(channel1Id, user2Id, channelService, userService);
        ChannelTest.deleteMember(channel1Id, user2Id, channelService, userService);

        // 채널 삭제
        ChannelTest.deleteChannel(channel2Id, channelService, userService);


        /**
         * Message Test
         */

        // 필요한 인스턴스 생성
        MessageRepository messageRepository = new FileMessageRepository();
        MessageService messageService = new BasicMessageService(messageRepository);

        // 메시지 생성
        Message message1 = MessageTest.setUpMessage(channel1, user1Id, messageService, channelService, userService);

        // 테스트용 메시지 UUID
        UUID message1Id = message1.getId();

        // 메시지 수정
        MessageTest.updateMessage(message1Id, messageService, channelService, userService);

        // 메시지 삭제
        MessageTest.deleteMessage(message1Id, messageService, channelService, userService);
    }
}