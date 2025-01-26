package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JcfChannelService;
import com.sprint.mission.discodeit.service.jcf.JcfMessageService;
import com.sprint.mission.discodeit.service.jcf.JcfUserService;
import com.sprint.mission.discodeit.service.jcf.NetworkService;

import java.nio.channels.FileChannel;
import java.util.Scanner;
import java.util.UUID;

public class TestMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("접속 주소를 입력해 주세요:");
        String input = scanner.nextLine();

        JcfUserService jcfUserService = new JcfUserService();
        JcfMessageService jcfMessageService = new JcfMessageService();
        JcfChannelService jcfChannelService = new JcfChannelService();
        NetworkService networkService = new NetworkService();
        //        FileUserService fileUserService = new FileUserService();
       /* FileMessageService fileMessageService = new FileMessageService();
         FileChannelService fileChannelService = new FileChannelService();*/

        try{
            networkService.connect(input);
             System.out.println();


       /* while(true){
            System.out.print("메뉴를 선택해주세요. 1. 유저 | 2. 채널 | 3. 조회 | 4. 수정 | 5. 삭제 | 6. 종료 ");
            int choice = scanner.nextInt();
            switch(choice) {
                case 1:
                    System.out.println("가입을 시작하겠습니다.");
                    System.out.print("이름 : ");
                    String userName = scanner.nextLine();
                    scanner.nextLine();
                    System.out.print("이메일 : ");
                    String email = scanner.nextLine();
                    User newUser = new User(userName, email);
                    jcfUserService.createUser(newUser);
                    System.out.println("가입이 완료되었습니다.");
                    UUID userId=newUser.getId();
                    System.out.println("당신의 유효ID :"+userId);
                    break;
                case 2:
                    System.out.println("채널을 생성하겠습니다.");
                    System.out.print("채널 이름:");
                    String channelName = scanner.nextLine();
                    scanner.nextLine();
                    System.out.print("채널 설명:");
                    String channelDescript = scanner.nextLine();
                    Channel newChannel = new Channel(channelName, channelDescript);
                    jcfChannelService.createChannel(newChannel);
                    UUID channelId=newChannel.getId();
                    System.out.println("채널 생성 완료. 채널의 고유ID: "+channelId);
                    System.out.print("사용자의 고유 ID를 입력해주세요");
                    UUID inputUserid=UUID.fromString(scanner.nextLine());
                    System.out.print("메세지를 입력해주세요.");
                    String messageContent = scanner.nextLine();
                    Message newMessage = new Message(messageContent,inputUserid,channelId);
                    UUID messageId=newMessage.getId();
                    System.out.println("메세지를 성공적으로 생성하였습니다.메세지 고유 ID:"+messageId);
                    break;
                case 3:
                    scanner.nextLine();
                    System.out.print("어떤 항목을 조회하고 싶으세요? (u: 사용자 | c: 채널 )");
                    String subChoice = scanner.nextLine();
                    if (subChoice.equals("u")) {
                        System.out.print("a: 전체 사용자 조회 | b: 사용자 조회");
                        String find = scanner.nextLine();
                        if (find.equals("a")) {
                            System.out.println(jcfUserService.findAllUsers());
                        } else if (find.equals("b")) {
                            System.out.print("고유 ID를 입력해주세요");
                            UUID userFindId=UUID.fromString(scanner.nextLine());
                            System.out.println(jcfUserService.findUser(userFindId));
                        } else {
                            System.out.println("잘못 입력하셨습니다.");
                        }
                    } else if (subChoice.equals("c")) {
                        System.out.print("a: 전체 채널 조회 | b: 채널 조회");
                        String find = scanner.nextLine();
                        if (find.equals("a")) {
                            System.out.println(jcfChannelService.findAllChannels());
                        } else if (find.equals("b")) {
                            System.out.print("채널의 고유 ID를 입력해주세요:");
                            UUID channelFindId=UUID.fromString(scanner.nextLine());
                            System.out.println(jcfChannelService.findChannel(channelFindId));
                            System.out.println("채널의 메세지 조회");
                            System.out.println(jcfMessageService.findAllMessages());
                        } else {
                            System.out.println("잘못 입력하셨습니다.");
                        }
                    }
                    break;
                case 4:
                    System.out.println("어떤 항목을 업데이트하고 싶으세요?(u: 사용자 | c: 채널 | m: 메세지)");
                    String subChoice1 = scanner.nextLine();
                    if (subChoice1.equals("u")) {
                        System.out.print("고유 ID를 입력해주세요");
                        UUID userFindId=UUID.fromString(scanner.nextLine());
                        System.out.print("a: 이름 수정 | b: 이메일 수정");
                        String toUpdate = scanner.nextLine();
                        if (toUpdate.equals("a")) {
                            System.out.print("업데이트 이름:");
                            String updateUsername = scanner.nextLine();
                            jcfUserService.updateUserName(userFindId, updateUsername);
                            System.out.println(jcfUserService.findUser(userFindId));
                        } else if (toUpdate.equals("b")) {
                            System.out.print("업데이트 이메일:");
                            String updateUseremail = scanner.nextLine();
                            jcfUserService.updateUserEmail(userFindId, updateUseremail);
                            System.out.println(jcfUserService.findUser(userFindId));
                        }
                    }else if (subChoice1.equals("c")) {
                        System.out.print("채널의 고유 ID를 입력해주세요:");
                        UUID channelFindId=UUID.fromString(scanner.nextLine());
                        System.out.print("a: 이름 수정 | b: 설명 수정");
                        String toUpdate = scanner.nextLine();
                        if (toUpdate.equals("a")) {
                            System.out.print("업데이트 이름:");
                            String updateChannelname = scanner.nextLine();
                            jcfChannelService.updateChannelName(channelFindId, updateChannelname);
                            System.out.println(jcfChannelService.findChannel(channelFindId));
                        } else if (toUpdate.equals("b")) {
                            System.out.print("업데이트 설명:");
                            String updateDescript = scanner.nextLine();
                            jcfChannelService.updateChannelDescription(channelFindId, updateDescript);
                            System.out.println(jcfChannelService.findChannel(channelFindId));
                        }
                    } else if (subChoice1.equals("m")) {
                        System.out.print("메세지의 고유 ID를 입력해주세요:");
                        UUID messageFindId=UUID.fromString(scanner.nextLine());
                        System.out.print("업데이트 내용:");
                        String updateMessage = scanner.nextLine();
                        jcfMessageService.updateMessage(messageFindId, updateMessage);
                        System.out.println(jcfMessageService.findMessage(messageFindId));
                    }
                    break;
                case 5:
                    System.out.println("어떤 항목을 삭제하고 싶으세요?(u: 사용자 | c: 채널 | m: 메세지)");
                    String subChoice2 = scanner.nextLine();
                    if (subChoice2.equals("u")) {
                        System.out.print("고유 ID를 입력해주세요");
                        UUID userFindId=UUID.fromString(scanner.nextLine());
                        jcfUserService.deleteUser(userFindId);
                        System.out.println(jcfUserService.findAllUsers());
                    }else if (subChoice2.equals("c")) {
                        System.out.print("채널의 고유 ID를 입력해주세요:");
                        UUID channelFindId=UUID.fromString(scanner.nextLine());
                        jcfChannelService.deleteChannel(channelFindId);
                        System.out.println(jcfChannelService.findAllChannels());
                    }else if (subChoice2.equals("m")) {
                        System.out.print("메세지의 고유 ID를 입력해주세요:");
                        UUID messageFindId=UUID.fromString(scanner.nextLine());
                        jcfMessageService.deleteMessage(messageFindId);
                        System.out.println(jcfMessageService.findAllMessages());
                    }
                    break;
                case 6:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("질못입력하였습니다.");
                    return;
            }
        }*/
        User alice = new User("Alice", "alice@example.code");
        User hyun = new User("Hyun", "hyun@example.code");
        User yull = new User("Yull", "yull@example.code");
        jcfUserService.createUser(alice);
        jcfUserService.createUser(hyun);
        jcfUserService.createUser(yull);
        System.out.println("일부 사용자 조회:" + jcfUserService.findUser(hyun.getId()));
        System.out.println("전체 사용자 조회: " + jcfUserService.findAllUsers());
        System.out.println();
        jcfUserService.updateUserName(alice.getId(),"AliceUpdate");
        jcfUserService.updateUserEmail(alice.getId(),"alice_update@example.com");
        System.out.println("업데이트 후 사용자 조회: " + jcfUserService.findAllUsers());
        jcfUserService.deleteUser(alice.getId());
        System.out.println("삭제 후 사용자 조회: " + jcfUserService.findAllUsers());
        System.out.println();
        /*fileUserService.createUser(alice);
        fileUserService.createUser(hyun);
        fileUserService.createUser(yull);
        System.out.println("일부 사용자 조회:" + fileUserService.findUser(hyun.getId()));
        System.out.println("전체 사용자 조회: " + fileUserService.findAllUsers());
        System.out.println();

        fileUserService.updateUserName(alice.getId(),"AliceUpdate");
        fileUserService.updateUserEmail(alice.getId(),"alice_update@example.com");
        System.out.println("업데이트 후 사용자 조회: " + fileUserService.findAllUsers());
        fileUserService.deleteUser(alice.getId());
        System.out.println("삭제 후 사용자 조회: " + fileUserService.findAllUsers());
        System.out.println();*/
        Channel hiChannel = new Channel("Hi", "Introduce");
        Channel howChannel = new Channel("How", "How about u?");
        Channel byeChannel = new Channel("Bye", "Good Day");
        jcfChannelService.createChannel(hiChannel);
        jcfChannelService.createChannel(byeChannel);
        jcfChannelService.createChannel(howChannel);
        System.out.println("전체 채널 조회: " + jcfChannelService.findAllChannels());
        System.out.println("일부 채널 조회: " + jcfChannelService.findChannel(hiChannel.getId()));
        System.out.println();

        jcfChannelService.updateChannelDescription(hiChannel.getId(),"hiUpdate");
        hiChannel.updateDescription("Update Introduce");
        System.out.println("업데이트 후 채널 조회: " + jcfChannelService.findAllChannels());

        jcfChannelService.deleteChannel(byeChannel.getId());
//        jcfChannelService.deleteChannel(hiChannel.getId());
        System.out.println("삭제 후 채널 조회: " + jcfChannelService.findAllChannels());
        System.out.println();

        Message howMessage = new Message("Good", yull.getId(), howChannel.getId());
//        Message hiMessage = new Message("Hi I'm alice", alice.getId(), hiChannel.getId());
        Message hiMessage = new Message("Hi I'm hyun", hyun.getId(), hiChannel.getId());
        jcfMessageService.messageSave(howMessage);
        jcfMessageService.messageSave(hiMessage);
        System.out.println("전체 메세지 조회: " + jcfMessageService.findAllMessages());
        System.out.println("일부 메세지 조회:" + jcfMessageService.findMessage(howMessage.getId()));
        System.out.println();

        jcfMessageService.updateMessage(howMessage.getId(),"How Update");
        System.out.println("업데이트 후 메세지 조회: " + jcfMessageService.findAllMessages());

        jcfMessageService.deleteMessage(hiMessage.getId());
        System.out.println("삭제 후 메세지 조회: " + jcfMessageService.findAllMessages());
        }catch (Exception e) {
            exceptionHandler(e);
        }finally {
            networkService.disconnect();
        }

        System.out.println();
        System.out.println("프로그램을 정상 종료합니다.");
    }


    private static void exceptionHandler(Exception e) {
        System.out.println("사용자 메세지: 죄송합니다. 알 수 없는 문제가 발생했습니다.");
        System.out.println("==개발자용 디버깅 메세지==");
        e.printStackTrace(System.out);
    }
}
