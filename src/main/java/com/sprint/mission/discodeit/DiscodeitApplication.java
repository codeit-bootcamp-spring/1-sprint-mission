package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.domain.ChannelType;
import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
    static List<UserRequest> setupUser(UserService userService) {
        List<UserRequest> users = List.of(
                new UserRequest("JohnDoe", "P@ssw0rd1", "john.doe@example.com", "010-1234-5678",null),
                new UserRequest("JaneDoe", "P@ssw0rd2", "jane.doe@example.com", "010-5678-1234",null),
                new UserRequest("Alice", "P@ssw0rd3", "alice.wonderland@example.com", "010-8765-4321", null),
                new UserRequest("Bob", "P@ssw0rd4", "bob.builder@example.com", "010-4321-8765", null),
                new UserRequest("Charlie", "P@ssw0rd5", "charlie.choco@example.com", "010-5678-9876", null)
        );

        for (UserRequest user : users) {
            userService.create(user);
        }
        return users;
    }
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        AuthService authService = context.getBean(AuthService.class);
        ChannelService channelService = context.getBean(ChannelService.class);

        setupUser(userService);

        System.out.println("==== User Read All ====");
        List<UserResponse> users = userService.readAll();
        users.forEach(user -> {
            System.out.println("userName : " + user.username()
                    + " | Email : " + user.email()
                    + " | phoneNumber : " + user.phoneNumber()
                    + " | isOnline : " + user.isOnline()
            );
        });

        System.out.println("==== User 2 Read ====");
        UserResponse findUser = userService.readOne(users.get(2).id());
        System.out.println("userName : " + findUser.username()
                + " | Email : " + findUser.email()
                + " | phoneNumber : " + findUser.phoneNumber()
                + " | isOnline : " + findUser.isOnline()
        );

//        System.out.println("==== User 2 Delete ====");
//        userService.delete(users.get(2).id());

        System.out.println("==== User 1 Update ====");
        UserResponse updateUser = userService.update(users.get(1).id(), new UserRequest("martin", "3ksp21","martin@codeit.com", "010-2323-2323",null));

        authService.login("alice.wonderland@example.com","P@ssw0rd3");
        authService.login("bob.builder@example.com", "123123");



        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Channel>>>>>>>>>>>>>>>>>>>>>>>");
        List<ChannelRequest> channels = List.of(
                new ChannelRequest("SB_1_Sprint", "JAVA Spring BackEnd Developer discode",
                        new ArrayList<>(List.of(users.get(0).id(), users.get(1).id())), users.get(1).id(), ChannelType.Private),
                new ChannelRequest("JavaScript_TechTalk", "All about JavaScript and its frameworks",
                        new ArrayList<>(), users.get(2).id(), ChannelType.Public),
                new ChannelRequest("React_FrontEnd", "Front-end technologies focusing on React",
                        new ArrayList<>(List.of(users.get(3).id(), users.get(4).id())), users.get(4).id(), ChannelType.Private),
                new ChannelRequest("NodeJS_DevGroup", "Node.js development and deployment discussions",
                        new ArrayList<>(), users.get(0).id(),ChannelType.Public),
                new ChannelRequest("SpringBoot_Dev", "Discussions on SpringBoot applications and microservices",
                        new ArrayList<>(List.of(users.get(1).id(), users.get(4).id())), users.get(1).id(), ChannelType.Private)
        );


        channels.forEach(channel -> channelService.create(channel));

        List<ChannelResponse> channelList = channelService.readAll();
        channelList.forEach(channel -> {
            if(channel.channelType() == ChannelType.Private){
                System.out.println("Private Channel");
            }else{
                System.out.println("channelName : " + channel.name()
                        + " | description : " + channel.description()
                );
            }

        });
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Message>>>>>>>>>>>>>>>>>>>>>>>");
//
//        List<MessageResponse> messages = List.of(
//            new MessageResponse("Hello", users.get(1), users.get(4)),
//            new MessageResponse("처음 뵙겠습니다~", users.get(1), channels.get(1)),
//            new MessageResponse("안녕하세요!", users.get(2), channels.get(0)),
//            new MessageResponse("채널 가입 요청 드립니다.", users.get(3), channels.get(2)),
//            new MessageResponse("반가워요!", users.get(1), users.get(3)),
//            new MessageResponse("공지사항 확인 부탁드립니다.", users.get(0), channels.get(3)),
//            new MessageResponse("다음 모임은 언제인가요?", users.get(4), channels.get(1)),
//            new MessageResponse("메일 보내드렸습니다.", users.get(2), users.get(0)),
//            new MessageResponse("새 프로젝트에 대해서 논의해보아요.", users.get(3), channels.get(4)),
//            new MessageResponse("다른 문의사항 있으면 알려주세요.", users.get(2), channels.get(0))
//        );
    }
}
