package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.entity.Channel;
import com.sprint.mission.discodeit.dto.entity.ChannelGroup;
import com.sprint.mission.discodeit.dto.entity.Message;
import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.dto.form.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.form.MessageWithContents;
import com.sprint.mission.discodeit.dto.form.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.form.PublicChannelDto;
import com.sprint.mission.discodeit.dto.form.UserUpdateDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.NetworkService;
import com.sprint.mission.discodeit.util.JavaAppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		ApplicationContext ac=SpringApplication.run(DiscodeitApplication.class, args);
		UserService userService=ac.getBean(BasicUserService.class);
		ChannelService channelService=ac.getBean(BasicChannelService.class);
		MessageService messageService=ac.getBean(BasicMessageService.class);

		Scanner scanner = new Scanner(System.in);
		System.out.print("접속 주소를 입력해 주세요:");
		String input = scanner.nextLine();
		NetworkService networkService = new NetworkService();
		try{
			networkService.connect(input);
			System.out.println();
			User alice = new User("alice","alice123","Alice", "alice@example.code");
			User hyun = new User("hyun","hyun123","Hyun", "hyun@example.code");
			User yull = new User("yull","yull123","Yull", "yull@example.code");
			userService.createUser(alice);
			userService.createUser(hyun);
			userService.createUser(yull);
			log.info("사용자 조회: {}", userService.findByloginId("alice"));
			log.info("전체 사용자 조회: {}", userService.findAllUsers());
			System.out.println();
			UserUpdateDto aliceUpdate = new UserUpdateDto("updatealice", "alice@example.code", "alice1234", "alice");
			userService.updateUser(alice.getId(), aliceUpdate);
			log.info("업데이트 후 사용자 조회: " + userService.findAllUsers());
			userService.deleteUser(alice.getId());
			log.info("삭제 후 사용자 조회: " + userService.findAllUsers());
			System.out.println();


			Channel hiChannel = new Channel("hiChannel","Hi", ChannelGroup.PUBLIC);
			Channel byeChannel = new Channel("byeChannel","Bye", ChannelGroup.PUBLIC);
			Channel howChannel = new Channel("How", "How about u?", ChannelGroup.PRIVATE);
			PublicChannelDto publicChannelHi = new PublicChannelDto(hiChannel);
			PublicChannelDto publicChannelBye = new PublicChannelDto(byeChannel);
			PrivateChannelDto privateChannel= new PrivateChannelDto(howChannel);

			channelService.createPublicChannel(publicChannelHi,hyun.getId());
			channelService.createPublicChannel(publicChannelBye,hyun.getId());
			channelService.createPrivateChannel(privateChannel,yull.getId());
			log.info("전체 채널 조회: " + channelService.findAllChannels());
			log.info("private 채널 조회: " + channelService.findPrivateChannel(privateChannel.getId()));
			log.info("사용자 채널 조회: "+channelService.findAllByUserId(yull.getId()));
			System.out.println();

			ChannelUpdateDto byeUpdate = new ChannelUpdateDto("byeUpdateChannel", "Updated");
			channelService.updateChannel(publicChannelBye.getId(), byeUpdate);
			log.info("업데이트 후 채널 조회: " + channelService.findAllChannels());

			List<UUID> channelListbyHyun = channelService.findAllByUserId(hyun.getId());
			log.info("hyun의 채널 리스트: " + channelListbyHyun);

			channelService.deleteChannel(byeChannel.getId());
			log.info("삭제 후 채널 조회: " + channelService.findAllChannels());
			System.out.println();


			BinaryContent binaryContent= new BinaryContent("testFile.txt",100L,"text/plain","this is test".getBytes(StandardCharsets.UTF_8));
			MessageWithContents howMessage = new MessageWithContents(yull.getId(),howChannel.getId(),"Good",binaryContent);
			Message hiMessage = new Message("Hi I'm hyun", hyun.getId(), hiChannel.getId());
			messageService.messageSaveWithContents(howMessage,binaryContent);
			messageService.messageSave(hiMessage);
			log.info("전체 메세지 조회: " + messageService.findAllMessages());
			log.info("일부 메세지 조회:" + messageService.findMessage(howMessage.getId()));
			System.out.println();

			messageService.updateMessage(howMessage.getId(),"How Update");
			log.info("업데이트 후 메세지 조회: " + messageService.findAllMessages());

			messageService.deleteMessage(hiMessage.getId());
			log.info("삭제 후 메세지 조회: " + messageService.findAllMessages());
			log.info("howChannel 의 메세지: "+messageService.findAllByChannelId(howChannel.getId()));

		}catch (Exception e) {
			exceptionHandler(e);
		}finally {
			networkService.disconnect();
		}

		System.out.println();
		log.info("프로그램을 정상 종료합니다.");
	}


	private static void exceptionHandler(Exception e) {
		System.out.println("사용자 메세지: 죄송합니다. 알 수 없는 문제가 발생했습니다.");
		System.out.println("==개발자용 디버깅 메세지==");
		e.printStackTrace(System.out);
	}
}
