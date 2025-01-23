package discodeit;

import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;
import discodeit.service.jcf.JCFServiceFactory;

public class JavaApplication {
    public static void main(String[] args) {

        // 서비스 객체 초기화
        ServiceFactory serviceFactory = JCFServiceFactory.getInstance();
        UserService jcfUserService = serviceFactory.getUserService();
        ChannelService jcfChannelService = serviceFactory.getChannelService();
        MessageService jcfMessageService = serviceFactory.getMessageService();

        // 유저 등록 후 조회
        User user1 = jcfUserService.createUser("user1", "user1@codeit.com", "010-1234-5678", "qwer1234");
        User user2 = jcfUserService.createUser("user2", "user2@codeit.com", "010-5678-1234", "qwer1234");
        User user3 = jcfUserService.createUser("user3", "user3@codeit.com", "010-4321-8765", "qwer1234");

        System.out.println("***** 유저1, 2, 3 조회 *****");
        System.out.println(jcfUserService.getInfo(user1) + System.lineSeparator());
        System.out.println(jcfUserService.getInfo(user2) + System.lineSeparator());
        System.out.println(jcfUserService.getInfo(user3) + System.lineSeparator());

        // 유저 정보 수정 후 조회
//        jcfUserService.updateName(user3, "유저3");

        System.out.println("***** 유저3 이름 수정 *****");
        System.out.println(jcfUserService.getInfo(user3) + System.lineSeparator());

        // 유저1이 채널 등록
        Channel channel1 = jcfChannelService.createChannel("channel1", "user1의 채널입니다.", user1);

        // 유저2, 유저3 채널 가입
        jcfChannelService.updateParticipants(channel1, user2);
        jcfChannelService.updateParticipants(channel1, user3);

        // 채널 조회
        System.out.println("***** 유저1의 채널에 유저2, 3이 가입 *****");
        System.out.println(jcfChannelService.getInfo(channel1) + System.lineSeparator());

        // 채널에 메시지 보낸 후 채널 조회
        Message message1 = jcfMessageService.createMessage(channel1, "안녕하세요~", user1);
        Message message2 = jcfMessageService.createMessage(channel1, "방장입니다!", user1);
        Message message3 = jcfMessageService.createMessage(channel1, "user2라고 합니다.", user2);
        Message message4 = jcfMessageService.createMessage(channel1, "잘 부탁하빈다. ^^", user3);

        System.out.println("***** 채널에 메시지 보낸 후 조회 *****");
        System.out.println(jcfChannelService.getInfo(channel1) + System.lineSeparator());

        // 메시지 수정, 삭제 후 채널 조회
        jcfMessageService.deleteMessage(message2, channel1, user1);
        jcfMessageService.updateContent(message4, "잘 부탁합니다. ^^");

        System.out.println("***** 유저1 메시지 삭제, 유저3 메시지 수정 *****");
        System.out.println(jcfChannelService.getInfo(channel1) + System.lineSeparator());

        // 유저 삭제 후 채널 조회 (참가자 목록에서 제거 되었는지 확인)
        // 채널 정보 수정
        jcfUserService.deleteUser(user2);
        jcfChannelService.updateIntroduction(channel1, "채널 설명 수정했습니다.", user1);

        System.out.println("***** 유저2 회원 탈퇴, 채널 소개 수정 *****");
        System.out.println(jcfChannelService.getInfo(channel1) + System.lineSeparator());

        // 채널 삭제
        jcfChannelService.deleteChannel(channel1, user1);

        System.out.println("***** 채널 삭제 후 유저들의 참여 채널 목록에서 정상 삭제 여부 조회 *****");
        System.out.println(jcfUserService.getInfo(user1) + System.lineSeparator());
        System.out.println(jcfUserService.getInfo(user3));
    }
}
