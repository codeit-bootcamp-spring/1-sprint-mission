package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Dto.UserDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

import java.util.UUID;

public class JavaApplication {

    //todo 전체적으로해야할것 1 : 검증코드 try catch로 변경
    //todo 전체적으로해야할것 2 : 레포지토리 업데이트 코드 보완

    static UserDto setupUser(UserService userService) {
        UUID user1 = userService.createUser("woody", "woody@codeit.com", "woody1234");
        return userService.findUserById(user1);
    }
/*
    static Channel setupChannel(ChannelService channelService) {
        UUID channel1 = channelService.createChannel(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channelService.getChannelById(channel1);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        UUID message1 = messageService.createMessage(author.getId(), channel.getId(),"안녕하세요.");
        System.out.println("메시지 생성: " + message1);
    }
*/
    public static void main(String[] args) {

        UserRepository userRepository = new FileUserRepository();
        //ChannelRepository channelRepository = new FileChannelRepository();
        //MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository);
        //ChannelService channelService = new BasicChannelService(channelRepository,userRepository);
        //MessageService messageService = new BasicMessageService(messageRepository,userRepository,channelRepository);




        //셋업
        UserDto user = setupUser(userService);
        //Channel channel = setupChannel(channelService);
        // 테스트
        //messageCreateTest(messageService, channel, user);


    }
}
