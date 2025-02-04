
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JavaApplication {


    public static void main(String[] args) {

        JCFChannelService jcfChannelService = new JCFChannelService();
        JCFUserService jcfUserService = new JCFUserService();
        JCFMessageService jcfMessageService = new JCFMessageService();



        //file* Service 테스트

        FileChannelService fileChannelService = new FileChannelService();
        FileUserService fileUserService= new FileUserService();
        FileMessageService fileMessageService = new FileMessageService();

        Channel channel1 = fileChannelService.createChannel("ch_fileIO_1");
        Channel channel3 = fileChannelService.createChannel("ch_fileIO_3");
        Channel channel2=  fileChannelService.readChannel(channel1.getChannelId());


        System.out.println(channel1.getChannelId());
        System.out.println(channel2.getChannelId());

        System.out.println(channel2.getChannelName());

        User user1 = fileUserService.createUser("user1");

        Message message1 = fileMessageService.createMessage(user1.getUserId(), channel1.getChannelId(),"message1");



        FileChannelRepository fileChannelRepository = new FileChannelRepository();
        fileChannelRepository.save(channel1);

//        //------------------채널 도메인
//        System.out.println("*채널 도메인 test");
//
//        //채널 생성
//        Channel channel1 = jcfChannelService.createChannel("ch1");
//        //Channel channel2 = jcfChannelService.createChannel("");
//
//        //null/공백 방어 테스트
//        try {
//            // 예시로 잘못된 채널명으로 생성
//            Channel channel = jcfChannelService.createChannel("");  // 빈 문자열을 넣어 예외 발생
//        } catch (IllegalArgumentException e) {
//            System.out.println("채널 생성 실패: " + e.getMessage());
//        } catch (RuntimeException e) {
//            System.out.println("오류 발생: " + e.getMessage());
//        }
//        //채널 명 변경
//        jcfChannelService.modifyChannel(channel1.getChannelId(), "ch12");
//
//        //모두 읽기
//        List<Channel> channelList = jcfChannelService.readAllChannel();
//        System.out.println("모든 채널을 출력합니다.");
//        for (Channel ch : channelList) {
//            System.out.println(" " + ch.getChannelName());
//        }
////
////        //채널 삭제
////        jcfChannelService.deleteChannel(channel2.getChannelId());
//        //모두 읽기
//        System.out.println("모든 채널을 출력합니다.");
//        for (Channel ch : channelList) {
//            System.out.println(" " + ch.getChannelName());
//        }
//
//
//        ///------------유저 도메인
//        ///
//
//        System.out.println("\n------------\n*유저 도메인 test");
//        User user1 = jcfUserService.createUser("효정");
//        User user2 = jcfUserService.createUser("고양이");
//        User user3 = jcfUserService.createUser("강아지");
//
//
//        //유저 조회 (readUser메서드는 jcfUserService의 modify, delete 등 메서드에서 사용됨
//        System.out.println("유저 조회: " + jcfUserService.readUser(user1.getUserId()).getUserName());
//
//        //전체 조회
//        System.out.println("전체 유저를 조회합니다.");
//        for (User user : jcfUserService.readAllUser()) {
//            System.out.println(" " + user.getUserName());
//        }
//
//        //유저 수정
//        jcfUserService.modifyUser(user3.getUserId(), "강아지2");
//
//
//        //삭제
//        jcfUserService.deleteUser(user1.getUserId());
//
//        //전체 조회
//        System.out.println("전체 유저를 조회합니다.");
//        for (User user : jcfUserService.readAllUser()) {
//            System.out.println(" " + user.getUserName());
//        }
//
//
//        /////////메시지 테스트
//
//        System.out.println("\n------------\n*메시지 도메인 test");
//        Message message1 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChannelId(), "안녕하세요");
//        Message message2 = jcfMessageService.createMessage(user2.getUserId(), channel1.getChannelId(), "야옹야옹");
//        Message message3 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChannelId(), "반가워요~");
//        Message message4 = jcfMessageService.createMessage(user3.getUserId(), channel1.getChannelId(), "잘지내요~");
//
//
//        //메시지 조회
//        System.out.println(jcfMessageService.readMessage(message1.getMsgId()).getUserID() + ": " + jcfMessageService.readMessage(message1.getMsgId()).getContent());
//        //메시지 모두 조회
//        System.out.println("메시지를 모두 조회합니다.");
//        for (Message msg : jcfMessageService.readAllMessage()) {
//            System.out.println(" " + msg.getUserID() + ": " + msg.getContent());
//        }
//
//        //메시지 수정
//        jcfMessageService.modifyMessage(message4.getMsgId(), "잘지내요~ 멍멍");
//
//        //메시지 삭제
//        jcfMessageService.deleteMessage(message3.getMsgId());
//
//        //메시지 모두 조회
//        System.out.println("메시지를 모두 조회합니다.");
//        for (Message msg : jcfMessageService.readAllMessage()) {
//            System.out.println(" " + msg.getUserID() + ": " + msg.getContent());
//        }



    }
        
}
