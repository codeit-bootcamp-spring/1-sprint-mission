

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

    public static void main(String[] args) {


        //------------------채널 도메인
        System.out.println("*채널 도메인 test");
        JCFChannelService jcfChannelService = new JCFChannelService();

        //채널 생성
        Channel channel = new Channel("ch1");
        jcfChannelService.createChannel(channel);
        //System.out.println(jcfChannelService.readChannel(channel.getChId()).getChName());;

        //새로운 채널 추가
        Channel channel2 = new Channel("ch2");
        jcfChannelService.createChannel(channel2);

        //채널 명 변경
        jcfChannelService.modifyChannel(channel.getChId(), "ch12");
        //System.out.println(jcfChannelService.readChannel(channel.getChId()).getChName());;

        //모두 읽기
        List<Channel> channelList = jcfChannelService.readAllChannel();
        System.out.println("모든 채널을 출력합니다.");
        for(Channel ch:channelList){
            System.out.println(ch.getChName());
        }
        
        //채널 삭제
        jcfChannelService.deleteChannel(channel2.getChId());
        //모두 읽기
        System.out.println("모든 채널을 출력합니다.");
        for(Channel ch:channelList){
            System.out.println(ch.getChName());
        }



        ///------------유저 도메인
        ///

        System.out.println("\n------------\n*유저 도메인 test");
        User user1 = new User("효정");
        User user2 = new User("고양이");
        User user3 = new User("강아지");

        JCFUserService jcfUserService = new JCFUserService();

        //유저 등록
        jcfUserService.createUser(user1);
        jcfUserService.createUser(user2);
        jcfUserService.createUser(user3);


        //유저 조회 (readUser메서드는 jcfUserService의 modify, delete 등 메서드에서 사용됨
        System.out.println("유저 조회: " + jcfUserService.readUser(user1.getUserId()).getUserName());

        //전체 조회
        System.out.println("전체 유저를 조회합니다.");
        for(User user: jcfUserService.readAllUser()){
            System.out.println(user.getUserName());
        }

        //유저 수정
        jcfUserService.modifyUser(user1.getUserId(), "효정이");

        //수정 조회
        System.out.println("유저 조회: " +jcfUserService.readUser(user1.getUserId()).getUserName());

        //삭제
        jcfUserService.deleteUser(user1.getUserId());

        //전체 조회
        System.out.println("전체 유저를 조회합니다.");
        for(User user: jcfUserService.readAllUser()){
            System.out.println(user.getUserName());
        }



        /////////메시지 테스트

        System.out.println("\n------------\n*메시지 도메인 test");
        Message message1= new Message(user3, "안녕하세요");
        Message message2= new Message(user2, "야옹야옹");
        Message message3 = new Message(user3,"반가워요~");
        Message message4 = new Message(user3,"잘지내요~");

        //user-메시지 매핑  -> 이걸 메시지 생성 때 자동화하면 좋을것같음.
        user3.addMessage(message1);
        user3.addMessage(message3);
        user3.addMessage(message4);
        user2.addMessage(message2);

        JCFMessageService jcfMessageService = new JCFMessageService();

        //메시지 등록
        jcfMessageService.createMessage(message1);
        jcfMessageService.createMessage(message2);
        jcfMessageService.createMessage(message3);
        jcfMessageService.createMessage(message4);

        //메시지 조회
        System.out.println( jcfMessageService.readMessage(message1.getMsgId()).getUser().getUserName() + ": "
                + jcfMessageService.readMessage(message1.getMsgId()).getContent());
        //메시지 모두 조회
        System.out.println("메시지를 모두 조회합니다.");
        for (Message msg : jcfMessageService.readAllMessage()) {
            System.out.println( msg.getUser().getUserName() + ": "
                    +  msg.getContent());
        }

        //메시지 수정
        jcfMessageService.modifyMessage(message4.getMsgId(), "잘지내요~ 멍멍" );

        //메시지 삭제
        jcfMessageService.deleteMessage(message3.getMsgId() );

        //메시지 모두 조회
        System.out.println("메시지를 모두 조회합니다.");
        for (Message msg : jcfMessageService.readAllMessage()) {
            System.out.println( msg.getUser().getUserName() + ": "
                    +  msg.getContent());
        }


        ////////////////////////////서비스 간 상호작용


        //채널에 유저 추가
        jcfChannelService.addUser(channel, user1);
        jcfChannelService.addUser(channel, user2);


        System.out.println("\n\n");
        //심화?
        //채널의 유저들의 메시지를 시간순으로 출력하기.
        for(Channel ch : jcfChannelService.readAllChannel()){
            System.out.println("++채널: " + ch.getChName()+ "의 모든 채팅 출력 ");

            // 해당 채널에 속한 유저들 가져오기
            List<User> channelUsers = jcfUserService.readAllUser(); // 채널에 속한 유저 목록을 가져온다고 가정

            for (User user : channelUsers) {
                System.out.print(user.getUserName() + ", ");
            }
            System.out.println("님이 접속중입니다.");

            // 유저들에 대한 메시지 리스트
            List<Message> allMessages = new ArrayList<>();

            // 각 유저의 메시지를 모두 모은다
            for (User user : channelUsers) {
                List<Message> userMessages = user.getMsgList(); // 유저가 작성한 메시지를 가져옴.
                allMessages.addAll(userMessages);
            }

//            // 메시지들을 시간순으로 정렬
            allMessages.sort(Comparator.comparing(Message::getCreatedAt)); //

            // 정렬된 메시지 출력
            for (Message msg : allMessages) {
                System.out.println(msg.getCreatedAt() + " - " + msg.getUser().getUserName() + ": " + msg.getContent());
            }
        }
        


    }
}
