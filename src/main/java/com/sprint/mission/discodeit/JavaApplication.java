package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDTO;
import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;

public class JavaApplication {
    static User setupUser(UserService userService) {
        CreateUserDTO createUserDTO = CreateUserDTO.builder()
                .username("woody")
                .email("woody@codeit.com")
                .password("woody1234")
                .build();
        return userService.create(createUserDTO);
    }

    static Channel setupChannel(ChannelService channelService) {
        ChannelDTO.PublicChannelDTO publicChannelDTO = new ChannelDTO.PublicChannelDTO("공지", "공지 채널입니다.");
        return channelService.createPublicChannel(publicChannelDTO);
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        MessageDTO.CreateMessageDTO createMessageDTO = MessageDTO.CreateMessageDTO.builder()
                .content("안녕하세요.")
                .channelId(channel.getId())
                .authorId(author.getId())
                .build();
        Message message = messageService.create(createMessageDTO);
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 레포지토리 초기화
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        // 서비스 초기화
        UserService userService = new BasicUserService(userRepository);
        ChannelService channelService = new BasicChannelService(channelRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
