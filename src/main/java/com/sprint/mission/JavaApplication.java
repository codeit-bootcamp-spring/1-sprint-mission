package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class JavaApplication {
    public static void main(String[] args) throws InterruptedException {
        UserService userService=new JCFUserService();
        ChannelService channelService=new JCFChannelService();
        MessageService messageService=new JCFMessageService();

        //등록
        System.out.println("등록");
        User usertest=new User("Alice","12345");
        userService.createUser(usertest);
        System.out.println(userService.getUserById(usertest.getId()).get().getName());
        long createdAtUser=userService.getUserById(usertest.getId()).get().getCreatedAt();
        System.out.println(formatDate(createdAtUser));


        Channel channeltest=new Channel("testChannel","test_description",usertest);
        channelService.createChannel(channeltest);
        System.out.println(channelService.getChannelById(channeltest.getId()).get().getChannel());
        long createdAtChannel=channelService.getChannelById(channeltest.getId()).get().getCreatedAt();
        System.out.println(formatDate(createdAtChannel));

        Message messagetset=new Message("test_message",usertest);
        messageService.createMessage(messagetset);
        System.out.println(messageService.getMessageById(messagetset.getId()).get().getContent());
        long createdAtMessage=messageService.getMessageById(messagetset.getId()).get().getCreatedAt();
        System.out.println(formatDate(createdAtMessage));
        System.out.println();

        //조회
        System.out.println("단일조회");
        Optional<User> userById = userService.getUserById(usertest.getId());
        System.out.println(userById.get().getName());
        Optional<Channel> channelById = channelService.getChannelById(channeltest.getId());
        System.out.println(channelById.get().getChannel()+" 채널 만든 유저 : "+channelById.get().getCreator());
        Optional<Message> messageById = messageService.getMessageById(messagetset.getId());
        System.out.println(messageById.get().getContent()+" 메세지 보낸 유저 : "+messageById.get().getSenderUser());
        System.out.println();


        //데이터 추가
        User usertest2=new User("Bob","12345");
        Channel channel2=new Channel("test2Channel","test_description",usertest2);
        Message message2=new Message("test_message2",usertest2);


        User usertest3=new User("James","12345");
        Channel channel3=new Channel("test3Channel","test_description",usertest3);
        Message message3=new Message("test_message3",usertest3);

        userService.createUser(usertest2);
        userService.createUser(usertest3);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);
        messageService.createMessage(message2);
        messageService.createMessage(message3);

        //전체 조회
        System.out.println("전체 조회");
        List<User> allUsers=userService.getAllUsers();
        System.out.println(allUsers);
        List<Channel> allChannels = channelService.getAllChannels();
        System.out.println(allChannels);
        List<Message> allMessages = messageService.getAllMessage();
        System.out.println(allMessages);
        System.out.println();

        //업데이트
        //딜레이
        Thread.sleep(2000);
        System.out.println("업데이트");

        //사용자 업데이트
        User updatedUser=new User("UpdatedAlice","1234567");
        userService.updateUser(usertest.getId(),updatedUser);

        //업데이트 사용자 확인
        System.out.println(userService.getUserById(usertest.getId()).get().getName());
        long updatedAtUser=userService.getUserById(usertest.getId()).get().getUpdatedAt();
        System.out.println(formatDate(updatedAtUser));

        //업데이트 채널 확인
        Channel updatedChannel=new Channel("updateChannel","update_test_description",updatedUser);
        channelService.updateChannel(channeltest.getId(),updatedChannel);
        System.out.println(channelService.getChannelById(channeltest.getId()).get().getChannel());
        long updatedAtChannel=channelService.getChannelById(channeltest.getId()).get().getUpdatedAt();
        System.out.println(formatDate(updatedAtChannel));

        //업데이트 메세지 확인
        Message updatedMessage=new Message("update_message",updatedUser);
        messageService.updateMessage(messagetset.getId(),updatedMessage);
        System.out.println(messageService.getMessageById(messagetset.getId()).get().getContent());
        long updatedAtMessage=messageService.getMessageById(messagetset.getId()).get().getUpdatedAt();
        System.out.println(formatDate(updatedAtMessage));
        System.out.println();

        System.out.println("업데이트 후 조회");
        Optional<User> updateUserById = userService.getUserById(usertest.getId());
        System.out.println(updateUserById.get().getName());
        Optional<Channel> updatChannelById = channelService.getChannelById(channeltest.getId());
        System.out.println(updatChannelById.get().getChannel()+" 채널 만든 유저 : "+updatChannelById.get().getCreator());
        Optional<Message> updateMessageById = messageService.getMessageById(messagetset.getId());
        System.out.println(updateMessageById.get().getContent()+" 메세지 보낸 유저 : "+updateMessageById.get().getSenderUser());
        System.out.println();

        //삭제
        System.out.println("삭제");
        userService.deleteUser(usertest.getId());
        channelService.deleteChannel(channelById.get().getId());
        messageService.deleteMessage(messageById.get().getId());

        System.out.println("삭제 후 전체 조회");
        allUsers=userService.getAllUsers();
        allChannels=channelService.getAllChannels();
        allMessages=messageService.getAllMessage();
        System.out.println(allUsers);
        System.out.println(allChannels);
        System.out.println(allMessages);
        System.out.println();
    }
    public static String formatDate(long createdAt) {
        Date creationDate = new Date(createdAt);  // long 값을 Date로 변환
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(creationDate);
    }
}
