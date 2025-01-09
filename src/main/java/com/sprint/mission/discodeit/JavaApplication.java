package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = JCFUserService.getInstance();
        ChannelService channelService = JCFChannelService.getInstance();
        MessageService messageService = JCFMessageService.getInstance();


        System.out.println("=========== 사용자 서비스 테스트 ===========");
        testUserService(userService);

        System.out.println("\n=========== 채널 서비스 테스트 ===========");
        testChannelService(channelService);

        System.out.println("\n=========== 메시지 서비스 테스트 ===========");
        testMessageService(messageService, userService, channelService);
    }

    private static void testUserService(UserService userService) {
        // 테스트용 사용자 데이터 배열
        User[] testUsers = {
                new User("김철수", "kim@test.com", UserStatus.ONLINE),
                new User("이영희", "lee@test.com", UserStatus.IDLE),
                new User("박지성", "park@test.com", UserStatus.OFFLINE),
                new User("정민수", "jung@test.com", UserStatus.ONLINE),
                new User("강다혜", "kang@test.com", UserStatus.IDLE),
                new User("조성민", "cho@test.com", UserStatus.ONLINE),
                new User("윤서연", "yoon@test.com", UserStatus.OFFLINE),
                new User("장미란", "jang@test.com", UserStatus.ONLINE),
                new User("임준호", "lim@test.com", UserStatus.IDLE),
                new User("한지민", "han@test.com", UserStatus.ONLINE)
        };

        // 사용자 데이터 등록
        System.out.println("10명의 테스트 사용자 등록 시작\n");
        for (User user : testUsers) {
            User createdUser = userService.register(user);
            System.out.printf("사용자 등록 완료 - 이름: %s, 이메일: %s, 상태: %s%n",
                    createdUser.getName(),
                    createdUser.getEmail(),
                    createdUser.getStatus());
        }

        System.out.println("\n전체 등록된 사용자 수: " + userService.getAllUsers().size());
        // 1. 유저 등록
        System.out.println("\n새 유저 등록 ");
        User user = new User("홍길동", "gildong@google.com", UserStatus.ONLINE);
        UUID userId = user.getId();
        User gildongUser = userService.register(user);
        System.out.printf("사용자 등록 완료 - 이름: %s, 이메일: %s, 상태: %s%n",
                gildongUser.getName(),
                gildongUser.getEmail(),
                gildongUser.getStatus());

        // 2. 유저 조회(단건)
        System.out.println("\n최근 등록한 아이디 " + gildongUser.getId() + "인 유저 조회 ");
        Optional<User> fetchedUser = userService.getUserDetails(gildongUser.getId());
        fetchedUser.ifPresent(u -> System.out.println("해당 아이디로 검색된 유저명 : " + u.getName()));

        // 3. 유저 조회(다건)
        System.out.println("\n모든 유저 조회");
        List<String> allUsersName = userService.getAllUsers().stream()
                .map(User::getName)
                .toList();
        System.out.println(allUsersName);

        System.out.println("총 유저 수: " + allUsersName.size());

        // 4. 유저 정보 수정
        System.out.println("\n유저 정보 수정 ");
        boolean updateSuccess = userService.updateUserProfile(gildongUser.getId(), "홍길동", "gildong@naver.com", UserStatus.IDLE);
        System.out.println("수정 결과 : " + (updateSuccess ? "성공" : "실패"));

        // 5. 수정된 유저 데이터 조회
        System.out.println("\n수정된 유저 " + gildongUser.getId() +"의 정보 조회 ");
        Optional<User> verifyUpdate = userService.getUserDetails(gildongUser.getId());
        verifyUpdate.ifPresent(u -> System.out.println("수정된 이메일: " + u.getEmail() + " 수정된 상태 : "+ u.getStatus()));

        // 6. 유저 삭제
        System.out.println("\n유저 삭제 ");
        boolean deleteSuccess = userService.deleteUser(gildongUser.getId());
        System.out.println("삭제 결과 : " + (deleteSuccess ? "성공" : "실패"));

        // 7. 삭제된 유저 조회
        System.out.println("\n삭제한 유저 조회 ");
        Optional<User> deletedUser = userService.getUserDetails(gildongUser.getId());
        System.out.println("유저 존재 여부 : " + deletedUser.isPresent());
        allUsersName = userService.getAllUsers().stream()
                .map(User::getName)
                .toList();
        System.out.println(allUsersName);
    }

    private static void testChannelService(ChannelService channelService) {
        // 1. 채널 등록
        System.out.println("새 채널 생성 ");
        Channel channel = channelService.createChannel("일반","스터디 회의실입니다." ,ChannelType.GROUP_DM);
        System.out.println("생성된 새 채널 Id: " + channel.getId());

        // 2. 채널 조회 (단건)
        System.out.println("\n채널 조회");
        Optional<Channel> searchChannel = channelService.getChannelDetails(channel.getId());
        searchChannel.ifPresent(c -> System.out.println("조회 된 채널 주제 : " + c.getTopic()));

        // 3. 채널 조회 (다건)
        System.out.println("\n모든 채널 조회 ");
        List<String> allChannelsName = channelService.findAllChannels()
                .stream()
                .map(Channel::getName)
                .toList();
        System.out.println("총 채널 수: " + allChannelsName.size());


        // 4. 채널 정보 수정
        System.out.println("\n채널 정보 수정 ");
        boolean updateSuccess = channelService.editChannel(channel.getId(), "일반","주 1회 스터디를 진행하는 채널입니다.", ChannelType.GROUP_DM);
        System.out.println("Update success: " + updateSuccess);

        // 5. 수정된 채널 정보 조회
        System.out.println("\n수정된 채널 조회 ");
        channelService.getChannelDetails(channel.getId())
                .ifPresent(c -> System.out.println("수정된 주제: " + c.getTopic()));

        // 6. 채널 삭제
        System.out.println("\n체널 삭제 ");
        System.out.println("삭제 결과: " + channelService.deleteChannel(channel.getId()));

        // 7. 삭제된 채널 조회
        System.out.println("\n삭제한 채널 조회 ");
        Optional<Channel> deletedChannel = channelService.getChannelDetails(channel.getId());
        System.out.println("존재 여부: " + deletedChannel.isPresent());


    }

    private static void testMessageService(MessageService messageService, UserService userService, ChannelService channelService) {
        // 유저 및 채널 등록
        User author = userService.register(new User("유저1", "user1@google.com", UserStatus.ONLINE));
        Channel channel = channelService.createChannel("채널1","학습 공간입니다", ChannelType.PUBLIC_THREAD);
        List<User> allUsers = userService.getAllUsers(); 

        System.out.println("새 메시지 전송(등록)");
        Message message = new Message("Hello, world!", author, channel);
        Message createdMessage = messageService.sendMessage(message, allUsers);
        System.out.println("새 메시지 ID: " + createdMessage.getId());

        System.out.println("\n채널 메시지 조회");
        List<Message> channelMessages = messageService.getChannelMessages(channel.getId());
        System.out.println("채널에 등록된 메시지 수: " + channelMessages.size());

        System.out.println("\n작성자 메시지 조회");
        List<Message> userMessages = messageService.getUserMessage(author);
        System.out.println(author.getName() + "님이 작성한 메시지 수 : " + userMessages.size());

        System.out.println("\n메시지 수정");
        boolean updateSuccess = messageService.editMessage(createdMessage.getId(), "Hello, world!!(수정)");
        System.out.println("수정 결과: " + (updateSuccess ? "성공" : "실패"));

        System.out.println("\n수정한 메시지 조회");
        List<Message> verifyUpdate = messageService.getChannelMessages(channel.getId());
        verifyUpdate.forEach(m -> System.out.println("메시지 내용 : " + m.getContent()));

        System.out.println("\n메시지 삭제");
        boolean deleteSuccess = messageService.deleteMessage(createdMessage.getId());
        System.out.println("삭제 결과: " + (deleteSuccess ? "성공" : "실패"));

        System.out.println("\n삭제한 메시지 조회");
        List<Message> afterDelete = messageService.getChannelMessages(channel.getId());
        System.out.println("삭제후 남은 메시지 수: " + afterDelete.size());


        System.out.println("\n=== 멘션 동작 테스트 ===");
        User userA = new User("유저A", "userA@example.com", UserStatus.ONLINE);
        userService.register(userA);
        User userB = new User("유저B", "userB@example.com", UserStatus.IDLE);
        userService.register(userB);

        Channel createdChannel = channelService.createChannel("소통해요~", "자유 대화를 위한 채널입니다~", ChannelType.GROUP_DM);

        // 유저 A가 유저 B를 멘션하며 메시지 전송
        String messageContent = "안녕하세요, @유저B님! 어떻게 지내시나요?";
        Message messageAtoB = new Message(messageContent, userA, createdChannel);

        // 모든 유저 리스트
        allUsers = userService.getAllUsers();

        // 메시지 전송 및 멘션 알림 처리
        messageService.sendMessage(messageAtoB, allUsers);
    }
}