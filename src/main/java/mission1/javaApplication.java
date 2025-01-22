package mission1;

import mission1.entity.Channel;
import mission1.entity.Message;
import mission1.entity.User;
import mission1.service.jcf.ChannelService;
import mission1.service.jcf.MessageService;
import mission1.service.jcf.UserService;
import mission1.service.jcf.jcf.JCFChannelService;
import mission1.service.jcf.jcf.JCFMessageService;
import mission1.service.jcf.jcf.JCFUserService;


import java.util.List;

public class javaApplication {

    private static final UserService userService = new JCFUserService();
    private static final ChannelService channelService = new JCFChannelService();
    private static final MessageService messageService = new JCFMessageService();

    public static void main(String[] args) {
        System.out.println("=====================USER START=====================");
        // 회원 등록
        System.out.println("******USER 등록******");
        System.out.println("......");
        User userA = userService.create(new User("닉네임 A", "abcd"));
        User userB = userService.create(new User("닉네임 B", "dcba"));
        System.out.printf("******USER %d명 등록******", userService.findAll().size());
        System.out.println();
        System.out.println();

        // 회원 조회
        System.out.println("******USER 단건 조회******");
        User user = userService.find(userA.getId());
        System.out.println("userA의 닉네임은 = " + user.getName());
        System.out.println();

        // 회원 리스트
        System.out.println("******USER 다건 조회******");
        List<User> all = userService.findAll();
        System.out.println("유저 리스트 = " + all);
        System.out.println();

        // 회원 수정 : 닉네임-비번 동시 수정 => id, 새로운 닉네임, 패스워드 필요
        // 비번은 중복 허용
        System.out.println("******USER 수정******");
        userService.update(userA.getId(), "닉네임 A+", "aaaa");
        // 수정된 데이터 조회
        System.out.println("userA의 닉네임 = " + userA.getName());
        System.out.println("userA의 비밀번호 = " + userA.getPassword());
        System.out.println();

        // 삭제 => id, 닉네임, 비밀번호 전부 일치해야 삭제 가능
        System.out.println("******USER 삭제******");
        System.out.println("삭제될 계정의 닉네임 = " + userB.getName());
        userService.delete(userB.getId(), userB.getName(), userB.getPassword());
        System.out.println("알림 : 삭제 완료");

        // 삭제 잘 됐는지 조회
        List<User> all2 = userService.findAll();
        System.out.println("유저 리스트 = " + all2);
        System.out.println();

        System.out.println("=====================USER FINISH=====================");
        System.out.println();
        System.out.println("=====================CHANNEL START=====================");

        // 등록
        System.out.println("******Channel 등록******");
        channelService.create("서버 1");
        channelService.create("서버 2");
        System.out.println(".....");
        System.out.printf("******Channel %d 개 등록 완료******", channelService.findAll().size());
        System.out.println();
        System.out.println();

        // 단건 조회
        System.out.println("******Channel 단건 조회******");
        System.out.println("'서버 1'(이)라는 서버 찾기 => " + channelService.findByName("서버 1"));
        System.out.println("'서버 2'(이)라는 서버 찾기 => " + channelService.findByName("서버 2"));
        System.out.println();

        // 다건 조회
        System.out.println("******Channel 다건 조회******");
        System.out.println("채널 목록 = " + channelService.findAll());
        System.out.println();

        // 수정
        System.out.println("******Channel 수정******");
        Channel updateServer = channelService.update("서버 1", "서버 1-1");
        System.out.println("채널 목록 = " + channelService.findAll());
        System.out.println("UpdateServer = " + updateServer);
        System.out.println();

        // 수정된 데이터 조회
        System.out.println("******Channel 수정 조회******");
        System.out.println("FindByName(\"서버 1-1\") = " + channelService.findByName("서버 1-1"));
        System.out.println();

        // 삭제
        System.out.println("******Channel 삭제******");
        String deleteName = "서버 2";
        System.out.println("서버 명 : '" + deleteName + "' 삭제 완료 ");
        channelService.delete(deleteName);
        System.out.println("채널 목록 = " + channelService.findAll());
        System.out.println();

        System.out.println("=====================CHANNEL FINISH=====================");
        System.out.println();
        System.out.println("=====================MESSAGE START=====================");

        // 등록
        System.out.println("******MESSAGE 등록******");
        Message message1 = messageService.create("스프린트 미션 중");
        System.out.println(".....");
        Message message2 = messageService.create("스프린트 거의 끝");
        System.out.printf("******MESSAGE %d 개 등록 완료******", messageService.findAll().size());
        System.out.println();
        System.out.println();

        // 단건 조회
        System.out.println("******MESSAGE 단건 조회******");
        System.out.println("'스프린트 미션 중'(이)라는 메시지 찾기 => " + messageService.findMessage("스프린트 미션 중"));
        System.out.println("'스프린트 거의 끝'(이)라는 메시지 찾기 => " + messageService.findMessage("스프린트 거의 끝"));
        System.out.println();

        // 다건 조회
        System.out.println("******MESSAGE 다건 조회******");
        System.out.println("메시지 목록 = " + messageService.findAll());
        System.out.println();

        // 수정
        System.out.println("******MESSAGE 수정******");
        String updateComment = "스프린트 미션 끝";
        Message updateMessage = messageService.update(message2.getId(), updateComment);
        System.out.println("메시지 목록 = " + messageService.findAll());
        System.out.println("UpdateMessage = " + updateMessage);
        System.out.println();

        // 수정된 데이터 조회
        System.out.println("******MESSAGE 수정 조회******");
        System.out.println("FindByMessage(\"스프린트 미션 끝\") = " + messageService.findMessage(updateComment));
        System.out.println();

        // 삭제
        System.out.println("******MESSAGE 삭제******");
        System.out.println("현재 메시지 목록 = " + messageService.findAll());
        String deleteMessage = "스프린트 미션 중";
        Message message = messageService.findMessage(deleteMessage);
        messageService.delete(message.getId(), deleteMessage);
        System.out.println("삭제할 메시지 : '" + deleteMessage + "' 삭제 완료 ");
        System.out.println("메시지 목록 = " + messageService.findAll());
        System.out.println();
        System.out.println("=====================MESSAGE FINISH=====================");
    }
}
