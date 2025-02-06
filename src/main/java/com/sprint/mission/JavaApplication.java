package com.sprint.mission;

import com.sprint.mission.discodeit.entity.ChannelType;
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

import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        ChannelRepository channelRepository;
        MessageRepository messageRepository;
        UserRepository userRepository;

        channelRepository = FileChannelRepository.getInstance();
        messageRepository = FileMessageRepository.getInstance();
        userRepository = FileUserRepository.getInstance();

        ChannelService channelService = new BasicChannelService(channelRepository, userRepository);
        MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
        UserService userService = new BasicUserService(userRepository);

        //유저 생성
        UUID user1 = userService.createUser("Kaya");
        UUID user2 = userService.createUser("Ayden");
        UUID user3 = userService.createUser("Sia");

        //채널 생성
        UUID channel1 = channelService.createChannel(ChannelType.PUBLIC, "Kaya's Channel");

        //JCF채널1에 생성한 모든 유저 추가
        userService.getUsersMap().keySet().stream().forEach(userId -> {channelService.addChannelMember(channel1, userId);});

        //JCF채널1에 속한 유저 이름 출력
        channelService.printAllMemberNames(channel1);


        //메세지 생성
        UUID message1 = messageService.createMessage(user2, channel1, "Hello World");
        UUID message2 = messageService.createMessage(user1, channel1, "It's hard to debug... Hey java.. just work al jal ddak ggal sen please.. ");

        //메세지 내용 및 상세정보 출력
        messageService.printMessageDetails(message1);
        messageService.printMessageDetails(message2);

        //메세지2 내용 수정
        messageService.reviseMessageContent(message2, "Bye World");
        //메세지2 내용 및 상세정보 출력
        messageService.printMessageDetails(message2);
        }



    }
