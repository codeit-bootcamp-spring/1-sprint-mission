package com.sprint.misson;

import com.sprint.misson.discordeit.dto.ChannelDTO;
import com.sprint.misson.discordeit.dto.UserDTO;
import com.sprint.misson.discordeit.entity.*;
import com.sprint.misson.discordeit.factory.JCFServiceFactory;
import com.sprint.misson.discordeit.factory.ServiceFactory;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JavaApplication {

    public static void main(String[] args) {

        //서비스간 의존성 구현을 팩토리 패턴을 사용하여 구현
        // 팩토리 패턴의 장점
        // 1. 객체 생성 로직을 한 곳에서 관리 (관심사 분리)
        // 2. 의존성 주입을 명확하게 제어
        // 3. 객체 생성 방식을 쉽게 변경 가능
        // 4. 테스트가 용이함 (Mock 객체 주입 등이 쉬워짐)
        // 5. 싱글톤 인스턴스 관리가 쉬워짐

        ServiceFactory serviceFactory = new JCFServiceFactory();

        UserService userService = serviceFactory.createUserService();
        ChannelService channelService = serviceFactory.createChannelService();
        MessageService messageService = serviceFactory.createMessageService();


        //1. 등록
        System.out.println("=== 1. 등록 테스트 ===");

        System.out.println("--- User 등록---");
        User user1 = userService.createUser("코드잇", "codeit@codeit.co.kr");
        User user2 = userService.createUser("박유진", "yudility@gmail.com");
        User user3 = userService.createUser("홍길동", "gildong@naver.com");

        System.out.println("> 유저 생성 결과 및 전체 유저 목록: ");
        userService.getUsers().forEach(User::displayShortInfo);

        //예외 - 중복
        System.out.println("\n* 중복 이메일 케이스");
        System.out.println("- 닉네임: \"중복\", 이메일: \"codeit@codeit.co.kr\"로 생성 시도");
        System.out.println("> 실행 결과: ");

        try {
            User user4 = userService.createUser("중복", "codeit@codeit.co.kr");
            user4.displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n> 전체 유저 목록: ");
        userService.getUsers().forEach(User::displayShortInfo);


        System.out.println("\n\n--- Channel 등록---");
        Channel channel1 = channelService.CreateChannel("일반1", ChannelType.TEXT);
        Channel channel2 = channelService.CreateChannel("일반2", ChannelType.TEXT);
        Channel channel3 = channelService.CreateChannel("공지사항", ChannelType.TEXT);
        Channel channel4 = channelService.CreateChannel("회의실", ChannelType.VOICE);

        System.out.println("\n> 채널 생성 결과 및 전체 채널 목록:");
        channelService.getChannels().forEach(Channel::displayShortInfo);

        System.out.println("\n* 채널에 유저 추가 및 삭제: ");

        System.out.println();
        System.out.println("- " + channel1.getChannelName() + " 채널에 " + user1.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1, user1));
        System.out.println("- " + channel1.getChannelName() + " 채널에 " + user2.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1, user2));
        System.out.println("- " + channel1.getChannelName() + " 채널에 " + user3.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1, user3));
        channel1.displayShortInfo();

        System.out.println();
        System.out.println("- " + channel2.getChannelName() + " 채널에 " + user1.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel2, user1));
        System.out.println("- " + channel2.getChannelName() + " 채널에 " + user2.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel2, user2));
        channel2.displayShortInfo();

        System.out.println();
        System.out.println("- " + channel3.getChannelName() + " 채널에 " + user1.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3, user1));
        System.out.println("- " + channel3.getChannelName() + " 채널에 " + user2.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3, user2));
        System.out.println("- " + channel3.getChannelName() + " 채널에 " + user3.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3, user3));
        channel3.displayShortInfo();

        System.out.println();
        System.out.println("- " + channel4.getChannelName() + " 채널에 " + user1.getNickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel4, user1));
        channel4.displayShortInfo();


        System.out.println("\n\n--- Massage 등록---");

        Message msg1 = messageService.createMessage(user1, "일반1 채널입니다!", channel1);
        Message msg2 = messageService.createMessage(user3, "반갑습니다!", channel1);
        Message msg3 = messageService.createMessage(user1, "일반2 채널입니다.", channel2);
        Message msg4 = messageService.createMessage(user2, "공지사항 채널입니다! 반갑습니다.", channel3);
        Message msg5 = messageService.createMessage(user1, "회의실 채널입니다.", channel4);

        System.out.println("> 메시지 생성 결과 및 전체 목록:");
        messageService.getMessages().forEach(Message::displayShortInfo);

        System.out.println("\n\n* 심화 - 메세지 예외 처리");

        System.out.println("\n1) DB에 존재하지 않는 유저로 메세지 생성 ");
        User notExistUser = new User("없는유저", "noOne@test.com", UserStatus.ACTIVE, "hello", AccountStatus.UNVERIFIED);
        System.out.println("- 테스트 유저");
        notExistUser.displayShortInfo();
        System.out.println("\n> 결과: ");
        try {
            messageService.createMessage(notExistUser, "can not be created. The user is not exist.", channel1).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n2) DB에 존재하지 않는 Channel로 메세지 생성 ");
        Channel notExistChannel = new Channel("없는채널", ChannelType.TEXT);
        System.out.println("- 테스트 채널");
        notExistChannel.displayShortInfo();
        System.out.println("\n> 결과: ");
        try {
            messageService.createMessage(user1, "can not be created. The channel is not exist.", notExistChannel).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) DB에 존재하지 않는 User와 Channel로 메세지 생성 ");
        System.out.println("- 테스트 채널 및 유저(위 테스트 케이스 사용)");
        System.out.println("\n> 결과: ");
        try {
            messageService.createMessage(notExistUser, "can not be created. The channel and the user is not exist.", notExistChannel).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n=== 2. 조회 테스트 ===");

        System.out.println("\n--- User 조회 ---");

        System.out.println("\n1) 전체 목록 조회");
        List<User> tempUsers = userService.getUsers();

        System.out.println("> 조회 결과: ");
        tempUsers.forEach(User::displayShortInfo);

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForTest = tempUsers.get(0).getId().toString();

        System.out.println("\n- 조회할 UUID를 가진 유저가 있는 경우: ");
        System.out.println("> 조회할 UUID: " + uuidStringForTest);
        System.out.println("\n> 조회 결과:");
        userService.getUserByUUID(uuidStringForTest).displayShortInfo();

        System.out.println("\n- 조회할 UUID를 가진 유저가 없는 경우: ");
        UUID testUUID = UUID.randomUUID();
        System.out.println("> 조회할 UUID: " + testUUID);
        System.out.println("\n> 조회 결과:");
        try {
            userService.getUserByUUID(testUUID.toString()).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) 이메일로 조회(단건): ");
        System.out.println("\n- 조회할 이메일을 가진 유저가 있는 경우: ");
        System.out.println("> codeit@codeit.co.kr을 이메일로 가진 유저 조회 결과: ");
        userService.getUserByEmail("codeit@codeit.co.kr").displayShortInfo();

        System.out.println("\n- 조회할 이메일을 가진 유저가 없는 경우");
        System.out.println("> no_one@test.com을 이메일로 가진 유저 조회 결과: ");
        try {
            userService.getUserByEmail("no_one@test.com").displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n4) 닉네임으로 조회(다건): ");
        System.out.println("> 닉네임에 \"박유진\"이 포함된 User 조회 결과: ");
        userService.getUsersByNickname("박유진").forEach(User::displayShortInfo);

        System.out.println("\n5) 유저 상태로 조회(다건): ");
        System.out.println("- default 는 ACTIVE 이므로 홍길동을 INACTIVE로 변경");
        UserDTO userDTO = new UserDTO();
        userDTO.setUserStatus(UserStatus.INACTIVE);
        userService.updateUser(user3.getId().toString(), userDTO);

        System.out.println("> 유저 상태에 활동중(ACTIVE)인 User 조회 결과: ");
        userService.getUserByUserStatus(UserStatus.ACTIVE).forEach(User::displayShortInfo);

        System.out.println("\n\n--- Channel 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<Channel> tempChannels = channelService.getChannels();
        tempChannels.forEach(Channel::displayShortInfo);

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForChannelTest = tempChannels.get(0).getId().toString();

        System.out.println("- 조회할 UUID를 가진 채널이 있는 경우: ");
        System.out.println("> 조회할 UUID: " + uuidStringForChannelTest);
        System.out.println("\n> 조회 결과: ");
        channelService.getChannelByUUID(uuidStringForChannelTest).displayShortInfo();

        System.out.println("- 조회할 UUID를 가진 채널이 없는 경우: ");
        UUID testUUID2 = UUID.randomUUID();
        System.out.println("> 조회할 UUID: " + testUUID2);
        System.out.println("\n> 조회 결과:");
        try {
            channelService.getChannelByUUID(testUUID2.toString()).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println("\n3) 채널 이름으로 조회(다건)");
        System.out.println("> 채널 이름에 \"일반\"이 포함된 채널 조회 결과:");
        channelService.getChannelsByName("일반").forEach(Channel::displayShortInfo);


        System.out.println("\n4) 채널 type(종류)으로 조회(다건)");
        System.out.println("> Channal Type이 TEXT인 채널 조회 결과: ");
        channelService.getChannelByType(ChannelType.TEXT).forEach(Channel::displayShortInfo);

        System.out.println("\n5) 채널에 참여중인 유저 목록 조회");
        System.out.println("> channel1에 참여 중인 유저 목록 조회 결과:");
        channelService.getUsersInChannel(channel1).forEach(u -> System.out.println(u.getId()));

        System.out.println("\n--- Message 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<Message> messageList = messageService.getMessages();
        messageList.forEach(Message::displayShortInfo);


        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForMessageTest = messageList.get(0).getId().toString();

        System.out.println("- 조회할 UUID를 가진 메세지가 있는 경우: ");
        System.out.println("> 조회할 UUID: " + uuidStringForMessageTest);
        System.out.println("> 조회 결과\n");
        messageService.getMessageByUUID(uuidStringForMessageTest).displayShortInfo();

        System.out.println("- 조회할 UUID를 가진 메세지가 없는 경우: ");
        UUID testUUID3 = UUID.randomUUID();
        System.out.println("> 조회할 UUID: " + testUUID3);
        System.out.println("> 조회 결과\n");
        try {
            messageService.getMessageByUUID(testUUID3.toString()).displayShortInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) 내용으로 조회(다건)");
        System.out.println("> 내용에 \"반갑습니다\"가 포함된 메세지 조회: ");
        messageService.getMessageByContent("반갑습니다").forEach(Message::displayShortInfo);


        System.out.println("\n4) 작성자로 조회(다건)");
        System.out.println("> user2(name=박유진)이 작성한 메세지 조회: ");
        messageService.getMessageBySender(user2).forEach(Message::displayShortInfo);

        //todo
        //없는 유저로 조회하는 경우

        System.out.println("\n5)특정 채널에 속하는 메세지 조회(다건)");
        System.out.println("> \"일반1\" 채널의 전체 메세지 목록:");
        messageService.getMessagesByChannel(channel1).forEach(Message::displayShortInfo);

        // 3. 수정 테스트
        System.out.println("\n\n=== 3. 수정 테스트 ===");

        System.out.println("\n--- User 수정 ---");
        System.out.println("\n-> User1의 이름을 '코드잇'에서 'codeit'으로 수정: ");
        user1.displayFullInfo();

        UserDTO userDto = new UserDTO();
        userDto.setNickname("codeit");

        try {
            System.out.println("\n 수정 결과: ");
            userService.updateUser(user1.getId().toString(), userDto).displayFullInfo();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n--- Channel 수정 ---");
        System.out.println("\n-> channel1의 이름을 '일반1'에서 '수다방'으로 수정: ");
        channel1.displayFullInfo();

        ChannelDTO channelDTO = new ChannelDTO();
        channelDTO.setChannelName("수다방");

        channelService.updateChannel(channel1.getId().toString(), channelDTO);

        System.out.println("\n-> 수정 결과:");
        channel1.displayFullInfo();

        System.out.println("\n\n--- Message 수정 ---");
        System.out.println("\n -> msg1 내용을 '일반1 채널입니다!'에서 '수다방 채널입니다!'로 수정 ");
        msg1.displayFullInfo();

        messageService.updateMessage(msg1.getId().toString(), "수다방 채널입니다!");

        System.out.println("\n -> 수정 결과:");
        msg1.displayFullInfo();

        // 4. 삭제 테스트
        System.out.println("\n\n=== 4. 삭제 테스트 ===");

        //의존 관계를 고려하여 역순으로 진행
        System.out.println("\n--- Message 삭제 ---");
        System.out.println("\n -> 이름이 \"홍길동\"인 user가 작성한 첫번째 메세지 삭제 ");

        System.out.println("\n> 전체 메세지 목록: ");
        messageService.getMessages().forEach(Message::displayShortInfo);

        User senderMsg = userService.getUsersByNickname("홍길동").get(0);
        Message msgToDelete = messageService.getMessageBySender(senderMsg).get(0);
        System.out.println("\n-> Message 삭제 결과: " + messageService.deleteMessage(msgToDelete));

        System.out.println("\n> 삭제 후 메세지 목록: ");
        messageService.getMessages().forEach(Message::displayShortInfo);

        //삭제 시에도 유저, 채널 없을 때 예외 보여줘야? -> 애초에 조회가 되어야 삭제가 되니 이거는 빼는걸로

        System.out.println("\n--- Channel 삭제 ---");
        System.out.println("\n -> 채널 이름에 \"일반\"이 들어간 채널 삭제");

        System.out.println("\n> 전체 채널 목록:");
        channelService.getChannels().forEach(Channel::displayShortInfo);

        List<Channel> normalChannels = channelService.getChannelsByName("일반");
        normalChannels.forEach(c -> System.out.println("\n-> 채널 \"" + c.getChannelName() + "\" 삭제 결과: " + channelService.deleteChannel(c)));

        System.out.println("\n삭제 후 채널 목록: ");
        channelService.getChannels().forEach(Channel::displayShortInfo);

        System.out.println("\n--- User 삭제 ---");
        System.out.println("\n -> email이 \"gildong@naver.com\"인 User 삭제");

        System.out.println("\n> 전체 유저 목록 조회: ");
        userService.getUsers().forEach(User::displayShortInfo);

        User userFoundByEmail = userService.getUserByEmail("gildong@naver.com");
        System.out.println("\n-> User 삭제 결과: ");
        System.out.println(userService.deleteUser(userFoundByEmail.getId().toString()));

        System.out.println("\n> 삭제 후 유저 목록: ");
        userService.getUsers().forEach(User::displayShortInfo);

    }
}

