package discodeit;

import discodeit.dto.ChannelInfoDto;
import discodeit.dto.UserInfoDto;
import discodeit.enity.ChannelType;
import discodeit.repository.file.FileChannelRepository;
import discodeit.repository.file.FileMessageRepository;
import discodeit.repository.file.FileUserRepository;
import discodeit.service.ChannelService;
import discodeit.service.MessageService;
import discodeit.service.ServiceFactory;
import discodeit.service.UserService;
import discodeit.service.basic.BasicChannelService;
import discodeit.service.basic.BasicMessageService;
import discodeit.service.basic.BasicUserService;
import discodeit.service.file.FileServiceFactory;
import discodeit.service.jcf.JCFServiceFactory;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class javaApplication {

    static void printUserInfo(UUID userId, UserService userService) {
        System.out.println("--- UUID로 사용자 조회 ---");
        UserInfoDto userInfoDto = userService.getUserInfoById(userId);
        if (userInfoDto != null) {
            System.out.println(userInfoDto);
        } else {
            System.out.println("정보 조회가 불가능합니다.");
        }
    }

    static void printAllUserInfo(UserService userService) {
        System.out.println("--- 전체 사용자 조회 ---");
        List<UserInfoDto> allUserDtoList = userService.getAllUsersInfo();
        allUserDtoList.stream()
                .forEach(System.out::println);
    }

    static void printChannelInfo(UUID channelId, ChannelService channelService) {
        System.out.println("--- UUID로 특정 채널 조회 ---");
        ChannelInfoDto channelInfoDto = channelService.getChannelInfoById(channelId);
        if (channelInfoDto != null) System.out.println(channelInfoDto.fullInfoToString());
        else System.out.println("정보 조회가 불가능합니다.");
    }

    static void printAllChannelInfo(ChannelService channelService) {
        System.out.println("--- 전체 채널 조회 ---");
        List<ChannelInfoDto> allChannelDtoList = channelService.getAllChannelsInfo();
        allChannelDtoList.stream()
                .forEach(System.out::println);
    }

    static void printMessageInfo(UUID messageId, MessageService messageService) {
        System.out.println("--- UUID로 특정 메세지 조회 ---");
        // 메시지 단건 조회
        String msg = messageService.getMessageById(messageId);
        if (msg != null) System.out.println(msg);
        else System.out.println("정보 조회가 불가능합니다.");
    }

    static void printAllMessageInfo(MessageService messageService) {
        System.out.println("--- 디스코드잇에서 작성된 모든 메세지 조회 ---");
        List<String> messageList = messageService.getAllMessages();
        messageList.stream()
                .forEach(System.out::println);
    }

    static void testSprint1() {
        ServiceFactory jcfServiceFactory = new JCFServiceFactory();
        UserService jcfUserService = jcfServiceFactory.getUserService();
        ChannelService jcfChannelService = jcfServiceFactory.getChannelService();
        MessageService jcfMessageService = jcfServiceFactory.getMessageService();

        //유저 등록
        System.out.println("==== Create User ====");
        UUID user1Id = jcfUserService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2Id = jcfUserService.createUser("Bob@gmail.com", "Bob", "12345");
        UUID user3Id = jcfUserService.createUser("Cindy@gmail.com", "Cindy", "12345");
        UUID user4Id = jcfUserService.createUser("Dan@gmail.com", "Dan", "12345");
        UUID user5Id = jcfUserService.createUser("Edward@gmail.com", "Edward", "12345");
        UUID user6Id = jcfUserService.createUser("Felix@gmail.com", "felix", "12345");
        System.out.println("// 이메일 중복 검사 확인");
        jcfUserService.createUser("Alice@gmail.com", "Alice", "12345");
        System.out.println("// 비밀번호 형식 검사 확인");
        jcfUserService.createUser("Gary@gmail.com", "Gary", "1234");
        System.out.println("====================\n\n");

        System.out.println("==== Read User ====");
        //유저 단건 조회
        printUserInfo(user1Id, jcfUserService);
        //유저 다건 조회
        printAllUserInfo(jcfUserService);
        System.out.println("====================\n\n");


        // 유저 수정
        System.out.println("==== Update User ====");
        System.out.println("// 이름과 이메일 변경");
        jcfUserService.updateUserName(user1Id, "Andy");
        jcfUserService.updateUserEmail(user1Id, "Andy@gmail.com");
        System.out.println("// 이미 존재하는 이메일로 바꾸려는 경우");
        jcfUserService.updateUserEmail(user1Id, "Bob@gmail.com");
        System.out.println("// 수정 후 전체 사용자 조회");
        printAllUserInfo(jcfUserService);
        System.out.println("====================\n\n");

        // 유저 삭제
        System.out.println("==== Delete User ====");
        jcfUserService.deleteUser(user6Id);
        System.out.println("// 삭제 후 전체 사용자 조회");
        printAllUserInfo(jcfUserService);
        System.out.println("// 삭제된 유저 정보 조회 시도: ");
        printUserInfo(user6Id, jcfUserService);
        System.out.println("====================\n\n");

        //채널 등록
        System.out.println("==== Create Channel ====");
        UUID channel1 = jcfChannelService.createChannel("채널 1", "첫 번째 채널", ChannelType.PUBLIC);
        UUID channel2 = jcfChannelService.createChannel("채널 2", "두 번째 채널", ChannelType.PUBLIC);
        UUID channel3 = jcfChannelService.createChannel("채널 3", "세 번째 채널", ChannelType.PUBLIC);
        UUID channel4 = jcfChannelService.createChannel("채널 4", "네 번째 채널", ChannelType.PUBLIC);
        System.out.println("====================\n\n");

        System.out.println("==== Read Channel ====");
        printChannelInfo(channel1, jcfChannelService);
        printAllChannelInfo(jcfChannelService);
        System.out.println("====================\n\n");

        //채널 수정
        System.out.println("==== Update Channel ====");
        System.out.println("// 채널 이름 변경");
        jcfChannelService.updateChannelName(channel1, "채널 11");
        // 채널에 유저 등록
        System.out.println("// 채널에 유저 입장");
        jcfChannelService.addUserIntoChannel(channel1, user1Id);
        jcfChannelService.addUserIntoChannel(channel1, user2Id);
        jcfChannelService.addUserIntoChannel(channel1, user3Id);
        jcfChannelService.addUserIntoChannel(channel1, user4Id);
        System.out.println("// 채널에 유저 입장시킨 후 채널 정보 조회");
        printAllChannelInfo(jcfChannelService);
        System.out.println("--- 채널 정보 조회 ---");
        printChannelInfo(channel1, jcfChannelService);
        System.out.println("// 유저 정보 조회 시에도 채널 목록이 출력되는지 확인");
        printUserInfo(user1Id, jcfUserService);
        System.out.println("//채널에 유저 퇴장시킨 후 채널 정보 조회");
        //채널에서 유저 삭제
        jcfChannelService.deleteUserInChannel(channel1, user4Id);
        printChannelInfo(channel1, jcfChannelService);
        //유저 삭제 시 채널에서도 함께 삭제
        System.out.println("// 사용자 삭제 시 채널에서도 정보 삭제");
        System.out.println("// 삭제 전 채널에 입장");
        jcfChannelService.addUserIntoChannel(channel1, user4Id);
        printChannelInfo(channel1, jcfChannelService);
        jcfUserService.deleteUser(user4Id);
        System.out.println("// 삭제 후");
        printChannelInfo(channel1, jcfChannelService);
        System.out.println("====================\n\n");

        //채널 삭제
        System.out.println("==== Delete Channel ====");
        jcfChannelService.addUserIntoChannel(channel4, user1Id);
        jcfChannelService.addUserIntoChannel(channel4, user2Id);
        jcfChannelService.deleteChannel(channel4);
        System.out.println("// 채널 삭제 후 전체 채널 목록 조회");
        printAllChannelInfo(jcfChannelService);
        System.out.println("// 삭제한 채널 정보 조회: 채널 정보 조회가 불가능한지 확인");
        printChannelInfo(channel4, jcfChannelService);
        System.out.println("// 사용자 정보 조회 시에도 채널이 안 나오는지 확인");
        printUserInfo(user1Id, jcfUserService);
        printUserInfo(user2Id, jcfUserService);
        System.out.println("====================\n\n");


        // 메시지 등록(전송)
        System.out.println("==== Create Message ====");
        UUID message1 = jcfMessageService.createMessage(user1Id, channel1, "안녕하세요");
        UUID message2 = jcfMessageService.createMessage(user1Id, channel1, "저는 Andy입니다.");
        UUID message3 = jcfMessageService.createMessage(user2Id, channel1, "반가워요 Andy.");
        System.out.println("// 유저가 입장하지 않은 채널에 메세지를 전송하려는 경우");
        UUID message4 = jcfMessageService.createMessage(user5Id, channel1, "아직 입장하지 않은 채널");
        System.out.println("====================\n\n");

        System.out.println("==== Read Message ====");
        System.out.println("// 채널에 메세지가 작성되었는지 확인");
        printChannelInfo(channel1, jcfChannelService);
        System.out.println("// UUID로 특정 메세지 단건 조회");
        // 메시지 단건 조회
        printMessageInfo(message1, jcfMessageService);
        printMessageInfo(message2, jcfMessageService);
        // 메시지 다건 조회
        printAllMessageInfo(jcfMessageService);
        System.out.println("// 메세지 추가 후 확인");
        jcfChannelService.addUserIntoChannel(channel2, user1Id);
        jcfChannelService.addUserIntoChannel(channel2, user2Id);
        jcfChannelService.addUserIntoChannel(channel2, user3Id);
        UUID message5 = jcfMessageService.createMessage(user1Id, channel2, "채널 2 시작");
        UUID message6 = jcfMessageService.createMessage(user2Id, channel2, "안녕하세요.");
        UUID message7 = jcfMessageService.createMessage(user3Id, channel2, "채널 2는 무엇을 하는 채널인가요?");
        printAllMessageInfo(jcfMessageService);
        System.out.println("====================\n\n");
//
        System.out.println("==== Update Message ====");
        //메시지 수정
        System.out.println("// 메세지 작성자가 아닌 다른 유저가 수정하려는 경우");
        jcfMessageService.updateMessage(user2Id, message1, "잘못된 작성자");
        jcfMessageService.updateMessage(user1Id, message1, "수정) 안녕하세요 여러분");
        System.out.println("// 메세지 수정 후 확인");
        System.out.println(jcfMessageService.getMessageById(message1));
        System.out.println("// 메세지 수정 후 소속 채널에 반영됐는지 확인");
        printChannelInfo(channel1, jcfChannelService);
        printAllMessageInfo(jcfMessageService);
        System.out.println("====================\n\n");

        System.out.println("==== Delete Message ====");
        // 메시지 삭제
        System.out.println("// 메세지 삭제 전 채널 조회");
        printChannelInfo(channel1, jcfChannelService);
        jcfMessageService.deleteMessage(message1);
        System.out.println("// 메세지 삭제 후 채널 조회");
        printChannelInfo(channel1, jcfChannelService);
        printAllMessageInfo(jcfMessageService);
        //메세지 삭제 후 메세지 조회
        printMessageInfo(message1, jcfMessageService);
        System.out.println("====================\n\n");


        System.out.println("--- 유저 삭제/퇴장 시 메세지 표시 ---");
        System.out.println("// Bob 유저 삭제 시");
        jcfUserService.deleteUser(user2Id);
        printAllMessageInfo(jcfMessageService);
        System.out.println("// Ch 2에서 Cindy 유저 퇴장 시");
        jcfChannelService.deleteUserInChannel(channel2, user3Id);
        printChannelInfo(channel2, jcfChannelService);
    }


    static void testSprint2() {
        ServiceFactory fileServiceFactory = new FileServiceFactory();
        UserService fileUserService = fileServiceFactory.getUserService();
        ChannelService fileChannelService = fileServiceFactory.getChannelService();
        MessageService messageService = fileServiceFactory.getMessageService();

        //유저 등록
        System.out.println("==== Create User ====");
        UUID user1Id = fileUserService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2Id = fileUserService.createUser("Bob@gmail.com", "Bob", "12345");
        UUID user3Id = fileUserService.createUser("Cindy@gmail.com", "Cindy", "12345");
        UUID user4Id = fileUserService.createUser("Dan@gmail.com", "Dan", "12345");
        UUID user5Id = fileUserService.createUser("Edward@gmail.com", "Edward", "12345");
        UUID user6Id = fileUserService.createUser("Felix@gmail.com", "felix", "12345");
        System.out.println("// 이메일 중복 검사 확인");
        fileUserService.createUser("Alice@gmail.com", "Alice", "12345");
        System.out.println("// 비밀번호 형식 검사 확인");
        fileUserService.createUser("Gary@gmail.com", "Gary", "1234");
        System.out.println("====================\n\n");

        System.out.println("==== Read User ====");
        //유저 단건 조회
        printUserInfo(user1Id, fileUserService);
        //유저 다건 조회
        printAllUserInfo(fileUserService);
        System.out.println("====================\n\n");


        // 유저 수정
        System.out.println("==== Update User ====");
        System.out.println("// 이름과 이메일 변경");
        fileUserService.updateUserName(user1Id, "Andy");
        fileUserService.updateUserEmail(user1Id, "Andy@gmail.com");
        System.out.println("// 이미 존재하는 이메일로 바꾸려는 경우");
        fileUserService.updateUserEmail(user1Id, "Bob@gmail.com");
        System.out.println("// 수정 후 전체 사용자 조회");
        printAllUserInfo(fileUserService);
        System.out.println("====================\n\n");

        // 유저 삭제
        System.out.println("==== Delete User ====");
        fileUserService.deleteUser(user6Id);
        System.out.println("// 삭제 후 전체 사용자 조회");
        printAllUserInfo(fileUserService);
        System.out.println("// 삭제된 유저 정보 조회 시도: ");
        printUserInfo(user6Id, fileUserService);
        System.out.println("====================\n\n");

        //채널 등록
        System.out.println("==== Create Channel ====");
        UUID channel1 = fileChannelService.createChannel("채널 1", "첫 번째 채널", ChannelType.PUBLIC);
        UUID channel2 = fileChannelService.createChannel("채널 2", "두 번째 채널", ChannelType.PUBLIC);
        UUID channel3 = fileChannelService.createChannel("채널 3", "세 번째 채널", ChannelType.PUBLIC);
        UUID channel4 = fileChannelService.createChannel("채널 4", "네 번째 채널", ChannelType.PUBLIC);
        System.out.println("====================\n\n");

        System.out.println("==== Read Channel ====");
        printChannelInfo(channel1, fileChannelService);
        printAllChannelInfo(fileChannelService);
        System.out.println("====================\n\n");

        //채널 수정
        System.out.println("==== Update Channel ====");
        System.out.println("// 채널 이름 변경");
        fileChannelService.updateChannelName(channel1, "채널 11");
        // 채널에 유저 등록
        System.out.println("// 채널에 유저 입장");
        fileChannelService.addUserIntoChannel(channel1, user1Id);
        fileChannelService.addUserIntoChannel(channel1, user2Id);
        fileChannelService.addUserIntoChannel(channel1, user3Id);
        fileChannelService.addUserIntoChannel(channel1, user4Id);
        System.out.println("// 채널에 유저 입장시킨 후 채널 정보 조회");
        printAllChannelInfo(fileChannelService);
        System.out.println("--- 채널 정보 조회 ---");
        printChannelInfo(channel1, fileChannelService);
        System.out.println("// 유저 정보 조회 시에도 채널 목록이 출력되는지 확인");
        printUserInfo(user1Id, fileUserService);
        System.out.println("//채널에 유저 퇴장시킨 후 채널 정보 조회");
        //채널에서 유저 삭제
        fileChannelService.deleteUserInChannel(channel1, user4Id);
        printChannelInfo(channel1, fileChannelService);
        //유저 삭제 시 채널에서도 함께 삭제
        System.out.println("// 사용자 삭제 시 채널에서도 정보 삭제");
        System.out.println("// 삭제 전 채널에 입장");
        fileChannelService.addUserIntoChannel(channel1, user4Id);
        printChannelInfo(channel1, fileChannelService);
        fileUserService.deleteUser(user4Id);
        System.out.println("// 삭제 후");
        printChannelInfo(channel1, fileChannelService);
        System.out.println("====================\n\n");

        //채널 삭제
        System.out.println("==== Delete Channel ====");
        fileChannelService.addUserIntoChannel(channel4, user1Id);
        fileChannelService.addUserIntoChannel(channel4, user2Id);
        fileChannelService.deleteChannel(channel4);
        System.out.println("// 채널 삭제 후 전체 채널 목록 조회");
        printAllChannelInfo(fileChannelService);
        System.out.println("// 삭제한 채널 정보 조회: 채널 정보 조회가 불가능한지 확인");
        printChannelInfo(channel4, fileChannelService);
        System.out.println("// 사용자 정보 조회 시에도 채널이 안 나오는지 확인");
        printUserInfo(user1Id, fileUserService);
        printUserInfo(user2Id, fileUserService);
        System.out.println("====================\n\n");


        // 메시지 등록(전송)
        System.out.println("==== Create Message ====");
        UUID message1 = messageService.createMessage(user1Id, channel1, "안녕하세요");
        UUID message2 = messageService.createMessage(user1Id, channel1, "저는 Andy입니다.");
        UUID message3 = messageService.createMessage(user2Id, channel1, "반가워요 Andy.");
        System.out.println("// 유저가 입장하지 않은 채널에 메세지를 전송하려는 경우");
        UUID message4 = messageService.createMessage(user5Id, channel1, "아직 입장하지 않은 채널");
        System.out.println("====================\n\n");

        System.out.println("==== Read Message ====");
        System.out.println("// 채널에 메세지가 작성되었는지 확인");
        printChannelInfo(channel1, fileChannelService);
        System.out.println("// UUID로 특정 메세지 단건 조회");
        // 메시지 단건 조회
        printMessageInfo(message1, messageService);
        printMessageInfo(message2, messageService);
        // 메시지 다건 조회
        printAllMessageInfo(messageService);
        System.out.println("// 메세지 추가 후 확인");
        fileChannelService.addUserIntoChannel(channel2, user1Id);
        fileChannelService.addUserIntoChannel(channel2, user2Id);
        fileChannelService.addUserIntoChannel(channel2, user3Id);
        UUID message5 = messageService.createMessage(user1Id, channel2, "채널 2 시작");
        UUID message6 = messageService.createMessage(user2Id, channel2, "안녕하세요.");
        UUID message7 = messageService.createMessage(user3Id, channel2, "채널 2는 무엇을 하는 채널인가요?");
        printAllMessageInfo(messageService);
        System.out.println("====================\n\n");

        System.out.println("==== Update Message ====");
        //메시지 수정
        System.out.println("// 메세지 작성자가 아닌 다른 유저가 수정하려는 경우");
        messageService.updateMessage(user2Id, message1, "잘못된 작성자");
        messageService.updateMessage(user1Id, message1, "수정) 안녕하세요 여러분");
        System.out.println("// 메세지 수정 후 확인");
        System.out.println(messageService.getMessageById(message1));
        System.out.println("// 메세지 수정 후 소속 채널에 반영됐는지 확인");
        printChannelInfo(channel1, fileChannelService);
        printAllMessageInfo(messageService);
        System.out.println("====================\n\n");

        System.out.println("==== Delete Message ====");
        // 메시지 삭제
        System.out.println("// 메세지 삭제 전 채널 조회");
        printChannelInfo(channel1, fileChannelService);
        messageService.deleteMessage(message1);
        System.out.println("// 메세지 삭제 후 채널 조회");
        printChannelInfo(channel1, fileChannelService);
        printAllMessageInfo(messageService);
        //메세지 삭제 후 메세지 조회
        printMessageInfo(message1, messageService);
        System.out.println("====================\n\n");


        System.out.println("--- 유저 삭제/퇴장 시 메세지 표시 ---");
        System.out.println("// Bob 유저 삭제 시");
        fileUserService.deleteUser(user2Id);
        printAllMessageInfo(messageService);
        System.out.println("// Ch 2에서 Cindy 유저 퇴장 시");
        fileChannelService.deleteUserInChannel(channel2, user3Id);
        printChannelInfo(channel2, fileChannelService);
    }


    static void testSprint2Advanced() {
        // JCF
//        BasicUserService userService = new BasicUserService(new JCFUserRepository());
//        BasicChannelService channelService = new BasicChannelService(new JCFChannelRepository());
//        BasicMessageService messageService = new BasicMessageService(new JCFMessageRepository());
        // File
        BasicUserService userService = new BasicUserService(new FileUserRepository());
        BasicChannelService channelService = new BasicChannelService(new FileChannelRepository());
        BasicMessageService messageService = new BasicMessageService(new FileMessageRepository());
        userService.setService(channelService, messageService);
        channelService.setService(userService, messageService);
        messageService.setService(userService, channelService);

        //유저 등록
        System.out.println("==== Create User ====");
        UUID user1Id = userService.createUser("Alice@gmail.com", "Alice", "12345");
        UUID user2Id = userService.createUser("Bob@gmail.com", "Bob", "12345");
        UUID user3Id = userService.createUser("Cindy@gmail.com", "Cindy", "12345");
        UUID user4Id = userService.createUser("Dan@gmail.com", "Dan", "12345");
        UUID user5Id = userService.createUser("Edward@gmail.com", "Edward", "12345");
        UUID user6Id = userService.createUser("Felix@gmail.com", "felix", "12345");
        System.out.println("// 이메일 중복 검사 확인");
        userService.createUser("Alice@gmail.com", "Alice", "12345");
        System.out.println("// 비밀번호 형식 검사 확인");
        userService.createUser("Gary@gmail.com", "Gary", "1234");
        System.out.println("====================\n\n");

        System.out.println("==== Read User ====");
        //유저 단건 조회
        printUserInfo(user1Id, userService);
        //유저 다건 조회
        printAllUserInfo(userService);
        System.out.println("====================\n\n");


        // 유저 수정
        System.out.println("==== Update User ====");
        System.out.println("// 이름과 이메일 변경");
        userService.updateUserName(user1Id, "Andy");
        userService.updateUserEmail(user1Id, "Andy@gmail.com");
        System.out.println("// 이미 존재하는 이메일로 바꾸려는 경우");
        userService.updateUserEmail(user1Id, "Bob@gmail.com");
        System.out.println("// 수정 후 전체 사용자 조회");
        printAllUserInfo(userService);
        System.out.println("====================\n\n");

        // 유저 삭제
        System.out.println("==== Delete User ====");
        userService.deleteUser(user6Id);
        System.out.println("// 삭제 후 전체 사용자 조회");
        printAllUserInfo(userService);
        System.out.println("// 삭제된 유저 정보 조회 시도: ");
        printUserInfo(user6Id, userService);
        System.out.println("====================\n\n");

        //채널 등록
        System.out.println("==== Create Channel ====");
        UUID channel1 = channelService.createChannel("채널 1", "첫 번째 채널", ChannelType.PUBLIC);
        UUID channel2 = channelService.createChannel("채널 2", "두 번째 채널", ChannelType.PUBLIC);
        UUID channel3 = channelService.createChannel("채널 3", "세 번째 채널", ChannelType.PUBLIC);
        UUID channel4 = channelService.createChannel("채널 4", "네 번째 채널", ChannelType.PUBLIC);
        System.out.println("====================\n\n");

        System.out.println("==== Read Channel ====");
        printChannelInfo(channel1, channelService);
        printAllChannelInfo(channelService);
        System.out.println("====================\n\n");

        //채널 수정
        System.out.println("==== Update Channel ====");
        System.out.println("// 채널 이름 변경");
        channelService.updateChannelName(channel1, "채널 11");
        // 채널에 유저 등록
        System.out.println("// 채널에 유저 입장");
        channelService.addUserIntoChannel(channel1, user1Id);
        channelService.addUserIntoChannel(channel1, user2Id);
        channelService.addUserIntoChannel(channel1, user3Id);
        channelService.addUserIntoChannel(channel1, user4Id);
        System.out.println("// 채널에 유저 입장시킨 후 채널 정보 조회");
        printAllChannelInfo(channelService);
        System.out.println("--- 채널 정보 조회 ---");
        printChannelInfo(channel1, channelService);
        System.out.println("// 유저 정보 조회 시에도 채널 목록이 출력되는지 확인");
        printUserInfo(user1Id, userService);
        System.out.println("//채널에 유저 퇴장시킨 후 채널 정보 조회");
        //채널에서 유저 삭제
        channelService.deleteUserInChannel(channel1, user4Id);
        printChannelInfo(channel1, channelService);
        //유저 삭제 시 채널에서도 함께 삭제
        System.out.println("// 사용자 삭제 시 채널에서도 정보 삭제");
        System.out.println("// 삭제 전 채널에 입장");
        channelService.addUserIntoChannel(channel1, user4Id);
        printChannelInfo(channel1, channelService);
        userService.deleteUser(user4Id);
        System.out.println("// 삭제 후");
        printChannelInfo(channel1, channelService);
        System.out.println("====================\n\n");

        //채널 삭제
        System.out.println("==== Delete Channel ====");
        channelService.addUserIntoChannel(channel4, user1Id);
        channelService.addUserIntoChannel(channel4, user2Id);
        channelService.deleteChannel(channel4);
        System.out.println("// 채널 삭제 후 전체 채널 목록 조회");
        printAllChannelInfo(channelService);
        System.out.println("// 삭제한 채널 정보 조회: 채널 정보 조회가 불가능한지 확인");
        printChannelInfo(channel4, channelService);
        System.out.println("// 사용자 정보 조회 시에도 채널이 안 나오는지 확인");
        printUserInfo(user1Id, userService);
        printUserInfo(user2Id, userService);
        System.out.println("====================\n\n");


        // 메시지 등록(전송)
        System.out.println("==== Create Message ====");
        UUID message1 = messageService.createMessage(user1Id, channel1, "안녕하세요");
        UUID message2 = messageService.createMessage(user1Id, channel1, "저는 Andy입니다.");
        UUID message3 = messageService.createMessage(user2Id, channel1, "반가워요 Andy.");
        System.out.println("// 유저가 입장하지 않은 채널에 메세지를 전송하려는 경우");
        UUID message4 = messageService.createMessage(user5Id, channel1, "아직 입장하지 않은 채널");
        System.out.println("====================\n\n");

        System.out.println("==== Read Message ====");
        System.out.println("// 채널에 메세지가 작성되었는지 확인");
        printChannelInfo(channel1, channelService);
        System.out.println("// UUID로 특정 메세지 단건 조회");
        // 메시지 단건 조회
        printMessageInfo(message1, messageService);
        printMessageInfo(message2, messageService);
        // 메시지 다건 조회
        printAllMessageInfo(messageService);
        System.out.println("// 메세지 추가 후 확인");
        channelService.addUserIntoChannel(channel2, user1Id);
        channelService.addUserIntoChannel(channel2, user2Id);
        channelService.addUserIntoChannel(channel2, user3Id);
        UUID message5 = messageService.createMessage(user1Id, channel2, "채널 2 시작");
        UUID message6 = messageService.createMessage(user2Id, channel2, "안녕하세요.");
        UUID message7 = messageService.createMessage(user3Id, channel2, "채널 2는 무엇을 하는 채널인가요?");
        printAllMessageInfo(messageService);
        System.out.println("====================\n\n");

        System.out.println("==== Update Message ====");
        //메시지 수정
        System.out.println("// 메세지 작성자가 아닌 다른 유저가 수정하려는 경우");
        messageService.updateMessage(user2Id, message1, "잘못된 작성자");
        messageService.updateMessage(user1Id, message1, "수정) 안녕하세요 여러분");
        System.out.println("// 메세지 수정 후 확인");
        System.out.println(messageService.getMessageById(message1));
        System.out.println("// 메세지 수정 후 소속 채널에 반영됐는지 확인");
        printChannelInfo(channel1, channelService);
        printAllMessageInfo(messageService);
        System.out.println("====================\n\n");

        System.out.println("==== Delete Message ====");
        // 메시지 삭제
        System.out.println("// 메세지 삭제 전 채널 조회");
        printChannelInfo(channel1, channelService);
        messageService.deleteMessage(message1);
        System.out.println("// 메세지 삭제 후 채널 조회");
        printChannelInfo(channel1, channelService);
        printAllMessageInfo(messageService);
        //메세지 삭제 후 메세지 조회
        printMessageInfo(message1, messageService);
        System.out.println("====================\n\n");


        System.out.println("--- 유저 삭제/퇴장 시 메세지 표시 ---");
        System.out.println("// Bob 유저 삭제 시");
        userService.deleteUser(user2Id);
        printAllMessageInfo(messageService);
        System.out.println("// Ch 2에서 Cindy 유저 퇴장 시");
        channelService.deleteUserInChannel(channel2, user3Id);
        printChannelInfo(channel2, channelService);
    }

    public static void main(String[] args) {
        //testSprint1();

        // 파일 없앤 후 시작
        String[] filesToDelete = {"user.ser", "channel.ser", "message.ser"};
        for (String fileName : filesToDelete) {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        //testSprint2();

        testSprint2Advanced();

    }
}
