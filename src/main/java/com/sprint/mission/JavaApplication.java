package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Dto.UserDto;
import com.sprint.mission.discodeit.entity.Type.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;

import java.util.UUID;

public class JavaApplication {

    //todo 전체적으로해야할것 1 : 검증코드 try catch로 변경
    //todo 전체적으로해야할것 2 : 레포지토리 업데이트 코드 보완
    //todo 핸들러 호출방식 변경. 싱글톤->빈 주입

    static UserDto setupUser(UserService userService) {
        UUID user1 = userService.createUser("woody", "woody@codeit.com", "woody1234");
        return userService.findUserById(user1);
    }

    static ChannelDto setupChannel(ChannelService channelService) {
        UUID channel1 = channelService.createPublicChannel("공지", "공지 채널입니다.");
        return channelService.findChannelById(channel1);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        UUID message1 = messageService.createMessage(author.getId(), channel.getId(),"안녕하세요.");
        System.out.println("메시지 생성: " + message1);
    }

    public static void main(String[] args) {


        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        ReadStatusRepository readStatusRepository = new FileReadStatusRepository();
        BinaryContentRepository binaryContentRepository = new FileBinaryContentRepository();
        UserStatusService userStatusService = new BasicUserStatusService();
        FileIOHandler fileIOHandler = FileIOHandler.getInstance();

        UserService userService = new BasicUserService(userRepository, fileIOHandler, binaryContentRepository, userStatusService);
        MessageService messageService = new BasicMessageService(messageRepository,userRepository, channelRepository, binaryContentRepository, fileIOHandler);
        ChannelService channelService = new BasicChannelService(channelRepository,userRepository, readStatusRepository, userService, messageRepository);
        ReadStatusService readStatusService = new BasicReadStatusService(readStatusRepository, userRepository, messageRepository, channelService, channelRepository);




        //셋업
        UserDto user = setupUser(userService);
        ChannelDto channel = setupChannel(channelService);
        // 테스트
        //messageCreateTest(messageService, channel, user);


    }
}
