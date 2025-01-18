package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;

public class TestMain {
    public static void main(String[] args) {
        JcfUserService jcfUserService = new JcfUserService();
        JcfMessageService jcfMessageService = new JcfMessageService();
        JcfChannelService jcfChannelService = new JcfChannelService();

        User alice = new User("Alice", "alice@example.code");
        User hyun = new User("Hyun", "hyun@example.code");
        User yull = new User("Yull", "yull@example.code");
        jcfUserService.createUser(alice);
        jcfUserService.createUser(hyun);
        jcfUserService.createUser(yull);
        System.out.println("일부 사용자 조회:" + jcfUserService.userList(hyun.getId()));
        System.out.println("전체 사용자 조회: " + jcfUserService.allUserList());
        System.out.println();

        alice.updateUserName("AliceUpdate");
        alice.updateUserEmail("alice_update@example.com");
        System.out.println("업데이트 후 사용자 조회: " + jcfUserService.allUserList());

//        jcfUserService.deleteUser(hyun.getId());
        jcfUserService.deleteUser(alice.getId());
        System.out.println("삭제 후 사용자 조회: " + jcfUserService.allUserList());
        System.out.println();

        Channel hiChannel = new Channel("Hi", "Introduce");
        Channel howChannel = new Channel("How", "How about u?");
        Channel byeChannel = new Channel("Bye", "Good Day");
        jcfChannelService.createChannel(hiChannel);
        jcfChannelService.createChannel(byeChannel);
        jcfChannelService.createChannel(howChannel);
        System.out.println("전체 채널 조회: " + jcfChannelService.allChannelList());
        System.out.println("일부 채널 조회: " + jcfChannelService.channelList(hiChannel.getId()));
        System.out.println();

        hiChannel.updateChannelName("hiUpdate");
        hiChannel.updateDescription("Update Introduce");
        System.out.println("업데이트 후 채널 조회: " + jcfChannelService.allChannelList());

//        jcfChannelService.deleteChannel(byeChannel.getId());
        jcfChannelService.deleteChannel(hiChannel.getId());
        System.out.println("삭제 후 채널 조회: " + jcfChannelService.allChannelList());
        System.out.println();

        Message howMessage = new Message("Good", yull.getId(), howChannel.getId());
//        Message hiMessage = new Message("Hi I'm alice", alice.getId(), hiChannel.getId());
        Message hiMessage = new Message("Hi I'm hyun", hyun.getId(), hiChannel.getId());
        jcfMessageService.messageSave(howMessage);
        jcfMessageService.messageSave(hiMessage);
        System.out.println("전체 메세지 조회: " + jcfMessageService.messageAllList());
        System.out.println("일부 메세지 조회:" + jcfMessageService.messageList(howMessage.getId()));
        System.out.println();

        howMessage.updateContent("How Update");
        System.out.println("업데이트 후 메세지 조회: " + jcfMessageService.messageAllList());

        jcfMessageService.deleteMessage(howMessage.getId());
        System.out.println("삭제 후 메세지 조회: " + jcfMessageService.messageAllList());
    }
}
