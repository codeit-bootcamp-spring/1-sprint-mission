package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class DiscodeitApplication {
  /* 만약 디렉토리가 없다면, 파일 저장 시 오류가 발생하기 때문에 프로그램이 시작될 때 자동으로 디렉토리를 생성하는 초기화 로직을 넣어야함.
    1. 클래스 안에 초기화 메서드
    2. psvm 안에 1) 디렉토리 선언 2) 초기화
   */

	public static void init(Path directory) {
		// 저장할 경로의 파일 초기화 메서드
		if (!Files.exists(directory)) {
			try {
				Files.createDirectories(directory);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);

		// 1) 디렉토리 선언 2) 초기화
		Path directory = Paths.get("data"); // 폴더 경로를 지정하는 부분. data는 파일을 저장할 폴더 이름일 뿐. 마음대로 변경 가능
		init(directory);

		SerializationUtil<UserDto> userUtil = new SerializationUtil<>();
		SerializationUtil<ChannelDto> channelUtil = new SerializationUtil<>();
		SerializationUtil<Message> messageUtil = new SerializationUtil<>();

		// 서비스 초기화 : Bean 이용
		// 레포지토리는 @Autowired로 스프링이 자동으로 주입해주니까 따로 초기화 안해도 됨
		// TODO context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		ChannelService channelService = context.getBean(ChannelService.class);
		MessageService messageService = context.getBean(MessageService.class);

		// ===== 회원 직렬화 =====
		System.out.println("\n===== 회원 서비스 CRUD =====");
		// 등록
		User user1 = new User("정연경", 24, Gender.FEMALE);
		// UserStatus 생성, user에 연결
		UserStatus userStatus1 = new UserStatus(user1.getId()); // UserStatus 엔티티에 UUID userId 선언해서 가능 > 이해 잘 안댐
		UserDto userDto1 = new UserDto(user1, null, userStatus1);
		boolean u1 = userService.createUser(userDto1);

		User user2 = new User("신서연", 23, Gender.FEMALE);
		// UserStatus 생성, user에 연결
		UserStatus userStatus2 = new UserStatus(user1.getId());
		UserDto userDto2 = new UserDto(user2, null, userStatus2);
		boolean u2 = userService.createUser(userDto2);

		if (u1 || u2) {
			List<UserDto> userList = userService.getAllUsers(); // 직렬화 대상!! : 최종 리스트 readAllUsers()
			userUtil.saveData(userList);
		}

		// 조회 - 단건
		userService.findUser(userDto1);

		// 조회 - 다건
		userService.findAllUsers();

		// 수정 & 조회
		UpdateUserDto updateUserDto1 = new UpdateUserDto(user1.getId(), user1.getName(), user1.getAge(), user1.getGender(), null);
		userService.updateUser(updateUserDto1);
		userUtil.saveData(userService.getAllUsers());

		// 삭제 & 조회
		userService.deleteUser(user1.getId());
		userUtil.saveData(userService.getAllUsers());

		// ===== 회원 파일 역직렬화 =====
		// for문 써서 리스트 객체 하나하나 프린트 해줘야 함
		System.out.println("\n===== 파일에서 회원 데이터 불러오기 =====");
		List<UserDto> deserializedUsers = userUtil.loadData();
		for (UserDto u : deserializedUsers)
			System.out.println(u);
		// deserializedUsers.forEach(System.out::println);


//		// ===== 채널 직렬화 =====
//		System.out.println("\n===== 채널 서비스 CRUD =====");
//
//		// 등록
//		Channel channel1 = new Channel("스터디 채널", "자바 공부, 상호 피드백");
//		// TODO : 유저랑 메세지가 이렇게 새로 생성한 리스트에 들어가는게 맞나....? 뭔가 찝찝
//		// 유저 리스트 생성
//		List<User> ch1Participants = List.of(user1, user2);
//		// 메시지 리스트 (초기에는 비어 있음)
//		List<Message> ch1Messages = Collections.emptyList();
//		ChannelDto channelDto1 = new ChannelDto(channel1, ch1Participants, ch1Messages, true); //
//		boolean c1 = channelService.createPrivateChannel(channelDto1);
//
//		Channel channel2 = new Channel("잡담 채널", "아무말 해도 되는 공간");
//		// 유저 리스트 생성
//		List<User> ch2Participants = List.of(user1, user2);
//		// 메세지 리스트 (초기에는 비어 있음)
//		List<Message> ch2Messages = Collections.emptyList();
//		ChannelDto channelDto2 = new ChannelDto(channel2, ch2Participants, ch2Messages, false);
//		boolean c2 = channelService.createPublicChannel(channelDto2);
//
//		if (c1 || c2) {
//			List<ChannelDto> channelList = channelService.getAllChannels();
//			channelUtil.saveData(channelList);
//		}
//
//		// 조회 - 단건
//		channelService.findPublicChannel(channeDto1);
//
//		// 조회 - 다건
//		channelService.readAllChannels();
//
//		// 수정 & 조회
//		channelService.updatePublicChannel(channelDto1);
//		channelUtil.saveData(channelService.getAllChannels());
//
//		// 삭제 & 조회
//		channelService.deleteChannel(channel1.getId());
//		channelUtil.saveData(channelService.getAllChannels());
//
//		// ===== 채널 파일 역직렬화 =====
//		System.out.println("\n===== 파일에서 채널 데이터 불러오기 =====");
//		List<Channel> deserializedChannels = channelUtil.loadData();
//		for (Channel c : deserializedChannels)
//			System.out.println(c);


		// ===== 메세지 직렬화 =====
		System.out.println("\n===== 메세지 서비스 CRUD =====");

		// 메세지 보낼 회원 등록
		User messageSender = new User("김예은", 25, Gender.FEMALE);
		// UserStatus 생성, user에 연결
		UserStatus userStatus3 = new UserStatus(messageSender.getId());
		UserDto userDto3 = new UserDto(messageSender, null, userStatus3);
		userService.createUser(userDto3);

		// 등록
		// TODO : 채널 아이디랑 연결시켜야할듯
		Message message1 = new Message("질문1", messageSender.getId());
		boolean m1 = messageService.createMessage(message1);

		Message message2 = new Message("질문2", messageSender.getId());
		boolean m2 = messageService.createMessage(message2);

		if (m1 || m2) {
			List<Message> messageList = messageService.getAllMessages();
			messageUtil.saveData(messageList);
		}

		// 조회 - 단건
		messageService.readMessage(message1.getId());

		// 조회 - 다건
		messageService.readAllMessages();

		// 수정 & 조회
		messageService.updateMessage(message1.getId(), "스프링 스터디", messageSender.getId());
		messageUtil.saveData(messageService.getAllMessages());

		// 삭제 & 조회
		messageService.deleteMessage(message1.getId());
		messageUtil.saveData(messageService.getAllMessages());

		// ===== 채널 파일 역직렬화 =====
		System.out.println("\n===== 파일에서 메세지 데이터 불러오기 =====");
		List<Message> deserializedMessages = messageUtil.loadData();
		for (Message m : deserializedMessages)
			System.out.println(m);
	}
}
