package com.sprint.mission;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    static UserResponseDto setupUser(UserService userService) {
        UserResponseDto user = userService.create(new CreateUserDto("woody", "우디","woody@codeit.com", "woody1234", ""));
        return user;
    }

    static ChannelResponseDto setupChannel(ChannelService channelService) {
        ChannelResponseDto channel = channelService.create(new CreateChannelDto("공지", ChannelType.PUBLIC, ChannelCategory.TEXT, "공지채널 입니다."));
        return channel;
    }

    static void messageCreateTest(MessageService messageService, String channelId, String authorId) {
        MessageResponseDto message = messageService.create(new CreateMessageDto( "안녕하세요.", channelId, authorId));
        System.out.println("메시지 생성: " + message.id());
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);
        UserStatusService userStatusService = context.getBean(UserStatusService.class);
        ReadStatusService readStatusService = context.getBean(ReadStatusService.class);
        BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);

        // 셋업
        UserResponseDto user = setupUser(userService);
        ChannelResponseDto channel = setupChannel(channelService);
        channelService.addUserToChannel(channel.id(), user.id());
        // 테스트
        messageCreateTest(messageService, channel.id(), user.id());


        //1. 등록
        System.out.println("\n=== 1. 등록 테스트 ===");

        System.out.println("\n--- User 등록---");
        UserResponseDto user1 = userService.create(new CreateUserDto("codeit", "코드잇", "codeit@codeit.co.kr", "codeit123", null));
        UserResponseDto user2 = userService.create(new CreateUserDto("yujin", "박유진", "yudility@gmail.com", "yujinpark123", "나는 박유진"));
        UserResponseDto user3 = userService.create(new CreateUserDto("gd0000", "홍길동", "gildong@naver.com", "gdhong123123", "반갑습니다."));

        System.out.println("> 유저 생성 결과 및 전체 유저 목록: ");
        userService.findAll().forEach(u -> System.out.println(u.toString()));

        //예외 - 중복
        System.out.println("\n* 중복 이메일 케이스");
        System.out.println("- 닉네임: \"중복\", 이메일: \"codeit@codeit.co.kr\"로 생성 시도");
        System.out.println("> 실행 결과: ");

        try {
            UserResponseDto user4 = userService.create(new CreateUserDto("중복", "나중복","codeit@codeit.co.kr", "pwd1341241", "저는 중복된 사용자 입니다."));
            System.out.println(user4.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n--- Channel 등록---");
        ChannelResponseDto channel1 = channelService.create(new CreateChannelDto("일반1", ChannelType.PUBLIC, ChannelCategory.TEXT, "일반1 채널"));
        ChannelResponseDto channel2 = channelService.create(new CreateChannelDto("일반2", ChannelType.PRIVATE, ChannelCategory.TEXT, "일반2 채널"));
        ChannelResponseDto channel3 = channelService.create(new CreateChannelDto("공지사항", ChannelType.PUBLIC, ChannelCategory.TEXT, "공지사항 채널"));
        ChannelResponseDto channel4 = channelService.create(new CreateChannelDto("회의실", ChannelType.PUBLIC, ChannelCategory.VOICE, "회의실 채널"));

        System.out.println("> 채널 생성 결과 및 전체 채널 목록:");
        channelService.findAllByUserId(user1.id()).forEach(c -> System.out.println(c.toString()));

        System.out.println("\n* 채널에 유저 추가 및 삭제: ");

        System.out.println();
        System.out.println("- " + channel1.channelName() + " 채널에 " + user1.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1.id(), user1.id()));
        System.out.println("- " + channel1.channelName() + " 채널에 " + user2.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1.id(), user2.id()));
        System.out.println("- " + channel1.channelName() + " 채널에 " + user3.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel1.id(), user3.id()));
        System.out.println(channel1);

        System.out.println();
        System.out.println("- " + channel2.channelName() + " 채널에 " + user1.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel2.id(), user1.id()));
        System.out.println("- " + channel2.channelName() + " 채널에 " + user2.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel2.id(), user2.id()));
        System.out.println(channel2.toString());

        System.out.println();
        System.out.println("- " + channel3.channelName() + " 채널에 " + user1.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3.id(), user1.id()));
        System.out.println("- " + channel3.channelName() + " 채널에 " + user2.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3.id(), user2.id()));
        System.out.println("- " + channel3.channelName() + " 채널에 " + user3.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel3.id(), user3.id()));
        System.out.println(channel3.toString());

        System.out.println();
        System.out.println("- " + channel4.channelName() + " 채널에 " + user1.nickname() + " 유저 추가 성공 여부 -> " + channelService.addUserToChannel(channel4.id(), user1.id()));
        System.out.println(channel4.toString());


        System.out.println("\n\n--- Massage 등록---");

        MessageResponseDto msg1 = messageService.create(new CreateMessageDto( "일반1 채널입니다!", channel1.id(), user1.id()));
        MessageResponseDto msg2 = messageService.create(new CreateMessageDto("반갑습니다!", channel1.id(), user3.id()));
        MessageResponseDto msg3 = messageService.create(new CreateMessageDto("일반2 채널입니다.", channel2.id(), user1.id()));
        MessageResponseDto msg4 = messageService.create(new CreateMessageDto("공지사항 채널입니다! 반갑습니다.", channel3.id(), user2.id()));
        MessageResponseDto msg5 = messageService.create(new CreateMessageDto("회의실 채널입니다.", channel4.id(), user1.id()));

        System.out.println("> 메시지 생성 결과 및 전체 목록:");
        messageService.findAll().forEach(m -> System.out.println(m.toString()));

        System.out.println("\n* 심화 - 메세지 예외 처리");

        System.out.println("\n1) DB에 존재하지 않는 User로 메세지 생성 ");
        User notExistUser = new User("none", "없는유저", "noOne@test.com", "hello", "123454356",  AccountStatus.UNVERIFIED, null);
        System.out.println("- 테스트 유저");
        notExistUser.displayFullInfo();
        System.out.println("> 결과: ");
        try {
            System.out.println(messageService.create(new CreateMessageDto("test message", channel1.id(), notExistUser.getId())).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n2) DB에 존재하지 않는 Channel로 메세지 생성 ");
        Channel notExistChannel = new Channel("없는채널", ChannelType.PUBLIC, ChannelCategory.TEXT, "없는 채널의 설명");
        System.out.println("- 테스트 채널");
        notExistChannel.displayFullInfo();
        System.out.println("> 결과: ");
        try {
            System.out.println(messageService.create(new CreateMessageDto("test message", notExistChannel.getId(), user1.id())).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) DB에 존재하지 않는 User와 Channel로 메세지 생성 ");
        System.out.println("- 테스트 채널 및 유저(위 테스트 케이스 사용)");
        System.out.println("> 결과: ");
        try {
            System.out.println(messageService.create(new CreateMessageDto("test message", notExistChannel.getId(), notExistUser.getId())).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n=== 2. 조회 테스트 ===");

        System.out.println("\n--- User 조회 ---");

        System.out.println("1) 전체 목록 조회");
        List<UserResponseDto> tempUsers = userService.findAll();

        System.out.println("> 조회 결과: ");
        tempUsers.forEach(u -> System.out.println(u.toString()));

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForTest = tempUsers.get(0).id();

        System.out.println("- 조회할 UUID를 가진 유저가 있는 경우: ");
        System.out.println("> UUID: " + uuidStringForTest + "로 조회한 결과: ");
        UserResponseDto userByUUID = userService.findById(uuidStringForTest);
        System.out.println(userByUUID.toString());

        System.out.println("\n- 조회할 UUID를 가진 유저가 없는 경우: ");
        UUID testUUID = UUID.randomUUID();
        System.out.println("> UUID: " + testUUID + "로 조회한 결과:");
        try {
            System.out.println(userService.findById(testUUID.toString()).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) 이메일로 조회(단건): ");
        System.out.println("- 조회할 이메일을 가진 유저가 있는 경우: ");
        System.out.println("> codeit@codeit.co.kr을 이메일로 가진 유저 조회 결과: ");
        UserResponseDto userByEmail = userService.findByEmail("codeit@codeit.co.kr");
        System.out.println(userByEmail.toString());

        System.out.println("\n- 조회할 이메일을 가진 유저가 없는 경우");
        System.out.println("> no_one@test.com을 이메일로 가진 유저 조회 결과: ");
        try {
            UserResponseDto noUser = userService.findByEmail("no_one@test.com");
            System.out.println(noUser.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n4) 닉네임으로 조회(다건): ");
        System.out.println("> 닉네임에 \"박유진\"이 포함된 User 조회 결과: ");
        userService.findAllContainsNickname("박유진").forEach(u -> System.out.println(u.toString()));

// UserStatus enum -> class 변경으로 인한 임시 주석 처리
//        System.out.println("\n5) 유저 상태로 조회(다건): ");
//        System.out.println("* default 는 ACTIVE 이므로 홍길동을 INACTIVE로 변경하여 테스트 하였음");
//        UserDTO userDTO = new UserDTO(user3.nickname(), user3.getEmail(), user3.getUserStatusId(), user3.getStatusMessage(), user3.getAccountStatus(), user3.getProfileImageId());
//
//        userService.updateUser(user3.id(), userDTO, Instant.now());

//        // UserStatus enum -> class 변경으로 인한 임시 주석 처리
//        System.out.println("> 유저 상태에 활동중(ACTIVE)인 User 조회 결과: ");
//        userService.getUserByUserStatus().forEach(User::displayInfo);

        System.out.println("\n\n--- Channel 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<ChannelResponseDto> tempChannels = channelService.findAllByUserId(user1.id());
        tempChannels.forEach(c -> System.out.println(c.toString()));

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForChannelTest = tempChannels.get(0).id();

        System.out.println("- 조회할 UUID를 가진 채널이 있는 경우: ");
        System.out.println("> 조회할 UUID: " + uuidStringForChannelTest);
        System.out.println("> 조회 결과: ");
        System.out.println(channelService.findById(uuidStringForChannelTest).toString());

        System.out.println("\n- 조회할 UUID를 가진 채널이 없는 경우: ");
        UUID testUUID2 = UUID.randomUUID();
        System.out.println("> 조회할 UUID: " + testUUID2);
        System.out.println("> 조회 결과:");
        try {
            System.out.println(channelService.findById(testUUID2.toString()).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        System.out.println("\n3) 채널 이름으로 조회(다건)");
        System.out.println("> 채널 이름에 \"일반\"이 포함된 채널 조회 결과:");
        channelService.findAllByChannelName("일반").forEach(c -> System.out.println(c.toString()));


        System.out.println("\n4) 채널 type(종류)으로 조회(다건)");
        System.out.println("> Channel Type 이 PUBLIC 인 채널 조회 결과: ");
        channelService.findByChannelType(ChannelType.PUBLIC).forEach(c -> System.out.println(c.toString()));
        System.out.println("\n5) 채널에 참여중인 유저 목록 조회");
        System.out.println("> channel1에 참여 중인 유저 목록 조회 결과:");
        channelService.findAllUserInChannel(channel1.id()).forEach(u -> System.out.println(u.id()));

        System.out.println("\n--- Message 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<MessageResponseDto> messageList = messageService.findAll();
        messageList.forEach(m -> System.out.println(m.toString()));


        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForMessageTest = messageList.get(0).id();

        System.out.println("- 조회할 UUID를 가진 메세지가 있는 경우: ");
        System.out.println("> 조회할 UUID: " + uuidStringForMessageTest);
        System.out.println("> 조회 결과:\n");
        System.out.println(messageService.findById(uuidStringForMessageTest).toString());

        System.out.println("\n- 조회할 UUID를 가진 메세지가 없는 경우: ");
        UUID testUUID3 = UUID.randomUUID();
        System.out.println("> 조회할 UUID: " + testUUID3);
        System.out.println("> 조회 결과:\n");
        try {
            System.out.println(messageService.findById(testUUID3.toString()).toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n3) 내용으로 조회(다건)");
        System.out.println("> 내용에 \"반갑습니다\"가 포함된 메세지 조회: ");
        messageService.findAllContainsContent("반갑습니다").forEach(m -> System.out.println(m.toString()));


        System.out.println("\n4) 작성자로 조회(다건)");
        System.out.println("> user2(name=박유진)이 작성한 메세지 조회: ");
        messageService.findAllBySenderId(user2.id()).forEach(m -> System.out.println(m.toString()));

        //todo
        //없는 유저로 조회하는 경우

        System.out.println("\n5)특정 채널에 속하는 메세지 조회(다건)");
        System.out.println("> \"일반1\" 채널의 전체 메세지 목록:");
        messageService.findAllByChannelId(channel1.id()).forEach(m -> System.out.println(m.toString()));

        // 3. 수정 테스트
        System.out.println("\n\n=== 3. 수정 테스트 ===");

        System.out.println("\n--- User 수정 ---");
        System.out.println("\n> User1의 이름을 '코드잇'에서 'codeit'으로 수정: ");
        System.out.println(user1.toString());
        //todo - 고민해볼것
        //UserResponseDto에 userStatus를 boolean으로 넣을 것인지, UserStatusId를 넣을 것인지.
        UpdateUserDto updateUserDto = new UpdateUserDto("codeit", user1.email(), userStatusService.findById(user1.id()).isActive(), user1.statusMessage(), user1.accountStatus(), user1.profileImageId(), Instant.now());

        try {
            System.out.println("\n> 수정 결과: ");
            System.out.println(UserResponseDto.from(userService.updateUser(user1.id(), updateUserDto), userStatusService.findById(user1.id()).isActive()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\n\n--- Channel 수정 ---");
        System.out.println("\n> channel1의 이름을 '일반1'에서 '수다방'으로 수정: ");
        ChannelResponseDto channelById = channelService.findById(channel1.id());
        System.out.println(channelById.toString());

        UpdateChannelDto updateChannelDto = new UpdateChannelDto("수다방", channelById.channelType(), channelById.description(), Instant.now());

        System.out.println("\n> 수정 결과:");
        System.out.println(channelService.updateChannel(channelById.id(), updateChannelDto).toString());

        System.out.println("\n\n--- Message 수정 ---");
        System.out.println("\n> msg1 내용을 '일반1 채널입니다!'에서 '수다방 채널입니다!'로 수정 ");
        MessageResponseDto messageByUUID = messageService.findById(msg1.id());
        System.out.println(messageByUUID.toString());

        System.out.println("\n> 수정 결과:");
        UpdateMessageDto updateMessageDto = new UpdateMessageDto("수다방 채널입니다!", Instant.now(), null);
        System.out.println(messageService.updateMessage(messageByUUID.id(), updateMessageDto).toString());

        // 4. 삭제 테스트
        System.out.println("\n\n=== 4. 삭제 테스트 ===");

        //의존 관계를 고려하여 역순으로 진행
        System.out.println("\n--- Message 삭제 ---");
        System.out.println("\n -> 이름이 \"홍길동\"인 user가 작성한 첫번째 메세지 삭제 ");

        System.out.println("\n> 전체 메세지 목록: ");
        messageService.findAll().forEach(m -> System.out.println(m.toString()));

        UserResponseDto senderMsg = userService.findAllContainsNickname("홍길동").get(0);
        MessageResponseDto msgToDelete = messageService.findAllBySenderId(senderMsg.id()).get(0);
        System.out.println("\n-> Message 삭제 결과: " + messageService.delete(msgToDelete.id()));

        System.out.println("\n> 삭제 후 메세지 목록: ");
        messageService.findAll().forEach(m -> System.out.println(m.toString()));

        //삭제 시에도 유저, 채널 없을 때 예외 보여줘야? -> 애초에 조회가 되어야 삭제가 되니 이거는 빼는걸로

        System.out.println("\n--- Channel 삭제 ---");
        System.out.println("\n -> 채널 이름에 \"일반\"이 들어간 채널 삭제");

        System.out.println("\n> 전체 채널 목록:");
        channelService.findAllByUserId(user1.id()).forEach(c -> System.out.println(c.toString()));

        List<ChannelResponseDto> normalChannels = channelService.findAllByChannelName("일반");
        normalChannels.forEach(c -> System.out.println("\n-> 채널 \"" + c.channelName() + "\" 삭제 결과: " + channelService.delete(c.id())));

        System.out.println("\n삭제 후 채널 목록: ");
        channelService.findAllByUserId(user1.id()).forEach(c -> System.out.println(c.toString()));

        System.out.println("\n--- User 삭제 ---");
        System.out.println("\n -> email이 \"gildong@naver.com\"인 User 삭제");

        System.out.println("\n> 전체 유저 목록: ");
        userService.findAll().forEach(u -> System.out.println(u.toString()));

        UserResponseDto foundUserByEmail = userService.findByEmail("gildong@naver.com");
        System.out.println("\n-> User 삭제 결과: " + userService.deleteUser(foundUserByEmail.id()));

        System.out.println("\n> 삭제 후 유저 목록: ");
        userService.findAll().forEach(u -> System.out.println(u.toString()));

    }

}
