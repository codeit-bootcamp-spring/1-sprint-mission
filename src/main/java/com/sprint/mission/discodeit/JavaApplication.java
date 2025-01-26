package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;


import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        User user1 = userService.createUser("Alice", "aliceExample@naver.com");
        User user2 = userService.createUser("Alex", "alexExample@naver.com");
        User user3 = userService.createUser("Gray", "GrayExample@naver.com");
        User user4 = userService.createUser("Hazel", "HazelExample@naver.com");

        Channel channel1 = channelService.createChannel("Codeit", user1);
        Channel channel2 = channelService.createChannel("Study", user2);

        user1.addChannel(channel1);
        user4.addChannel(channel1);

        Message message1 = messageService.createMessage(channel1.getId(), user1.getId(), "오늘 수업 9시 시작이에요!");
        Message message2 = messageService.createMessage(channel1.getId(), user2.getId(), "오늘도 9시간 수업입니다!!");
        Message message3 = messageService.createMessage(channel2.getId(), user3.getId(), "저랑 같이 스터디 하실분?!");
        Message message4 = messageService.createMessage(channel2.getId(), user4.getId(), "저도 스터디 하고 싶어요!");

        //READ
        List<User> userList = userService.getAllUserList();
        System.out.println(userList);
        List<Channel>channelList = channelService.getAllChannelList();
        System.out.println(channelList);
        List<Message>messageList = messageService.getAllMessageList();
        System.out.println(messageList);
        //Update
        UUID userIdToUpdate = user1.getId();
        userService.updateUserName(userIdToUpdate, "Winter");
        userService.updateUserEmail(userIdToUpdate, "winterExample");
        userService.updateUserEmail(userIdToUpdate, "WinterExample@naver.com");
        User updateUser = userService.searchById(userIdToUpdate);
        if(updateUser != null) {
            System.out.println("수정된 유저 정보 : " + updateUser.toString());
        }

        UUID channelIdToUpdate = channel1.getId();
        channelService.updateChannelName(channelIdToUpdate, "Codeit_Sprint");
        Channel updateChannel = channelService.searchById(channelIdToUpdate);
        if (updateChannel != null) {
            System.out.println("수정된 채널 정보 : " + updateChannel.toString());
        }


        UUID messageIdToUpdate = message1.getId();
        messageService.updateMessage(messageIdToUpdate, "9시에 수업시작하면 QR 체크해주세요!");
        Message updateMessage = messageService.searchById(messageIdToUpdate);
        if (updateMessage != null) {
            System.out.println("수정된 메시지 : " + updateMessage.getContent());
        }
        //delete
        UUID userIdToDelete = user3.getId();
        userService.deleteUser(userIdToDelete);
        System.out.println(userService.getAllUserList());

        UUID channelIdToDelte = channel2.getId();
        channelService.deleteChannel(channelIdToDelte);
        System.out.println(channelService.getAllChannelList());

        UUID messageIdToDelete = message4.getId();
        messageService.deleteMessage(messageIdToDelete);
        System.out.println( messageService.getAllMessageList());

        System.out.println("======File=====");
        FileUserRepository userRepository = new FileUserRepository();
        FileChannelRepository channelRepository = new FileChannelRepository();
        FileMessageRepository messageRepository = new FileMessageRepository();

        userRepository.createUser("Alice" , "Alice1234@google.com");
        userRepository.createUser("Hazel" , "Hazel3455@google.com");
        channelRepository.createChannel("Codeit-Backend", user1);
        channelRepository.createChannel("SprintBoot", user4);
        messageRepository.createMessage(channel1.getId(),user1.getId(),"이 채널에서는 뭐하는거에요?");

        List<User> users = userRepository.getAllUserList();
        users.forEach(System.out::println);
        List<Channel> channels = channelRepository.getAllChannelList();
        channels.forEach(System.out::println);
        List<Message> messages = messageRepository.getAllMessageList();

        UUID userId = users.get(0).getId();
        userRepository.updateUserName(userId, "Alice Updated");
        userRepository.updateUserEmail(userId, "AliceUpdated@google.com");
        users.forEach(System.out::println);

        UUID channelId = channels.get(0).getId();
        channelRepository.updateChannelName(channelId, "Codeit-Spring");
        channels.forEach(System.out::println);

        UUID messageId = messages.get(0).getId();
        messageRepository.updateMessage(messageId,"이것 뭐에요?");
        messages.forEach(System.out::println);

        userRepository.deleteUser(userId);
        channelRepository.deleteChannel(channelId);
        messageRepository.deleteMessage(messageId);
    }
}