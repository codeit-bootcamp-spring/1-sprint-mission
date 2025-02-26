package com.sprint.mission.discodeit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
//		ApplicationContext ac=SpringApplication.run(DiscodeitApplication.class, args);
		SpringApplication.run(DiscodeitApplication.class, args);
		/*스프린트 5에서는 삭제할 예정입니다.
		UserService userService=ac.getBean(UserService.class);
		ChannelService channelService=ac.getBean(ChannelService.class);
		MessageService messageService=ac.getBean(MessageService.class);
		LoginService loginService=ac.getBean(LoginService.class);

		Scanner scanner = new Scanner(System.in);
		System.out.print("접속 주소를 입력해 주세요:");
		String input = scanner.nextLine();
		NetworkService networkService = new NetworkService();
		try{
			networkService.connect(input);
			System.out.println();
			BinaryContent profile= new BinaryContent("profile.png",100L,"image/png","this is test".getBytes(StandardCharsets.UTF_8));
			User alice = new User("alice","alice123","Alice", "alice@example.code",profile);
			User hyun = new User("hyun","hyun123","Hyun", "hyun@example.code");
			User yull = new User("yull","yull123","Yull", "yull@example.code");
			userService.createUser(alice);
			userService.createUser(hyun);
			userService.createUser(yull);
			log.info("사용자 조회: {}", userService.findByloginId("alice"));
			log.info("전체 사용자 조회: {}", userService.findAllUsers());
			System.out.println();
			BinaryContent updateProfile= new BinaryContent("profile2.png",100L,"image/png","this is update".getBytes(StandardCharsets.UTF_8));
			UserUpdateDto aliceUpdate = new UserUpdateDto("updatealice", "alice@example.code", "alice1234", "alice");
			userService.updateUser(alice.getId(), aliceUpdate);
			userService.updateProfile(alice.getId(), updateProfile);
			log.info("업데이트 후 사용자 조회: " + userService.findAllUsers());
			userService.deleteUser(alice.getId());
			log.info("삭제 후 사용자 조회: " + userService.findAllUsers());
			System.out.println();

			loginService.login("hyun", "hyun123");
			loginService.login("yull", "yull123");

			Channel hiChannel = new Channel("hiChannel","Hi", ChannelGroup.PUBLIC);
			Channel byeChannel = new Channel("byeChannel","Bye", ChannelGroup.PUBLIC);
			Channel howChannel = new Channel("How", "How about u?", ChannelGroup.PRIVATE);

			PublicChannelDto publicChannelHi = new PublicChannelDto(hiChannel);
			PublicChannelDto publicChannelBye = new PublicChannelDto(byeChannel);
			PrivateChannelDto privateChannel= new PrivateChannelDto(howChannel);

			Participant hiParticipation = new Participant(yull);
			Participant hiParticipation2 = new Participant(hyun);

			Participant byeParticipation = new Participant(hyun);

			Participant howParticipation=new Participant(hyun);
			Participant howParticipation2=new Participant(yull);

			channelService.createPublicChannel(publicChannelHi,hiParticipation,hiParticipation2);
			channelService.createPublicChannel(publicChannelBye,byeParticipation);
			channelService.createPrivateChannel(privateChannel,howParticipation,howParticipation2);
			log.info("전체 Public채널 조회: " + channelService.findAllPublicChannels());
			log.info("전체 Private채널 조회: " + channelService.findAllPrivateChannels());
			log.info("public 채널 조회: " + channelService.findById(publicChannelHi.getId()));
			log.info("private 채널 조회: " + channelService.findById(privateChannel.getId()));
			log.info("사용자 채널 조회: "+channelService.findAllByLoginId(yull.getLoginId()));
			System.out.println();

			ChannelUpdateDto byeUpdate = new ChannelUpdateDto("byeUpdateChannel", "Updated");
			channelService.updateChannel(byeChannel.getId(), byeUpdate);
			log.info("업데이트 후 채널 조회: " + channelService.findAllPublicChannels());
			List<UUID> channelListbyHyun = channelService.findAllByLoginId(hyun.getLoginId());
			log.info("hyun의 채널 리스트: " + channelListbyHyun);

			channelService.deleteChannel(byeChannel.getId());
			log.info("삭제 후 채널 조회: " + channelService.findAllPrivateChannels());
			log.info("삭제 후 채널 조회: " + channelService.findAllPublicChannels());
			System.out.println();

			BinaryContent binaryContent= new BinaryContent("testFile.txt",100L,"text/plain","this is test".getBytes(StandardCharsets.UTF_8));
			BinaryContent binaryContent2= new BinaryContent("testFile2.txt",200L,"text/plain","this is test2".getBytes(StandardCharsets.UTF_8));
			List<BinaryContent> binaryList=new ArrayList<>();
			binaryList.add(binaryContent);
			binaryList.add(binaryContent2);

			Message howMessage = new Message("Good",yull.getId(),howChannel.getId());
			Message hiMessage = new Message("Hi I'm hyun with file", hyun.getId(), hiChannel.getId());
			messageService.messageSaveWithContents(hiMessage.getSenderId(),hiMessage,binaryList);
			messageService.messageSave(howMessage.getSenderId(),howMessage);
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
	}*/
	}
}
