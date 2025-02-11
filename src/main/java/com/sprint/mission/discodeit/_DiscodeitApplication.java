//package com.sprint.mission.discodeit;
//
//import com.sprint.mission.discodeit.entity.*;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.*;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//
//import java.util.*;
//
//
//public class _DiscodeitApplication {
//    static List<User> setupUser(UserService userService) {
//        List<User> users = List.of(
//                new User("JohnDoe", "john.doe@example.com", "010-1234-5678", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "Gaming"))),
//                new User("JaneDoe", "jane.doe@example.com", "010-5678-1234", "Busan, Korea", 28, "Traveling", new ArrayList<>(List.of("Photography", "Hiking"))),
//                new User("Alice", "alice.wonderland@example.com", "010-8765-4321", "Incheon, Korea", 25, "Painting", new ArrayList<>(List.of("Art", "Design"))),
//                new User("Bob", "bob.builder@example.com", "010-4321-8765", "Daegu, Korea", 35, "Building", new ArrayList<>(List.of("Construction", "DIY Projects"))),
//                new User("Charlie", "charlie.choco@example.com", "010-5678-9876", "Jeju, Korea", 27, "Cooking", new ArrayList<>(List.of("Food", "Baking")))
//        );
//
//        for (User user : users) {
//            userService.create(user);
//        }
//        return users;
//    }
//
//    private static final String DATA_NULL_BLANK = "";
//    public static void main(String[] args) {
//        UserRepository jcfUserRepo = new JCFUserRepository();
//        UserRepository fileUserRepo = new FileUserRepository();
//
//        System.out.println("====== 유저 jcf repo =====");
//        UserService userService = new BasicUserService(jcfUserRepo);
//
//        System.out.println("================");
//        System.out.println("JCF 유저 셋팅");
//        List<User> setUserList = setupUser(userService);
//
//        System.out.println("================");
//        System.out.println("JCF 유저 모두 읽기");
//        List<User> jcfUserList = userService.readAll();
//        jcfUserList.forEach(user -> {
//            System.out.println("userName : " + user.getUsername()
//                    + " | Email : " + user.getEmail()
//                    + " | phoneNumber : " + user.getPhoneNumber()
//                    + " | Address : " + user.getAddr()
//                    + " | Age : " + user.getAge()
//                    + " | Hobby : " + user.getHobby()
//                    + " | Interest : " + user.getInterest()
//            );
//        });
//
//        System.out.println("================");
//        System.out.println("JCF User 'Bob' search");
//        User jcfUserOne = jcfUserList.stream()
//                                .filter(user -> user.getUsername().equals("Bob"))
//                                .findFirst()
//                                .orElse(null);
//
//        System.out.println("userName : " + jcfUserOne.getUsername()
//                + " | Email : " + jcfUserOne.getEmail()
//                + " | phoneNumber : " + jcfUserOne.getPhoneNumber()
//                + " | Address : " + jcfUserOne.getAddr()
//                + " | Age : " + jcfUserOne.getAge()
//                + " | Hobby : " + jcfUserOne.getHobby()
//                + " | Interest : " + jcfUserOne.getInterest()
//        );
//
//        System.out.println("================");
//        System.out.println("JCF User 'Bob' update");
//        userService.update(jcfUserOne.getId(), new User("Bob", "P@ssw0rdB0b", "updated_user1@example.com", "010-2349-9548", "Seoul, Korea", 35, "Reading2", new ArrayList<>(List.of("Construction","Technology", "Gaming"))));
//        jcfUserOne = userService.readOne(jcfUserOne.getId());
//
//        System.out.println("userName : " + jcfUserOne.getUsername()
//                + " | Email : " + jcfUserOne.getEmail()
//                + " | phoneNumber : " + jcfUserOne.getPhoneNumber()
//                + " | Address : " + jcfUserOne.getAddr()
//                + " | Age : " + jcfUserOne.getAge()
//                + " | Hobby : " + jcfUserOne.getHobby()
//                + " | Interest : " + jcfUserOne.getInterest()
//        );
//
//        System.out.println();
//        System.out.println("====== 유저 file Repo 교체 =====");
//        userService = new BasicUserService(fileUserRepo);
//
//        System.out.println("================");
//        System.out.println("File 유저 셋팅");
//        for(User user : setUserList){
//            userService.create(user);
//        }
////        setupUser(userService);
//
//        List<User> fileUserList = userService.readAll();
//        fileUserList.forEach(user -> {
//            System.out.println("userName : " + user.getUsername()
//                    + " | Email : " + user.getEmail()
//                    + " | phoneNumber : " + user.getPhoneNumber()
//                    + " | Address : " + user.getAddr()
//                    + " | Age : " + user.getAge()
//                    + " | Hobby : " + user.getHobby()
//                    + " | Interest : " + user.getInterest()
//            );
//        });
//
//        System.out.println("================");
//        System.out.println("File User 'Bob' search");
//        User fileUserOne = fileUserList.stream()
//                .filter(user -> user.getUsername().equals("Bob"))
//                .findFirst()
//                .orElse(null);
//
//        System.out.println("userName : " + fileUserOne.getUsername()
//                + " | Email : " + fileUserOne.getEmail()
//                + " | phoneNumber : " + fileUserOne.getPhoneNumber()
//                + " | Address : " + fileUserOne.getAddr()
//                + " | Age : " + fileUserOne.getAge()
//                + " | Hobby : " + fileUserOne.getHobby()
//                + " | Interest : " + fileUserOne.getInterest()
//        );
//
//
//        System.out.println("================");
//        System.out.println("File User 'Bob' update");
//        userService.update(fileUserOne.getId(), new User("Bob", "updated_user1@example.com", "010-2349-9548", "Seoul, Korea", 35, "Reading2", new ArrayList<>(List.of("Construction","Technology", "Gaming"))));
//        fileUserOne = userService.readOne(fileUserOne.getId());
//
//        System.out.println("userName : " + fileUserOne.getUsername()
//                + " | Email : " + fileUserOne.getEmail()
//                + " | phoneNumber : " + fileUserOne.getPhoneNumber()
//                + " | Address : " + fileUserOne.getAddr()
//                + " | Age : " + fileUserOne.getAge()
//                + " | Hobby : " + fileUserOne.getHobby()
//                + " | Interest : " + fileUserOne.getInterest()
//        );
//
//        System.out.println("================");
//        System.out.println("File User 'Bob' delete");
//        userService.delete(fileUserOne.getId());
//        fileUserList = userService.readAll();
//        fileUserList.forEach(user -> {
//            System.out.println("userName : " + user.getUsername()
//                    + " | Email : " + user.getEmail()
//                    + " | phoneNumber : " + user.getPhoneNumber()
//                    + " | Address : " + user.getAddr()
//                    + " | Age : " + user.getAge()
//                    + " | Hobby : " + user.getHobby()
//                    + " | Interest : " + user.getInterest()
//            );
//        });
//
//
//
//        System.out.println();
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Channel>>>>>>>>>>>>>>>>>>>>>>>");
//        List<Channel> channels = List.of(
//                new Channel("SB_1_Sprint", "JAVA Spring BackEnd Developer discode",
//                        new ArrayList<>(List.of(jcfUserList.get(0), jcfUserList.get(1))), jcfUserList.get(1)),
//                new Channel("JavaScript_TechTalk", "All about JavaScript and its frameworks",
//                        new ArrayList<>(List.of(jcfUserList.get(1), jcfUserList.get(2))), jcfUserList.get(2)),
//                new Channel("React_FrontEnd", "Front-end technologies focusing on React",
//                        new ArrayList<>(List.of(jcfUserList.get(3), jcfUserList.get(4))), jcfUserList.get(4)),
//                new Channel("NodeJS_DevGroup", "Node.js development and deployment discussions",
//                        new ArrayList<>(List.of(jcfUserList.get(0), jcfUserList.get(2))), jcfUserList.get(0)),
//                new Channel("SpringBoot_Dev", "Discussions on SpringBoot applications and microservices",
//                        new ArrayList<>(List.of(jcfUserList.get(1), jcfUserList.get(4))), jcfUserList.get(1))
//        );
//        System.out.println();
//        System.out.println("================");
//        System.out.println("JCF 채널 셋팅");
//
//        ChannelRepository jcfChannelRepo = new JCFChannelRepository();
//        ChannelService channelService = new BasicChannelService(jcfChannelRepo);
//
//        for(Channel channel : channels){
//            channelService.create(channel);
//        }
//        System.out.println("채널 등록 완료");
//        System.out.println("================");
//
//        System.out.println("================");
//        System.out.println("JCF 전체 채널 조회 :");
//        List<Channel> jcfChannelList = channelService.readAll();
//        jcfChannelList.forEach(channel -> {
//            System.out.println("name : " + channel.getName()
//                    + " | Owner : " + channel.getOwner().getUsername()
//                    + " | description : " + channel.getDescription()
//            );
//        });
//
//
//        System.out.println("JCF User 'JavaScript_TechTalk' search");
//        Channel jcfChannelOne = jcfChannelList.stream()
//                .filter(channel -> channel.getName().equals("JavaScript_TechTalk"))
//                .findFirst()
//                .orElse(null);
//
//        System.out.println(jcfChannelOne.getName());
//
////        Channel channel1 = channels.get(1);
////
////        User addUser = new User("martin", "martin@example.com", "010-5678-5678", "Seoul, Korea", 30, "Reading", new ArrayList<>(List.of("Technology", "computer")));
////        userService.create(addUser);
////        System.out.println("================");
////
////        System.out.println("채널에 새로운 맴버 추가하기 : ");
////        channelService.channelMemberJoin(channel1.getId(), addUser);
////        System.out.println("================");
////
////        System.out.println("채널 주인장 변경하기 :");
////        channelService.channelOwnerChange(channel1.getId(), users.get(1));
////        System.out.println("================");
//
//
//        System.out.println();
//        System.out.println("================");
//        System.out.println("File 채널 셋팅");
//
//        ChannelRepository fileChannelRepo = new FileChannelRepository();
//        channelService = new BasicChannelService(fileChannelRepo);
//
//        for(Channel channel : channels){
//            channelService.create(channel);
//        }
//        System.out.println("채널 등록 완료");
//        System.out.println("================");
//
//        System.out.println("================");
//        System.out.println("File 전체 채널 조회 :");
//        List<Channel> fileChannelList = channelService.readAll();
//        fileChannelList.forEach(channel -> {
//            System.out.println("name : " + channel.getName()
//                    + " | Owner : " + channel.getOwner().getUsername()
//                    + " | description : " + channel.getDescription()
//            );
//        });
//
//        MessageRepository jcfMessageRepo = new JCFMessageRepository();
//        MessageService messageService = new BasicMessageService(jcfMessageRepo);
//        System.out.println();
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>Message>>>>>>>>>>>>>>>>>>>>>>>");
//
//        List<Message> messages = List.of(
//            new Message("Hello", jcfUserList.get(1), jcfUserList.get(4)),
//            new Message("처음 뵙겠습니다~", jcfUserList.get(1), channels.get(1)),
//            new Message("안녕하세요!", jcfUserList.get(2), channels.get(0)),
//            new Message("채널 가입 요청 드립니다.", jcfUserList.get(3), channels.get(2)),
//            new Message("반가워요!", jcfUserList.get(1), jcfUserList.get(3)),
//            new Message("공지사항 확인 부탁드립니다.", jcfUserList.get(0), channels.get(3)),
//            new Message("다음 모임은 언제인가요?", jcfUserList.get(4), channels.get(1)),
//            new Message("메일 보내드렸습니다.", jcfUserList.get(2), jcfUserList.get(0)),
//            new Message("새 프로젝트에 대해서 논의해보아요.", jcfUserList.get(3), channels.get(4)),
//            new Message("다른 문의사항 있으면 알려주세요.", jcfUserList.get(2), channels.get(0))
//        );
//
//        messages.forEach(message -> {
//            messageService.create(message);
//        });
//
//        System.out.println("메시지 전송 완료");
//        System.out.println("================");
//
//        System.out.println("전체 메시지 조회 : ");
//        List<Message> messageList = messageService.readAll();
//        messageList.forEach(message -> {
//            String sender = message.getSender().getUsername();
//            String recipient = message.getRecipient()  != null ? message.getRecipient().getUsername() : DATA_NULL_BLANK ;
//
//            if(recipient != DATA_NULL_BLANK){
//                System.out.println(sender + "님이 " + recipient + "님에게 글을 남기셨습니다.");
//                System.out.println(sender + " -> " + recipient + " : "  + message.getContent());
//            }else{
//                System.out.println(sender + "님이 " + message.getChannel().getName() + " 채널에 글을 남기셨습니다.");
//                System.out.println(sender + " - " + message.getContent());
//            }
//        });
//        System.out.println("================");
//
////        System.out.println(channels.get(1).getName() + " 채널 전체 메시지 조회 : ");
////        List<Message> cMList = messageService.readAll(channels.get(1).getId());
////        cMList.forEach(message -> {
////            String sender = message.getSender().getUsername();
////            String recipient = message.getRecipient() != null ? message.getRecipient().getUsername() : DATA_NULL_BLANK;
////
////            if (recipient != DATA_NULL_BLANK) {
////                System.out.println(sender + " -> " + recipient + " : " + message.getContent());
////            } else {
////                System.out.println(sender + " - " + message.getContent());
////            }
////        });
//
//        System.out.println();
//        System.out.println("================");
//        System.out.println("File 채널 셋팅");
//
//        MessageRepository fileMessageRepo = new FileMessageRepository();
//        MessageService fMessageService = new BasicMessageService(fileMessageRepo);
//
//        for(Message message : messages){
//            fMessageService.create(message);
//        }
//        System.out.println("메시지 등록 완료");
//        System.out.println("================");
//
//        System.out.println("전체 메시지 조회 : ");
//        messageList = fMessageService.readAll();
//        messageList.forEach(message -> {
//            String sender = message.getSender().getUsername();
//            String recipient = message.getRecipient()  != null ? message.getRecipient().getUsername() : DATA_NULL_BLANK ;
//
//            if(recipient != DATA_NULL_BLANK){
//                System.out.println(sender + "님이 " + recipient + "님에게 글을 남기셨습니다.");
//                System.out.println(sender + " -> " + recipient + " : "  + message.getContent());
//            }else{
//                System.out.println(sender + "님이 " + message.getChannel().getName() + " 채널에 글을 남기셨습니다.");
//                System.out.println(sender + " - " + message.getContent());
//            }
//        });
//        System.out.println("================");
//
//
//    }
//}
