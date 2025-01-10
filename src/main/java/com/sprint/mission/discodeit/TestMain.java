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
        System.out.println("일부 사용자 조회:"+jcfUserService.userList(hyun.getId()));
        System.out.println("전체 사용자 조회: "+jcfUserService.allUserList());

        alice.updateUserName("AliceUpdate");
        alice.updateUserEmail("alice_update@example.com");
        System.out.println("전체 사용자 조회: "+jcfUserService.allUserList());

        jcfUserService.deleteUser(hyun.getId()); //alice.getId()로 하면 오류가 발생합니다. 어떻게 해결해야할지 모르겠습니다.
        System.out.println("전체 사용자 조회: "+jcfUserService.allUserList());

        Channel hiChannel= new Channel("Hi","Introduce");
        Channel howChannel=new Channel("How","How about u?");
        Channel byeChannel= new Channel("Bye","Good Day");
        jcfChannelService.createChannel(hiChannel);
        jcfChannelService.createChannel(byeChannel);
        jcfChannelService.createChannel(howChannel);
        System.out.println("전체 채널 조회: " + jcfChannelService.allChannelList());
        System.out.println("일부 채널 조회: " + jcfChannelService.channelList(hiChannel.getId()));

        hiChannel.updateChannelName("hiUpdate");
        hiChannel.updateDescription("Update Introduce");
        System.out.println("전체 채널 조회: " + jcfChannelService.allChannelList());

        jcfChannelService.deleteChannel(byeChannel.getId());//여기도 hiChannel로하면 오류가 나는데 업데이트를 했기 떄문일까요?
        System.out.println("전체 채널 조회: " + jcfChannelService.allChannelList());

        Message howMessage = new Message("Good", yull.getId(),howChannel.getId());
        Message hiMessage = new Message("Hi I'm alice", alice.getId(),hiChannel.getId());
        jcfMessageService.messageSave(howMessage);
        jcfMessageService.messageSave(hiMessage);
        System.out.println("전체 메세지 조회: " + jcfMessageService.messageAllList());
        System.out.println("일부 메세지 조회:"+jcfMessageService.messageList(howMessage.getId()));

        howMessage.updateContent("How Update");
        System.out.println("전체 메세지 조회: " + jcfMessageService.messageAllList());

        jcfMessageService.deleteMessage(howMessage.getId()); //여긴 hiMessage로 하면 오류가 납니다.
        System.out.println("전체 메세지 조회: " + jcfMessageService.messageAllList());
    }
}
