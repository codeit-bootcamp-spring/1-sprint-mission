package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;

public class TestMain {
    public static void main(String[] args) {
        JcfUserService jcfUserService = new JcfUserService();
//        FileUserService fileUserService = new FileUserService();
        JcfMessageService jcfMessageService = new JcfMessageService();
        JcfChannelService jcfChannelService = new JcfChannelService();

        User alice = new User("Alice", "alice@example.code");
        User hyun = new User("Hyun", "hyun@example.code");
        User yull = new User("Yull", "yull@example.code");
        jcfUserService.createUser(alice);
        jcfUserService.createUser(hyun);
        jcfUserService.createUser(yull);
       /* fileUserService.createUser(alice);
        fileUserService.createUser(hyun);
        fileUserService.createUser(yull);*/
        System.out.println("일부 사용자 조회:" + jcfUserService.findUser(hyun.getId()));
//        System.out.println("일부 사용자 조회:" + fileUserService.findUser(hyun.getId()));
        System.out.println("전체 사용자 조회: " + jcfUserService.findAllUsers());
//        System.out.println("전체 사용자 조회: " + fileUserService.findAllUsers());
        System.out.println();

        jcfUserService.updateUserName(alice.getId(),"AliceUpdate");
        jcfUserService.updateUserEmail(alice.getId(),"alice_update@example.com");
        System.out.println("업데이트 후 사용자 조회: " + jcfUserService.findAllUsers());
//        System.out.println("업데이트 후 사용자 조회: " + fileUserService.findAllUsers());

        jcfUserService.deleteUser(alice.getId());
//        fileUserService.deleteUser(alice.getId());
        System.out.println("삭제 후 사용자 조회: " + jcfUserService.findAllUsers());
//        System.out.println("삭제 후 사용자 조회: " + fileUserService.findAllUsers());
        System.out.println();

        Channel hiChannel = new Channel("Hi", "Introduce");
        Channel howChannel = new Channel("How", "How about u?");
        Channel byeChannel = new Channel("Bye", "Good Day");
        jcfChannelService.createChannel(hiChannel);
        jcfChannelService.createChannel(byeChannel);
        jcfChannelService.createChannel(howChannel);
        System.out.println("전체 채널 조회: " + jcfChannelService.findAllChannels());
        System.out.println("일부 채널 조회: " + jcfChannelService.findChannel(hiChannel.getId()));
        System.out.println();

        jcfChannelService.updateChannelDescription(hiChannel.getId(),"hiUpdate");
        hiChannel.updateDescription("Update Introduce");
        System.out.println("업데이트 후 채널 조회: " + jcfChannelService.findAllChannels());

        jcfChannelService.deleteChannel(byeChannel.getId());
//        jcfChannelService.deleteChannel(hiChannel.getId());
        System.out.println("삭제 후 채널 조회: " + jcfChannelService.findAllChannels());
        System.out.println();

        Message howMessage = new Message("Good", yull.getId(), howChannel.getId());
//        Message hiMessage = new Message("Hi I'm alice", alice.getId(), hiChannel.getId());
        Message hiMessage = new Message("Hi I'm hyun", hyun.getId(), hiChannel.getId());
        jcfMessageService.messageSave(howMessage);
        jcfMessageService.messageSave(hiMessage);
        System.out.println("전체 메세지 조회: " + jcfMessageService.findAllMessages());
        System.out.println("일부 메세지 조회:" + jcfMessageService.findMessage(howMessage.getId()));
        System.out.println();

        jcfMessageService.updateMessage(howMessage.getId(),"How Update");
        System.out.println("업데이트 후 메세지 조회: " + jcfMessageService.findAllMessages());

        jcfMessageService.deleteMessage(hiMessage.getId());
        System.out.println("삭제 후 메세지 조회: " + jcfMessageService.findAllMessages());
    }
}
