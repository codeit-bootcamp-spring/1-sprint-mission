package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.binarycontent.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.channel.*;
import com.sprint.mission.discodeit.entity.message.*;
import com.sprint.mission.discodeit.entity.user.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.auth.AuthService;
import com.sprint.mission.discodeit.service.status.ReadStatusService;
import com.sprint.mission.discodeit.service.status.UserStateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ac = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = ac.getBean("basicUserService", UserService.class);
		ChannelService channelService = ac.getBean("basicChannelService", ChannelService.class);
		MessageService messageService = ac.getBean("basicMessageService", MessageService.class);
		AuthService userAuthService = ac.getBean("userAuthService", AuthService.class);
		ReadStatusService readStatusService = ac.getBean("readStatusService", ReadStatusService.class);
		UserStateService userStateService = ac.getBean("userStateService", UserStateService.class);
		BinaryContentService binaryContentService = ac.getBean("binaryContentService", BinaryContentService.class);

		log.error(userService.toString());
		log.error(channelService.toString());
		log.error(messageService.toString());
		log.error(userAuthService.toString());
		log.error(readStatusService.toString());
		log.error(userStateService.toString());
		log.error(binaryContentService.toString());

		User ordinaryUser = new User("userA", "userA@gmail.com", "passwordA", UserRole.ROLE_COMMON);
		User ordinaryUser2 = new User("userB", "userB@gmail.com", "passwordB", UserRole.ROLE_ADMIN);

