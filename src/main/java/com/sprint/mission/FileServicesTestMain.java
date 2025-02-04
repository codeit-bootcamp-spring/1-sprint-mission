package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.FileServiceFactory;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.factory.ServiceFactory;

import java.util.List;
import java.util.UUID;

public class FileServicesTestMain {
    public static void main(String[] args) {
        //factory 폴더를 만들어도 되는 지 몰라서 일단 discodit 아래에 두었습니다.

        ServiceFactory factory = new FileServiceFactory();
        FileUserService userService = (FileUserService) factory.getUserService();
        FileChannelService channelService = (FileChannelService) factory.getChannelService();
        FileMessageService messageService = (FileMessageService) factory.getMessageService();

        UUID user1 = userService.createUser("Yang");
        UUID user2 = userService.createUser("Kim");
        UUID user3 = userService.createUser("Lee");
        UUID user4 = userService.createUser("Han");

        UUID channel1 = channelService.createChannel("SBS");
        UUID channel2 = channelService.createChannel("KBS");
        UUID channel3 = channelService.createChannel("MBC");



        System.out.println("====================================================");
        System.out.println("유저 단건 조회");
        System.out.println(userService.getUser(user1));
        System.out.println("====================================================");
        System.out.println("유저 다건 조회");
        List<User> users = userService.getUsers();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("====================================================");
        System.out.println("유저 수정");
        userService.updateUser(user1, "Park");
        System.out.println(userService.getUser(user1));
        System.out.println("====================================================");
        //userService.fetchMap();
        //System.out.println("fetchMap 실행");
        System.out.println("유저 목록 출력");
        users = userService.getUsers();
        for (User user : users) {
            System.out.println(user);
        }
        System.out.println("====================================================");
        System.out.println("유저 삭제");
        userService.deleteUser(user2);
        users = userService.getUsers();
        users.forEach(System.out::println);

        System.out.println("====================================================");
        System.out.println("채널 단건 조회");
        System.out.println(channelService.getChannel(channel1));
        System.out.println("====================================================");
        System.out.println("채널 다건 조회");
        List<Channel> channels = channelService.getChannels();
        for (Channel channel : channels) {
            System.out.println(channel);
        }
        System.out.println("====================================================");
        System.out.println("채널 수정");
        System.out.println(channelService.getChannel(channel2));
        channelService.updateChannel(channel2, "EBS");// KBS->EBS
        System.out.println(channelService.getChannel(channel2));
        System.out.println("====================================================");
        System.out.println("채널 메시지 등록");

        UUID message1 = channelService.addMessage_By_Channel(channel1, user4, "hello");
        System.out.println(message1 + " 등록완료");
        UUID message2 = channelService.addMessage_By_Channel(channel1, user3, "world");
        System.out.println(message2 + " 등록완료");
        UUID message3 = channelService.addMessage_By_Channel(channel1, user1, "super");
        System.out.println(message3 + " 등록완료");
        UUID message4 = channelService.addMessage_By_Channel(channel2, user3, "mario");
        System.out.println(message4 + " 등록완료");

        messageService.getMessages().forEach(System.out::println);
        System.out.println("====================================================");

        System.out.println("채널 메시지 다건 출력 ");
        List<Message> messages = channelService.messages(channel1);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println("====================================================");

        System.out.println("채널 메시지 삭제 / 메시지가 삭제되면 메시지를 가지고 있던 채널 속 메시지의 데이터도 삭제된다.");
        messageService.deleteMessage(message1);
        channelService.messages(channel1).forEach(System.out::println);

        System.out.println("====================================================");
        System.out.println("채널 삭제 시 메시지 처리");
        channelService.deleteChannel(channel1);
        messages = channelService.messages(channel1);
        System.out.println("channel1's messages = " + messages);
        System.out.println("====================================================");
        System.out.println("메시지 내용 변경");
        System.out.println("메지시 변경 이전 -> "+ messageService.getMessage(message4));
        messageService.updateMessage(message4, "Luigi");
        System.out.println("메지시 변경 이후 -> "+messageService.getMessage(message4));
        System.out.println("====================================================");
        System.out.println("유저 이름으로 메시지 확인");
        List<Message> messageByUserId = messageService.getMessagesByUserId(user3);
        System.out.println("messageByUserId = " + messageByUserId);
        System.out.println("====================================================");
        System.out.println("유저 삭제 시 메시지 삭제");
        System.out.println("user3 = " + userService.getUser(user3));
        userService.deleteUser(user3);
        messageByUserId = messageService.getMessagesByUserId(user3);
        System.out.println("messageByUserId = " + messageByUserId);
        System.out.println("====================================================");

    }

}
