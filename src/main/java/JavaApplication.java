
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JavaApplication {
    public static JCFChannelService jcfChannelService = new JCFChannelService();
    public static JCFUserService jcfUserService = new JCFUserService();
    public static JCFMessageService jcfMessageService = new JCFMessageService();



    public static void main(String[] args) {


        //------------------채널 도메인
        System.out.println("*채널 도메인 test");

        //채널 생성
        Channel channel1 = jcfChannelService.createChannel("ch1");
        Channel channel2 = jcfChannelService.createChannel("ch2");

        //채널 명 변경
        jcfChannelService.modifyChannel(channel1.getChId(), "ch12");

        //모두 읽기
        List<Channel> channelList = jcfChannelService.readAllChannel();
        System.out.println("모든 채널을 출력합니다.");
        for (Channel ch : channelList) {
            System.out.println(" " + ch.getChannelName());
        }

        //채널 삭제
        jcfChannelService.deleteChannel(channel2.getChId());
        //모두 읽기
        System.out.println("모든 채널을 출력합니다.");
        for (Channel ch : channelList) {
            System.out.println(" " + ch.getChannelName());
        }


        ///------------유저 도메인
        ///

        System.out.println("\n------------\n*유저 도메인 test");
        User user1 = jcfUserService.createUser("효정");
        User user2 = jcfUserService.createUser("고양이");
        User user3 = jcfUserService.createUser("강아지");


        //유저 조회 (readUser메서드는 jcfUserService의 modify, delete 등 메서드에서 사용됨
        System.out.println("유저 조회: " + jcfUserService.readUser(user1.getUserId()).getUserName());

        //전체 조회
        System.out.println("전체 유저를 조회합니다.");
        for (User user : jcfUserService.readAllUser()) {
            System.out.println(" " + user.getUserName());
        }

        //유저 수정
        jcfUserService.modifyUser(user3.getUserId(), "강아지2");


        //삭제
        jcfUserService.deleteUser(user1.getUserId());

        //전체 조회
        System.out.println("전체 유저를 조회합니다.");
        for (User user : jcfUserService.readAllUser()) {
            System.out.println(" " + user.getUserName());
        }


        /////////메시지 테스트

        System.out.println("\n------------\n*메시지 도메인 test");
        Message message1 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChId(),  "안녕하세요");
        Message message2 = jcfMessageService.createMessage(user2.getUserId(), channel1.getChId(), "야옹야옹");
        Message message3 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChId(), "반가워요~");
        Message message4 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChId(), "잘지내요~");




        //메시지 조회
        System.out.println(jcfMessageService.readMessage(message1.getMsgId()).getUserID() + ": " + jcfMessageService.readMessage(message1.getMsgId()).getContent());
        //메시지 모두 조회
        System.out.println("메시지를 모두 조회합니다.");
        for (Message msg : jcfMessageService.readAllMessage()) {
            System.out.println(" " + msg.getUserID() + ": " + msg.getContent());
        }

        //메시지 수정
        jcfMessageService.modifyMessage(message4.getMsgId(), "잘지내요~ 멍멍");

        //메시지 삭제
        jcfMessageService.deleteMessage(message3.getMsgId());

        //메시지 모두 조회
        System.out.println("메시지를 모두 조회합니다.");
        for (Message msg : jcfMessageService.readAllMessage()) {
            System.out.println(" " + msg.getUserID() + ": " + msg.getContent());
        }


        ////////////////////////////서비스 간 상호작용



        System.out.println("\n\n");
        //심화?
        //채널의 유저들의 메시지를 시간순으로 출력하기.
        for (Channel ch : jcfChannelService.readAllChannel()) {
            System.out.println("++채널: " + ch.getChannelName() + "의 모든 채팅 출력 ");

            // 해당 채널에 속한 유저들 가져오기
            List<User> channelUsers = jcfUserService.readAllUser(); // 채널에 속한 유저 목록을 가져온다고 가정

            for (User user : channelUsers) {
                System.out.print(user.getUserName() + ", ");
            }
            System.out.println("님이 접속중입니다.");

        }


    }
}
