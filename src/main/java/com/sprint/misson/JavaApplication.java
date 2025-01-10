package com.sprint.misson;

import com.sprint.misson.discordeit.entity.*;
import com.sprint.misson.discordeit.service.ChannelService;
import com.sprint.misson.discordeit.service.MessageService;
import com.sprint.misson.discordeit.service.UserService;
import com.sprint.misson.discordeit.service.jcf.JCFChannelService;
import com.sprint.misson.discordeit.service.jcf.JCFMessageService;
import com.sprint.misson.discordeit.service.jcf.JCFUserService;

import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        UserService userService=new JCFUserService();
        ChannelService channelService=new JCFChannelService();
        MessageService messageService=new JCFMessageService();

        //TODO - 심화
        //서비스 간 의존성 주입

        //1. 등록
        System.out.println("=== 1. 등록 테스트 ===");

        System.out.println("--- User 등록---");
        User user1 = userService.createUser( "코드잇", "codeit@codeit.co.kr" );
        User user2 = userService.createUser( "박유진","yudility@gmail.com" );
        User user3 = userService.createUser( "홍길동", "gildong@naver.com" );

        System.out.println("> 유저 생성 결과: " );
        System.out.println("- user1 생성 성공: " + (user1!=null));
        System.out.println("- user2 생성 성공: " + (user2!=null));
        System.out.println("- user3 생성 성공: " + (user3!=null));


        //Todo invalid 값도 추가하여 예외처리하기.
        System.out.println("\n* 중복 이메일 케이스");
        System.out.println("-> 닉네임: \"중복\", 이메일: \"codeit@codeit.co.kr\"로 생성 시도");
        User userDuplicateEmail  = userService.createUser("중복", "codeit@codeit.co.kr");
        System.out.println("> 중복 이메일 생성 결과: " + (userDuplicateEmail!= null) );

        System.out.println("\n> 전체 유저 목록: ");
        userService.getUsers().forEach(
                u -> System.out.println(
                        "[User]"
                        + "\n - id: " + u.getId().toString()
                        + "\n - nickName: "+ u.getNickname()
                        + "\n - email: "+ u.getEmail()
                )
        );

        System.out.println("\n\n--- Channel 등록---");
        Channel channel1 = channelService.CreateChannel("일반1", ChannelType.TEXT);
        Channel channel2 = channelService.CreateChannel("일반2", ChannelType.TEXT);
        Channel channel3 = channelService.CreateChannel("공지사항", ChannelType.TEXT);
        Channel channel4 = channelService.CreateChannel("회의실", ChannelType.VOICE);

        System.out.println("> 채널 생성 결과:");
        System.out.println("- channal1 생성: " + (channel1 != null) );
        System.out.println("- channal2 생성: " + (channel2 != null));
        System.out.println("- channal3 생성: " +(channel3 != null));
        System.out.println("- channal4 생성" + (channel4 != null));


        System.out.println("> 전체 채널 목록:");
        channelService.getChannels()
                .forEach(c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - name: "+ c.getChannelName()
                                + "\n - type: "+ c.getChannelType()
                ));

        System.out.println("\n\n--- Massage 등록---");

        Message msg1 = messageService.createMessage(user1, "일반1 채널입니다!", channel1);
        Message msg2 = messageService.createMessage(user3, "반갑습니다!", channel1);
        Message msg3 = messageService.createMessage(user1, "일반2 채널입니다.", channel2);
        Message msg4 = messageService.createMessage(user2, "공지사항 채널입니다! 반갑습니다.", channel3);
        Message msg5 = messageService.createMessage(user1, "회의실 채널입니다.", channel4);

        System.out.println("> 메시지 생성 결과:");
        System.out.println("- 메시지1: " + msg1);
        System.out.println("- 메시지2: " + msg2);
        System.out.println("- 메세지3: " + msg3);
        System.out.println("- 메세지4: " + msg4);
        System.out.println("- 메세지5: " + msg5);


        System.out.println("\n> 전체 메시지 목록:");
        messageService.getMessages()
                .forEach(m -> System.out.println(
                        "[Message]"
                        + "\n - id: " + m.getId().toString()
                        + "\n - sender: "+ m.getSender().getNickname()
                        + "\n - channel: "+ m.getChannel().getChannelName()
                        + "\n - content: "+ m.getContent()
                ));

        System.out.println();
        System.out.println("\n=== 2. 조회 테스트 ===");

        System.out.println("\n--- User 조회 ---");

        System.out.println("\n1) 전체 목록 조회");
        List<User> tempUsers = userService.getUsers();

        System.out.println( "\n> 조회 결과");
        tempUsers.forEach(
                u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: "+ u.getNickname()
                                + "\n - email: "+ u.getEmail()
                )
        );

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForTest =tempUsers.get( 0 ).getId().toString();

        System.out.println("> 조회할 UUID: "+ uuidStringForTest);
        User userByUUID=userService.getUserByUUID( uuidStringForTest );
        System.out.println( "\n> 조회 결과\n" +
                "[User]"
                + "\n - id: " + userByUUID.getId().toString()
                + "\n - nickName: "+ userByUUID.getNickname()
                + "\n - email: "+ userByUUID.getEmail());

        System.out.println("\n3) 이메일로 조회(단건): ");
        User foundUser = userService.getUserByEmail("codeit@codeit.co.kr");
        System.out.println("- codeit@codeit.co.kr을 이메일로 가진 유저 이름: "+foundUser.getNickname());
        User notFoundUser = userService.getUserByEmail("no_one@test.com");
        System.out.println("- no_one@test.com을 이메일로 가진 유저 이름: "+foundUser.getNickname());


        System.out.println("\n4) 닉네임으로 조회(다건): ");
        List<User> result = userService.getUsersByNickname( "박유진" );
        System.out.println("\"박유진\" 닉네임을 가진 User 조회 결과: ");
        result.forEach( u -> System.out.println(
                "[User]"
                        + "\n - id: " + u.getId().toString()
                        + "\n - nickName: " + u.getNickname()
        ) );

        System.out.println("\n5) 계정 상태로 조회(다건): ");
        System.out.println("> 활동중(ACTIVE)인 User 조회 결과: ");
        userService.getUserByUserStatus( UserStatus.ACTIVE )
                .forEach( u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: " + u.getNickname()
                                + "\n - userStatus: " + u.getUserStatus()
                ));

        System.out.println("\n\n--- Channel 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<Channel> tempChannels = channelService.getChannels();
        tempChannels.forEach(
                u -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - channelName: "+ u.getChannelName()
                )
        );

        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForChannelTest =tempChannels.get( 0 ).getId().toString();

        System.out.println("> 조회할 UUID: "+ uuidStringForChannelTest);
        Channel channelByUUID =channelService.getChannelByUUID( uuidStringForChannelTest );
        System.out.println( "\n> 조회 결과\n" +
                "[Channel]"
                + "\n - id: " + channelByUUID.getId().toString()
                + "\n - channelName: "+ channelByUUID.getChannelName()
                + "\n - type: "+ channelByUUID.getChannelType());


        System.out.println("\n3) 채널 이름으로 조회(다건)");
        System.out.println("> 이름에 \"일반\"이 포함된 채널 조회 결과:");
        channelService.getChannelsByName( "일반" )
                .forEach( c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - channelName: " + c.getChannelName()
                ) );


        System.out.println("\n4) 채널 type(종류)으로 조회(다건)");
        System.out.println("> Channal Type이 text인 채널 조회 결과: ");
        channelService.getChannelByType( ChannelType.TEXT )
                .forEach( c-> System.out.println(
                        "[Channel]"
                        + "\n - id: " + c.getId().toString()
                        + "\n - channelName: " + c.getChannelName()
                        + "\n - type: "+ c.getChannelType()
                ));



        System.out.println("\n--- Message 조회 ---");
        System.out.println("\n1) 전체 목록 조회");
        List<Message> messageList = messageService.getMessages();
        messageList
                .forEach(m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ));


        System.out.println("\n2) UUID로 조회(단건)");
        String uuidStringForMessageTest = messageList.get( 0 ).getId().toString();

        System.out.println("- 조회할 UUID: "+ uuidStringForMessageTest);
        Message messageByUUID =messageService.getMessageByUUID( uuidStringForMessageTest );
        System.out.println( "> 조회 결과\n" +
                "[Message]"
                        + "\n - id: " + messageByUUID.getId().toString()
                        + "\n - sender: "+ messageByUUID.getSender().getNickname()
                        + "\n - channel: "+ messageByUUID.getChannel().getChannelName()
                        + "\n - content: "+ messageByUUID.getContent());



        System.out.println("\n3) 내용으로 조회(다건)");
        System.out.println("> 내용에 \"반갑습니다\"가 포함된 메세지 조회: ");
        messageService.getMessageByContent( "반갑습니다" )
                .forEach( m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ) );


        System.out.println("\n4) 작성자로 조회(다건)");
        System.out.println("> nickname이 \"박유진\"인 사용자가 작성한 메세지 조회: ");
        User sender=userService.getUsersByNickname( "박유진" ).get( 0 );
        messageService.getMessageBySender( sender )
                .forEach( m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ) );

        System.out.println("\n5)특정 채널에 속하는 메세지 조회(다건)");
        System.out.println("> \"일반1\" 채널의 전체 메세지 목록:");
        messageService.getMessagesByChannel(channel1)
                .forEach( m-> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ) );


        // 3. 수정 테스트
        System.out.println("\n=== 3. 수정 테스트 ===");

        System.out.println("\n--- User 수정 ---");
        System.out.println("-> User1의 이름을 '코드잇'에서 'codeit'으로 수정: ");

        System.out.println("\n> 전체 유저 목록 조회: ");
        userService.getUsers().forEach(
                u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: "+ u.getNickname()
                                + "\n - email: "+ u.getEmail()
                                + "\n - createdAt: "+u.getCreatedAt().toString()
                                + "\n - updatedAt: "+u.getUpdatedAt().toString()
                )
        );

        user1.updateNickname("codeit");
        userService.updateUser(user1);

        System.out.println("\n> 수정 후 유저 목록: ");
        userService.getUsers().forEach(
                u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: "+ u.getNickname()
                                + "\n - email: "+ u.getEmail()
                                + "\n - createdAt: "+u.getCreatedAt().toString()
                                + "\n - updatedAt: "+u.getUpdatedAt().toString()
                )
        );


        System.out.println("\n\n--- Channel 수정 ---");
        System.out.println("-> channel1의 이름을 '일반1'에서 '수다방'으로 수정: ");

        System.out.println("\n> 전체 채널 목록:");
        channelService.getChannels()
                .forEach(c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - name: "+ c.getChannelName()
                                + "\n - type: "+ c.getChannelType()
                                + "\n - createdAt: "+c.getCreatedAt().toString()
                                + "\n - updatedAt: "+c.getUpdatedAt().toString()
                ));

        channel1.updateChannelName( "수다방" );
        channelService.updateChannel( channel1 );

        System.out.println("\n> 수정 후 채널 목록: ");
        channelService.getChannels()
                .forEach(c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - name: "+ c.getChannelName()
                                + "\n - type: "+ c.getChannelType()
                                + "\n - createdAt: "+c.getCreatedAt().toString()
                                + "\n - updatedAt: "+c.getUpdatedAt().toString()
                ));

        System.out.println("\n--- Message 수정 ---");
        System.out.println("\n -> msg1ForChannel1의 내용을 '일반1 채널입니다!'에서 '수다방 채널입니다!'로 수정 ");

        System.out.println("\n> 전체 메세지 목록: ");
        messageService.getMessages()
                .forEach(m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                                + "\n - createdAt: "+m.getCreatedAt().toString()
                                + "\n - updatedAt: "+m.getUpdatedAt().toString()
                ));

        //todo
        //update관련 질문
        msg1.updateContent( "수다방 채널입니다!" );
        messageService.updateMessage( msg1 );

        System.out.println("\n> 수정 후 메세지 목록: ");
        messageService.getMessages()
                .forEach(m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                                + "\n - createdAt: "+m.getCreatedAt().toString()
                                + "\n - updatedAt: "+m.getUpdatedAt().toString()
                ));


        // 4. 삭제 테스트
        System.out.println("\n\n=== 4. 삭제 테스트 ===");

        //의존 관계를 고려하여 역순으로 진행
        System.out.println("\n--- Message 삭제 ---");
        System.out.println("\n -> 이름이 \"홍길동\"인 user가 작성한 첫번째 메세지 삭제 ");

        System.out.println("\n> 전체 메세지 목록: ");
        messageService.getMessages()
                .forEach(m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ));

        User senderMsg = userService.getUsersByNickname( "홍길동" ).get( 0 );
        Message msgToDelete=messageService.getMessageBySender( senderMsg ).get( 0 );
        messageService.deleteMessage( msgToDelete );

        System.out.println("\n> 삭제 후 메세지 목록: ");
        messageService.getMessages()
                .forEach(m -> System.out.println(
                        "[Message]"
                                + "\n - id: " + m.getId().toString()
                                + "\n - sender: "+ m.getSender().getNickname()
                                + "\n - channel: "+ m.getChannel().getChannelName()
                                + "\n - content: "+ m.getContent()
                ));


        System.out.println("\n--- Channel 삭제 ---");
        System.out.println("\n -> 채널 이름에 \"일반\"이 들어간 채널 삭제");

        System.out.println("\n> 전체 채널 목록:");
        channelService.getChannels()
                .forEach(c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - name: "+ c.getChannelName()
                                + "\n - type: "+ c.getChannelType()
                ));

        channelService.getChannelsByName( "일반" )
                .forEach( channelService::deleteChannel );


        System.out.println("\n삭제 후 채널 목록: ");
        channelService.getChannels()
                .forEach(c -> System.out.println(
                        "[Channel]"
                                + "\n - id: " + c.getId().toString()
                                + "\n - name: "+ c.getChannelName()
                                + "\n - type: "+ c.getChannelType()
                ));


        System.out.println("\n--- User 삭제 ---");
        System.out.println("\n -> email이 \"gildong@naver.com\"인 User 삭제");

        System.out.println("\n> 전체 유저 목록 조회: ");
        userService.getUsers().forEach(
                u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: "+ u.getNickname()
                                + "\n - email: "+ u.getEmail()
                )
        );

        userService.deleteUser( userService.getUserByEmail( "gildong@naver.com" ) );

        System.out.println("\n> 삭제 후 유저 목록: ");
        userService.getUsers().forEach(
                u -> System.out.println(
                        "[User]"
                                + "\n - id: " + u.getId().toString()
                                + "\n - nickName: "+ u.getNickname()
                                + "\n - email: "+ u.getEmail()
                )
        );

    }
}

