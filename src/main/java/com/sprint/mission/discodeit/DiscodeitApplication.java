package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.channel.ChannelPrivateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		/**시간이 없어서 jUnit은 못 썼습니다 ㅠ!! **/


//		// ConfigurableApplicationContext
//		// 스프링의 빈을 다 가지고 있는 메인 컨테이너
		ConfigurableApplicationContext context =SpringApplication.run(DiscodeitApplication.class, args);

		// 서비스 초기화
			// context에서 Bean을 조회하여 각 서비스 구현체 할당 코드 작성하세요.
		UserService userService = context.getBean("basicUserService", UserService.class);
		ChannelService channelService = context.getBean("basicChannelService", ChannelService.class);
		MessageService messageService = context.getBean("basicMessageService",MessageService.class);

		// 셋업
		UUID userId = setupUser(userService);

		/*

		// 조회
		System.out.println("showAllUsers");
		System.out.println(userService.showAllUsers());
		System.out.println("getUserById");
		System.out.println(userService.getUserById(userId));

		// 수정
		System.out.println("updateUser");
		updateUser(userId, userService);

		System.out.println(userService.getUserById(userId));

		// 삭제
		System.out.println("removeUserById");
		userService.removeUserById(userId);
		System.out.println(userService.getUserById(userId)); // Exception in thread "main" java.lang.RuntimeException:  No  + {id} User exists.


		 */



		// 셋업
		UUID publicChannelId = setupPublicChannel(userId, channelService);
		UUID privateChannelId = setupPrivateChannel(userId, channelService);

		/*
		System.out.println();
		// 조회
		System.out.println("findAllByUserId");
		System.out.println(channelService.findAllByUserId(userId));

		System.out.println("getChannelById");
		System.out.println(channelService.getChannelById(publicChannelId));
		System.out.println(channelService.getChannelById(privateChannelId));

		System.out.println();
		// 수정
		System.out.println("updateChannel");
		updateChannel(publicChannelId, channelService);
		System.out.println(channelService.getChannelById(publicChannelId));

		System.out.println();
		// 삭제
		System.out.println("deleteChannelById");
		channelService.deleteChannelById(publicChannelId);
		System.out.println(channelService.getChannelById(publicChannelId)); // Exception in thread "main" java.util.NoSuchElementException: 해당 채널이 없습니다.
		*/


		//
 		// 셋업
		UUID messageId = setupMessageTest(publicChannelId, userId, messageService);

		// 조회
		System.out.println("findAllByChannelId");
		System.out.println(messageService.findAllByChannelId(publicChannelId));

		System.out.println("getMessageById");
		System.out.println(messageService.getMessageById(messageId));

//		// 수정
//		System.out.println("updateMessage");
//		updateMessage(messageId, "메세지 내용 수정", messageService);
//		System.out.println(messageService.getMessageById(messageId));
//
//
//		// 삭제
//		messageService.deleteMessageById(messageId);
//		System.out.println(messageService.getMessageById(messageId)); // Exception in thread "main" java.util.NoSuchElementException: 해당 메세지가 없습니다.

	}
	static UUID setupUser(UserService userService) {
		UserCreateRequest userCreateRequest = new UserCreateRequest("유저1", "AEFSFD@gmail.com", "2ASFD1ASDF456");
		return userService.createUser(userCreateRequest);
	}

	static void updateUser(UUID userId, UserService userService){
		UserUpdateRequest userUpdateRequest =
				new UserUpdateRequest(userId,
						"새이름",
						"newemail@gmail.com",
						"2ASFD1ASDF456",
						null);
		userService.updateUserInfo(userUpdateRequest);
	}
	//


	static UUID setupPublicChannel(UUID ownerId, ChannelService channelService) {
		ChannelPublicRequest channelPublicRequest =
				new ChannelPublicRequest(ChannelType.PUBLIC, ownerId, "공개 채널", "공개 채널입니다.");
		return channelService.createPublicChannel(channelPublicRequest);
	}

	static UUID setupPrivateChannel(UUID ownerId, ChannelService channelService) {
		ChannelPrivateRequest channelPrivateRequest =
				new ChannelPrivateRequest(ChannelType.PRIVATE, ownerId);
		return channelService.createPrivateChannel(channelPrivateRequest);
	}
	static void updateChannel(UUID publicChannelId, ChannelService channelService){

		ChannelUpdateRequest channelUpdateRequest =
				new ChannelUpdateRequest(publicChannelId, "공개 채널 이름 변경", "공개 채널 설명 변경");
		channelService.updateChannel(channelUpdateRequest);
	}


	//
	static UUID setupMessageTest(UUID publicChannelId, UUID userId, MessageService messageService) {
		MessageCreateRequest messageCreateRequest =
				new MessageCreateRequest(publicChannelId, userId, "메시지 내용 작성", 0);
		return messageService.createMessage(messageCreateRequest);
	}
	static void updateMessage(UUID messageId, String newMessage, MessageService messageService){
		MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(messageId, newMessage);
		messageService.updateMessageText(messageUpdateRequest);
	}

}
