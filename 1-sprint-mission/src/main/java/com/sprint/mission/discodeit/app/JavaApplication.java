package com.sprint.mission.discodeit.app;



import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {
//        FileUserRepository userRepository = new FileUserRepository();
//        FileChannelRepository channelRepository = new FileChannelRepository();
//        FileMessageRepository messageRepository = new FileMessageRepository();
//
//        // 서비스 계층에 주입
//        FileUserService userService = FileUserService.getInstance(userRepository);
//        FileChannelService channelService = FileChannelService.getInstance(channelRepository);
//        FileMessageService messageService = FileMessageService.getInstance(messageRepository,channelRepository, userRepository);
//
//        //user 객체 생성, user 생성
//        System.out.println(" <<<< 유저 생성 >>>> ");
//
//        User user1 = userService.create(, "강병훈", , "dkdkdk@");
//        User user2 = userService.create(, "John", , "dddas@");
//        User user3 = userService.create(, "Bob", , "xxxx@ccc.com");
//        System.out.println();
//        List<User> users = userService.findAll();
//        for (User user : users) {
//            System.out.println(user.getEmail());
//        }
//
//        //사용자 업데이트
//        userService.update(, user1.getId(), , "kbh");
//        //사용자 삭제
//        userService.delete(user3.getId());
//        System.out.println();
//        // 사용자 출력
//        System.out.println(" <<<< All Users >>>>");
//        List<User> updateUsers = userService.findAll();
//        for (User user : updateUsers) {
//            System.out.println(user);
//        }
//        System.out.println();
//
//        //채널 생성
//        System.out.println(" <<<< 채널 생성 >>>> ");
//        Channel channel1 = channelService.create(user1, "공지", "공지 입니다");
//        Channel channel2 = channelService.create(user2, "공지1", "공지 입니다1");
//
//        // 채널 조회
//        System.out.println(" <<<< All Channels >>>>");
//        List<Channel>channels  = channelService.findAllByUserId();
//        for (Channel channel : channels) {
//            System.out.println(channel.getName());
//        }
//        //채널 업데이트
//        channelService.update(channel1);
//        //채널 삭제
//        channelService.delete(channel1);
//        System.out.println(" <<<< All Channels >>>>");
//        //수정된 채널 조회
//        List<Channel> channelNew = channelService.findAllByUserId();
//        for (Channel channel : channelNew) {
//            System.out.println(channel.getName());
//        }
//
//        System.out.println(" <<<< 메세지 생성 >>>> ");
//        //메세지 생성
//        Message messageUser1 = messageService.create(user1);
//        Message messageUser2 = messageService.create(user1);
//        Message messageUser3 = messageService.create(user1);
//        //메세지 조회
//        System.out.println(" <<<< All Messages >>>>");
//        List<Message> messages = messageService.findAll();
//        for (Message message : messages) {
//            System.out.println(message.getContent());
//        }
//
//        messageService.delete(messageUser1);
//        messageService.update(user1);
//
//        System.out.println(" <<<< All Messages >>>>");
//        List<Message> newMessage = messageService.findAll();
//        for (Message message : newMessage) {
//            System.out.println(message.getContent());
//        }
    }
}
