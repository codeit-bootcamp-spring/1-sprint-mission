import discodeit.entity.Channel;
import discodeit.entity.Message;
import discodeit.entity.User;
import discodeit.service.jcf.JCFChannelService;
import discodeit.service.jcf.JCFMessageService;
import discodeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {
    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();

        // 1. User
        System.out.println("*** User Test ***");
        System.out.println();

        // 1.1. User 등록
        System.out.println("* User 등록 확인");
        User userYB = new User("이유빈", "yubin@gmail.com", "010-1234-1234");
        userService.create(userYB);
        System.out.println("[등록 성공] User ID: " + userYB.getUserId() + " / User Name: " + userYB.getUserName());

        User userMJ = new User("김민지", "minji@naver.com", "010-2345-2345");
        userService.create(userMJ);
        System.out.println("[등록 성공] User ID: " + userMJ.getUserId() + " / User Name: " + userMJ.getUserName());

        User userYH = new User("김영희", "younghee@hanmail.net", "010-9999-9999");
        userService.create(userYH);
        System.out.println("[등록 성공] User ID: " + userYH.getUserId() + " / User Name: " + userYH.getUserName());

        // 1.2. User 전화번호 중복
        System.out.println("\n* User 등록 시 전화번호 중복 오류");
        try {
            User userTest = new User("홍길동", "gildong@gmail.com", "010-1234-1234");
            userService.create(userTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 1.3. User e-mail 형식 오류
        System.out.println("\n* User 등록 시 e-maiil 형식 오류");
        try {
            User userTest = new User("홍길동", "gil.dong@codeit.com", "010-3456-3456");
            userService.create(userTest);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 1.4. 유저 모두 읽기
        System.out.println("\n* User 모두 읽기");
        userService.readAll().stream()
                .map(User::getUserName)
                .sorted()
                .forEach(username -> System.out.print(username + " "));
        System.out.println();

        // 1.5. 유저 수정
        System.out.println("\n* User 수정");
        User newUserYB = new User("이유빈", "yubin@codeit.com", "010-1111-1111");
        User updateUserYB = userService.update(userYB.getUserId(), newUserYB);
        System.out.println("[수정 성공] User ID: " + updateUserYB.getUserId() + " / User Name: " + updateUserYB.getUserName()
                + " / User e-mail: " + updateUserYB.getEmail() + " / User phone Number: " + updateUserYB.getPhoneNum());

        // 1.6. 유저 삭제
        System.out.println("\n* User 삭제");
        userService.delete(userMJ.getUserId());
        System.out.println("* 삭제된 User 목록 확인");
        userService.readAll().stream()
                .map(User::getUserName)
                .sorted()
                .forEach(username -> System.out.print(username + " "));
        System.out.println();

        // 1.7. 유저 삭제 불가
        System.out.println("\n* User 삭제 불가 (존재하지 않는 User)");
        try {
            System.out.println("- User '김민지' 삭제 시도");
            userService.delete(userMJ.getUserId());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
        System.out.println();


        // 2. Channel
        System.out.println("*** Channel Test ***");
        System.out.println();

        // 2.1. 채널 생성
        System.out.println("* Channel 생성 확인");
        Channel codeitCh = new Channel("Codeit!!!");
        channelService.create(codeitCh);
        System.out.println("[생성 성공] Channel ID: " + codeitCh.getChannelId() + " / Channel Name: " + codeitCh.getChannelName());

        Channel groupCh = new Channel("Group 1 Channel");
        channelService.create(groupCh);
        System.out.println("[생성 성공] Channel ID: " + groupCh.getChannelId() + " / Channel Name: " + groupCh.getChannelName());

        // 2.2. 채널에 User 추가
        codeitCh.addUser(userYB);
        codeitCh.addUser(userYH);
        groupCh.addUser(userYB);

        // 2.3. 채널 User 확인
        System.out.println("\nChannel User 확인");
        System.out.println("- Codeit Channel User 확인");
        List<User> codeitChUserList = channelService.getUserList(codeitCh.getChannelId());
        codeitChUserList.stream().map(User::getUserName).forEach(System.out::println);

        System.out.println("- Group 1 Channel User 확인");
        List<User> groupChUserList = channelService.getUserList(groupCh.getChannelId());
        groupChUserList.stream().map(User::getUserName).forEach(System.out::println);

        // 2.4. 채널 User 삭제
        System.out.println("\nChannel User 삭제");
        channelService.deleteUser(codeitCh.getChannelId(), userYH);
        System.out.println("- Codeit Channel User 확인");
        List<User> codeitChUserList2 = channelService.getUserList(codeitCh.getChannelId());
        codeitChUserList2.stream().map(User::getUserName).forEach(System.out::println);

        // 2.5. 채널 읽기 (Channel ID 지정)
        System.out.println("\n* Codeit Channel만 읽기");
        Channel readCodeitCh = channelService.readById(codeitCh.getChannelId());
        System.out.println("Channel ID: " + readCodeitCh.getChannelId() + " / Channel Name: " + readCodeitCh.getChannelName());

        // 2.6. 채널 이름 수정
        System.out.println("\n* Channel Name 수정");
        Channel newCodeitCh = new Channel("Codeit Mission 1");
        Channel updateCodeitCh = channelService.update(codeitCh.getChannelId(), newCodeitCh);
        System.out.println("[수정 성공] Channel ID: " + updateCodeitCh.getChannelId() + " / Channel Name: " + updateCodeitCh.getChannelName());

        // 2.7. 채널 이름 수정 불가
        System.out.println("\n* Channel Name 수정 불가");
        System.out.println("- 'Codeit Mission 1' 채널을 'Group 1 Channel'로 변경 시도");
        Channel newCodeitCh2 = new Channel("Group 1 Channel");
        try {
            Channel updateCodeitCh2 = channelService.update(codeitCh.getChannelId(), newCodeitCh2);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 2.8. 채널 삭제
        System.out.println("\n* Channel 삭제");
        System.out.println("- 현재 채널 목록 확인");
        channelService.readAll().stream()
                        .map(Channel::getChannelName)
                .sorted()
                .forEach(System.out::println);

        channelService.deleteChannel(groupCh.getChannelId());
        System.out.println("- 채널 목록 확인");
        channelService.readAll().stream()
                .map(Channel::getChannelName)
                .sorted()
                .forEach(System.out::println);

        System.out.println();
        System.out.println();


        // 3. Message
        System.out.println("*** Message Test ***");
        System.out.println();

        // 3.1. 메세지 생성
        System.out.println("* 매세지 생성 확인");
        Message message1 = new Message(userYH, userYB, codeitCh, "안녕!!!!!!!");
        messageService.create(message1);
        System.out.println("[생성 성공] 발신자: " + message1.getSendUser().getUserName()
                + " -> 수신자: "+ message1.getReceiveUser().getUserName()
                + "\n내용: " + message1.getMessageDetail());

        Message message2 = new Message(userYB, userYH, codeitCh, "Hello Hello");
        messageService.create(message2);
        System.out.println("\n[생성 성공] 발신자: " + message2.getSendUser().getUserName()
                + " -> 수신자: "+ message2.getReceiveUser().getUserName()
                + "\n내용: " + message2.getMessageDetail());

        // 3.2. 메세지 확인
        System.out.println("\n* 메세지 전부 확인");
        messageService.readAll().stream().map(Message::getMessageDetail).forEach(System.out::println);

        // 3.3. 메세지 채널 확인
        System.out.println("\n* 메세지 채널 확인");
        System.out.println("- 첫번째 메세지가 전송된 채널: " + messageService.getChannel(message1.getMessageId()).getChannelName());
        System.out.println("- 두번째 메세지가 전송된 채널: " + messageService.getChannel(message2.getMessageId()).getChannelName());

        // 3.4. 메세지 수정
        System.out.println("\n* 메세지 수정");
        Message newMessage = new Message(userYH, userYB, codeitCh, "안녕...");
        Message updateMessage = messageService.updateMessage(message1.getMessageId(), newMessage);
        System.out.println("수정된 메세지: " + updateMessage.getMessageDetail());

        // 3.5. 메세지 삭제
        System.out.println("\n* 메세지 삭제");
        System.out.println("- 현재 메세지 목록 확인");
        messageService.readAll().stream().map(Message::getMessageDetail).forEach(System.out::println);
        System.out.println("- '안녕...' 메세지 삭제");
        messageService.delete(message1.getMessageId());
        System.out.println("- 메세지 목록 확인");
        messageService.readAll().stream().map(Message::getMessageDetail).forEach(System.out::println);
    }
}
