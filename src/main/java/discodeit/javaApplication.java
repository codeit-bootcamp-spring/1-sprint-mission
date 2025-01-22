package discodeit;

import discodeit.enity.Channel;
import discodeit.enity.Message;
import discodeit.enity.User;
import discodeit.service.ServiceFactory;
import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

import java.util.UUID;

public class javaApplication {
    public static void main(String[] args) {

        ServiceFactory serviceFactory = new ServiceFactory();
        JCFUserService jcfUserService = serviceFactory.getJcfUserService();
        JCFChannelService jcfChannelService = serviceFactory.getJcfchannelService();
        JCFMessageService jcfMessageService = serviceFactory.getJcfMessageService();

        //유저 등록
        System.out.println("==== Create User ====");
        UUID user1 = jcfUserService.createUser("Alice@gmail.com", "Alice");
        UUID user2 = jcfUserService.createUser("Bob@gmail.com", "Bob");
        UUID user3 = jcfUserService.createUser("Cindy@gmail.com", "Cindy");
        UUID user4 = jcfUserService.createUser("Dan@gmail.com", "Dan");
        UUID user5 = jcfUserService.createUser("Edward@gmail.com", "Edward");
        UUID user6 = jcfUserService.createUser("Felix@gmail.com", "felix");
        jcfUserService.createUser("Alice@gmail.com", "Alice");
        System.out.println("====================\n\n");

        System.out.println("==== Read User ====");
        //유저 다건 조회
        jcfUserService.viewAllUser();
        System.out.println();
        //유저 단건 조회
        jcfUserService.viewUserInfo(user1);
        System.out.println("====================\n\n");

        // 유저 수정
        System.out.println("==== Update User ====");
        jcfUserService.updateUserName(user1, "Andy");
        jcfUserService.updateUserEmail(user1, "Andy@gmail.com");
        jcfUserService.updateUserEmail(user1, "Bob@gmail.com");
        System.out.println();
        jcfUserService.viewAllUser();
        System.out.println("====================\n\n");

        // 유저 삭제
        System.out.println("==== Delete User ====");
        jcfUserService.deleteUser(user6);
        System.out.println();
        jcfUserService.viewAllUser();
        System.out.println();
        System.out.println("// test: 삭제된 유저 정보 조회 시도: ");
        jcfUserService.viewUserInfo(user6);
        System.out.println("====================\n\n");

        //채널 등록
        System.out.println("==== Create Channel ====");
        UUID channel1 = jcfChannelService.createChannel("채널 1");
        UUID channel2 = jcfChannelService.createChannel("채널 2");
        UUID channel3 = jcfChannelService.createChannel("채널 3");
        UUID channel4 = jcfChannelService.createChannel("채널 4");
        System.out.println("====================\n\n");

        //채널 다건 조회
        System.out.println("==== Read Channel ====");
        jcfChannelService.viewAllChannels();
        System.out.println();
        //채널 단건 조회
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");

        //채널 수정
        System.out.println("==== Update Channel ====");
        jcfChannelService.updateChannelName(channel1, "채널 11");
        // 채널에 유저 등록
        jcfChannelService.addUserIntoChannel(channel1, user1);
        jcfChannelService.addUserIntoChannel(channel1, user2);
        jcfChannelService.addUserIntoChannel(channel1, user3);
        jcfChannelService.addUserIntoChannel(channel1, user4);
        jcfChannelService.viewAllChannels();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfUserService.viewUserInfo(user1);
        //채널에서 유저 삭제
        jcfChannelService.deleteUserInChannel(channel1, user4);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        //유저 삭제 시 채널에서도 함께 삭제
        System.out.println("// test: 사용자 삭제 시 채널에서도 정보 삭제");
        jcfChannelService.addUserIntoChannel(channel1, user4);
        jcfChannelService.viewChannelInfo(channel1);
        jcfUserService.deleteUser(user4);
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");


        //채널 삭제
        System.out.println("==== Delete Channel ====");
        jcfChannelService.addUserIntoChannel(channel4, user1);
        jcfChannelService.addUserIntoChannel(channel4, user2);
        jcfChannelService.deleteChannel(channel4);
        jcfChannelService.viewAllChannels();
        System.out.println("\n//test: 삭제한 채널 정보 조회:");
        jcfChannelService.viewChannelInfo(channel4);
        jcfUserService.viewUserInfo(user1);
        jcfUserService.viewUserInfo(user2);
        System.out.println("====================\n\n");


        // 메시지 등록(전송)
        System.out.println("==== Create Message ====");
        UUID message1 = jcfMessageService.createMessage(user1, channel1, "안녕하세요");
        UUID message2 = jcfMessageService.createMessage(user1, channel1, "저는 Andy입니다.");
        UUID message3 = jcfMessageService.createMessage(user2, channel1, "반가워요 Andy.");
        UUID message4 = jcfMessageService.createMessage(user5, channel1, "아직 입장하지 않은 채널");
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println("====================\n\n");


        System.out.println("==== Read Message ====");
        System.out.println("//test: 메세지 단건 조회");
        // 메시지 단건 조회
        jcfMessageService.viewMessage(message1);
        System.out.println();
        // 메시지 다건 조회
        jcfChannelService.addUserIntoChannel(channel2, user1);
        jcfChannelService.addUserIntoChannel(channel2, user2);
        jcfChannelService.addUserIntoChannel(channel2, user3);
        UUID message5 = jcfMessageService.createMessage(user1, channel2, "채널 2 시작");
        UUID message6 = jcfMessageService.createMessage(user2, channel2, "안녕하세요.");
        UUID message7 = jcfMessageService.createMessage(user3, channel2, "채널 2는 무엇을 하는 채널인가요?");
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");

        System.out.println("==== Update Message ====");
        //메시지 수정
        jcfMessageService.updateMessage(user2, message1, "잘못된 작성자");
        jcfMessageService.updateMessage(user1, message1, "수정) 안녕하세요 여러분");
        jcfMessageService.viewMessage(message1);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");

        System.out.println("==== Delete Message ====");
        // 메시지 삭제
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.deleteMessage(message1);
        System.out.println();
        jcfChannelService.viewChannelInfo(channel1);
        System.out.println();
        jcfMessageService.viewAllMessages();
        System.out.println("====================\n\n");


        System.out.println("--- 유저 삭제 시 메세지 표시 ---");
        jcfUserService.deleteUser(user2);
        jcfChannelService.deleteUserInChannel(channel2, user3);
        jcfChannelService.viewChannelInfo(channel2);
        System.out.println();
    }
}