//		initializeJsonFiles();

		System.out.println("===");
		System.out.println("[유저 서비스 테스트]");
		UserCommonRequest userCommonRequest = getUserCommonRequest(ordinaryUser.getUserName(), ordinaryUser.getUserEmail(), ordinaryUser.getUserPassword(), ordinaryUser.getUserRole());
		UserCommonRequest userCommonRequest2 = getUserCommonRequest(ordinaryUser2.getUserName(), ordinaryUser2.getUserEmail(), ordinaryUser2.getUserPassword(), ordinaryUser2.getUserRole());
		BinaryContent binaryContent = new BinaryContent(UUID.randomUUID(), "profileimg", "png");

		System.out.println("유저 생성");
		UserCommonResponse user = userService.createUser(userCommonRequest);
		UserCommonResponse userWithProfile = userService.createUserWithProfile(userCommonRequest2, binaryContent);
		System.out.println(user);

		System.out.println("\n유저 조회");
		UserResponse response = userService.find(user.userId());
		System.out.println("UserId: " + response.getUserStatus().getUserId());
		System.out.println("UserName: " + response.getUserName());
		System.out.println("UserEmail: " + response.getUserEmail());

		System.out.println("\n프사 있는 유저의 프사 파일 조회");
		Map<UUID, BinaryContentResponse> byUserId = binaryContentService.findByUserId(userWithProfile.userId());
		byUserId.values().forEach(bc -> System.out.println("fileName = " + bc.fileName() +
				", fileType = " + bc.fileType()));


		System.out.println("\n유저 모든 조회");
		Map<UUID, UserResponse> findAllUsers = userService.findAll();
		System.out.println(findAllUsers.values());

		System.out.println("\n유저 업데이트");
		UserCommonRequest updateRequest = new UserCommonRequest(user.userId(), "updateName", "update@gmail.com", "updatepwd");
		UserCommonResponse updatedUser = userService.update(user.userId(), updateRequest);
		System.out.println(updatedUser);

		System.out.println("\n유저 삭제 후 모든 유저 출력");
		userService.deleteUser(userWithProfile.userId());
		Map<UUID, UserResponse> users = userService.findAll();
		users.values().forEach(v -> System.out.println("userName: " + v.getUserName() + ", email: " + v.getUserEmail()));


		System.out.println("===");


		System.out.println("\n\n===");
		System.out.println("[채널 서비스 테스트]");
		System.out.println("채널 생성");

		ChannelCommonRequest channelCommonRequest = new ChannelCommonRequest(UUID.randomUUID(), "public_ch.1", ordinaryUser);
		ChannelResponse publicChannel = channelService.createPublicChannel(channelCommonRequest, new HashMap<>());
		System.out.println(publicChannel);

		System.out.println("채널 조회");
		ChannelFindResponse findChannel = channelService.findChannelById(publicChannel.channelId());
		System.out.println(findChannel);

		System.out.println("\n채널 업데이트");
		ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(publicChannel.channelId(), "변경된 채널", "PUBLIC", ordinaryUser);
		channelService.updateChannel(channelUpdateRequest);
		ChannelFindResponse updateChannel = channelService.findChannelById(publicChannel.channelId());
		System.out.println(updateChannel);

		System.out.println("\n채널 삭제");
		channelService.removeChannelById(publicChannel.channelId());
		Map<UUID, ChannelFindResponse> allChannels = channelService.getAllChannels(user.userId());
		System.out.println(allChannels);

		System.out.println("\n====================");
		System.out.println("====================");




		// 메세지 서비스 테스트를 위한 채널 및 유저 생성
		ChannelResponse pubCh = channelService.createPublicChannel(channelCommonRequest, new HashMap<>());
		UserCommonRequest userRequest1 = new UserCommonRequest(ordinaryUser.getId(), ordinaryUser.getUserName(), ordinaryUser.getUserEmail(), ordinaryUser.getUserPassword());
		UserCommonRequest userRequest2 = new UserCommonRequest(ordinaryUser2.getId(), ordinaryUser2.getUserName(), ordinaryUser2.getUserEmail(), ordinaryUser2.getUserPassword());
		userService.createUser(userRequest1);
		userService.createUser(userRequest2);

		System.out.println("\n\n===");
		System.out.println("[메세지 서비스 테스트]");
		System.out.println("메세지 생성");
		MessageCreateRequest messageCreateRequest = new MessageCreateRequest("안녕하세요", "반갑습니다.", ordinaryUser, ordinaryUser2);
		MessageCreateRequest messageCreateRequest2 = new MessageCreateRequest("답장입니다.", "ㅎㅇ", ordinaryUser2, ordinaryUser);
		MessageCreateResponse message = messageService.createMessage(messageCreateRequest);
		MessageCreateResponse message2 = messageService.createMessage(messageCreateRequest2);
		System.out.println(message);

		System.out.println("\n메세지 조회");
		MessageResponse findMessage = messageService.getMessageById(message.messageId());
		System.out.println(findMessage);

		System.out.println("\n메세지에 파일 첨부해서 생성");
		MessageCreateRequest createRequestWithFile = new MessageCreateRequest("파일 첨부", "같이하기.", ordinaryUser, ordinaryUser2);
		MessageSendFileRequest fileRequest = new MessageSendFileRequest(user.userId(), "codeit_sprint", "jar");
		MessageCreateResponse fileInMessage = messageService.createMessage(createRequestWithFile, fileRequest);
		System.out.println(fileInMessage);
		Map<UUID, BinaryContentResponse> userSendFile = binaryContentService.findByUserId(user.userId());
		userSendFile.values().forEach(bc -> System.out.println("fileName = " + bc.fileName() + ", fileType = " + bc.fileType()));

		System.out.println("\n채널 안 모든 메세지 조회");
		ChannelAddMessageRequest addRequest = new ChannelAddMessageRequest(pubCh.channelId(), message.messageId());
		ChannelAddMessageRequest addRequest2 = new ChannelAddMessageRequest(pubCh.channelId(), message2.messageId());
		channelService.sendMessage(addRequest);
		channelService.sendMessage(addRequest2);


		Map<UUID, MessageResponse> allMessages = messageService.findAllByChannelId(pubCh.channelId());
		System.out.println(allMessages);

		System.out.println("\n메세지 업데이트");
		MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(pubCh.channelId() ,message.messageId(), "새로운 제목", "새로운 내용");
		MessageResponse updateMessage = messageService.updateMessage(messageUpdateRequest);
		System.out.println(updateMessage);

		System.out.println("\n메세지 삭제 후 전체 조회");
		messageService.deleteMessage(message.messageId());
		System.out.println(messageService.findAllByChannelId(pubCh.channelId()));


		System.out.println("\n\n===");
		System.out.println("[유저 Auth 서비스 테스트]");
		System.out.println("올바른 로그인 시도");
		UserLoginRequest loginRequest = new UserLoginRequest("updateName", "updatepwd");
		UserLoginResponse loginedUser = userAuthService.login(loginRequest);
		System.out.println(loginedUser);

		System.out.println("\n잘못된 로그인 시도");
		UserLoginRequest loginRequest2 = new UserLoginRequest("adssadsad", "1251dcasdwq");
		UserLoginResponse loginedUser2 = userAuthService.login(loginRequest2);
		System.out.println(loginedUser2);
	}

	private static UserCommonRequest getUserCommonRequest(String userName, String email, String pwd, UserRole role) {
		User user = new User(userName, email, pwd, role);
		return new UserCommonRequest(user.getId() ,user.getUserName(), user.getUserEmail(), user.getUserPassword());
	}

/*	// 테스트 할때마다 json 초기화
	public static void initializeJsonFiles() throws IOException {
		String projectRoot = System.getProperty("user.dir");
		ObjectMapper objectMapper = new ObjectMapper();

		String[] fileNames = {
				"usersStatus.json",
				"users.json",
				"readStatus.json",
				"messages.json",
				"channels.json",
				"binaryContent.json"
		};

		for (String fileName : fileNames) {
			ObjectNode rootNode = objectMapper.createObjectNode();

			Path filePath = Paths.get(projectRoot, fileName);
			objectMapper.writerWithDefaultPrettyPrinter()
					.writeValue(filePath.toFile(), rootNode);
		}
	}*/
}
