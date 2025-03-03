//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.PasswordFormatException;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.MessageService;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.basic.BasicChannelService;
//import com.sprint.mission.discodeit.service.basic.BasicMessageService;
//import com.sprint.mission.discodeit.service.basic.BasicUserService;
//
///**
// * 설계의 주안점 : DB-Service를 2 tier 구조로 가져가면, 매 crud마다 계속 file system까지 가서 데이터를 조회해야 한다.
// * IO Bound문제의 해결을 위해, sprint mission 1에 사용했던 JCF를, 캐시의 역할처럼 이용해서, IO시간을 줄인다.
// * naive(현재) : 프로그램이 시작하면, 일단 파일 시스템에 있는 데이터를 전부 다 JCF에 불러온다.
// * 일련의 crud작업을 Service-JCF캐시를 통해 진행한다.
// * 작업이 끝난 뒤, JCF의 값을 파일시스템에 덮어씌운다.
// * 모든 구현체의 앞단에 인터페이스를 배치하고 lvalue를 인터페이스로 받은 뒤 다형성을 이용한 메소드를 실행시켜 유지보수성을 향상시키고,
// * 서비스 로직의 독립성을 보장한다
// * File*Repository(File System. work as DB) <-> *Repository(impl) <-> *Service(impl) <-> Basic*Service(Service with cache(JCF))
// *
// * 리팩토링이 필요한 부분
// * 크기를 고려하지 않은 상황이기 때문에, DB의 모든내용을 JCF에 한번에 다 담으려 시도하다가 Out Of Memory가 발생할 여지가 있다.
// * => 개선을 위해서 실재 캐시가 동작하듯 약속된 크기만큼씩만 데이터를 가져오고, cache miss가 나면 DB까지 가서 캐시를 교체하는 방식으로 리팩토링 필요
// *
// * 성능을 고려한 멤버필드 (Channel props -> channelUserList, channelMessageList)...-> 삭제 필요
// *  디스코드 서비스에서 가장 많이 사용하는게 채널입장해서 채팅 -> 채널의 유저, 메시지모음은 빨라야 할 것 같다(완탐 -> Hash)
// * -> 독립적으로 작동하는 느낌. crud 마다 혹시 바뀌어야하는지 고민하면서 머리 아파지는 상황
// *
// * => JPA로 독립적인 로직 코드구현(정합성 문제)은 해결 가능하지만, 이렇게 '*대다' 구조를 사용하면, 메모리에 올리는 과정에서 OOM이 일어날 위험이 있다.
// * -> JPA가 있다고 해도, *대다를 위한 멤버필드의 사용은 피하자.
// */
//public class JavaApplication {
//    public static void main(String[] args) throws PasswordFormatException {
//        UserService userDB = BasicUserService.getInstance();
//        ChannelService channelDB = BasicChannelService.getInstance();
//        MessageService messageDB = BasicMessageService.getInstance();
//
//        UserRepository userFile = FileUserRepository.getInstance();
//        ChannelRepository channelFile = FileChannelRepository.getInstance();
//        MessageRepository messageFile = FileMessageRepository.getInstance();
//
//        userDB.setUserList(userFile.load());
//        channelDB.setChannelList(channelFile.load());
//        messageDB.setMessageList(messageFile.load());
//        // 프로그램 실행 직후, 파일(db)의 내용을 JCF(캐시)로 전부 가져온다.
//        //이후 crud 과정은, sprint1과 동일
//
//        System.out.println("==========printing DB contents==========");
//        ((BasicUserService) userDB).printing();
//        ((BasicChannelService) channelDB).printing();
//        ((BasicMessageService) messageDB).printing();
//
//        //userDB.createUser(new User.Builder().buildEmail("a@b"). ... .build()); -> 하고싶었는데, user A, B, C alias가 필요.
//        //없으면 -> sout(userDB.getUserList().~~find userA -> by traversing userList using PKable props(ex email)
//        // -> 유저리스트에서 유저 찾아서 출력하는 행동 하나조차 고생, 전통적 pk인 uuid는 user alias가 없어서 또 못씀...
//        System.out.println("==========Create User and save them to userDB==========");
//        User A = new User.Builder().buildAlias("Alice").buildEmail("Alice@co.kr").buildPassword("longEnoughPassWord!").buildUpdatedAt().build();
//        User B = new User.Builder().buildAlias("Bob").buildEmail("Bob@co.kr").buildPassword("longPassWordForBob").buildUpdatedAt().build();
//        User C =new User.Builder().buildAlias("Carl").buildEmail("Carl@co.kr").buildPassword("longPassWordForCarl!").buildUpdatedAt().build();
//        User D =new User.Builder().buildAlias("Dave").buildEmail("Dave@co.kr").buildPassword("longPassWordForDave!").buildUpdatedAt().build();
//        User E =new User.Builder().buildAlias("Eul").buildEmail("Eul@co.kr").buildPassword("longPassWordForEul!").buildUpdatedAt().build();
//        User F =new User.Builder().buildAlias("Fred").buildEmail("Fred@co.kr").buildPassword("longPassWordForFred!").buildUpdatedAt().build();
//        User G =new User.Builder().buildAlias("Graham").buildEmail("Graham@co.kr").buildPassword("longPassWordForGraham!").buildUpdatedAt().build();
//        User H =new User.Builder().buildAlias("Henry").buildEmail("Henry@co.kr").buildPassword("longPassWordForHenry!").buildUpdatedAt().build();
//        User I =new User.Builder().buildAlias("Ian").buildEmail("Ian@co.kr").buildPassword("longPassWordForIan!").buildUpdatedAt().build();
//        userDB.createUser(A);
//        userDB.createUser(B);
//        userDB.createUser(C);
//        userDB.createUser(D);
//        userDB.createUser(E);
//        userDB.createUser(F);
//        userDB.createUser(G);
//        userDB.createUser(H);
//        userDB.createUser(I);
//
//        System.out.println("==========User Information==========");
//        System.out.println(userDB.getUserList().get(A.getId()).toString());
//        System.out.println(userDB.getUserList().get(B.getId()).toString());
//        System.out.println(userDB.getUserList().get(C.getId()).toString());
//        System.out.println(userDB.getUserList().get(D.getId()).toString());
//        System.out.println(userDB.getUserList().get(E.getId()).toString());
//        System.out.println(userDB.getUserList().get(F.getId()).toString());
//        System.out.println(userDB.getUserList().get(G.getId()).toString());
//        System.out.println(userDB.getUserList().get(H.getId()).toString());
//        System.out.println(userDB.getUserList().get(I.getId()).toString());
//
//        System.out.println("==========Update User Information==========");
//        userDB.updateUserField(userDB.getUserList().get(A.getId()).getId(), "aliaaases", "Alice_mod");
//        userDB.updateUserField(B.getId(), "EmAils", "Bob_mod");
//        userDB.updateUserField(C.getId(), "paaassWord", "Carl_modified_password");
//        //셋 다 리플렉션 fieldName을 이상하게 만든 상황. 런타임 에러 의한 프로그램 종료나, exception throw 없이 멤버필드 업데이트만
//        //안된 채로 프로그램 정상종료.
//
//        System.out.println("==========Modified User Information==========");
//        System.out.println(userDB.getUserList().get(A.getId()).toString());
//        System.out.println(userDB.getUserList().get(B.getId()).toString());
//        System.out.println(userDB.getUserList().get(C.getId()).toString());
//
//        System.out.println("==========Create Channel and allocate Users into channel==========");
//        /**
//         * A, B, C user -> ch_1
//         * D, E, F user -> ch_2
//         * G, H, I user -> ch_3
//         */
////        List<User> channelUserList_1 = new ArrayList<>();
////        List<User> channelUserList_2 = new ArrayList<>();
////        List<User> channelUserList_3 = new ArrayList<>();
////        List<Message> channelMessageList_1 = new ArrayList<>();
////        List<Message> channelMessageList_2 = new ArrayList<>();
////        List<Message> channelMessageList_3 = new ArrayList<>();
//        Channel c1 = new Channel.Builder().buildOwner(A).buildUpdatedAt().buildChannelName("channel_1")/*.buildChannelUserList(channelUserList_1).buildChannelMessageList(channelMessageList_1)*/.build();
//        Channel c2 = new Channel.Builder().buildOwner(D).buildUpdatedAt().buildChannelName("channel_2")/*.buildChannelUserList(channelUserList_2).buildChannelMessageList(channelMessageList_2)*/.build();
//        Channel c3 = new Channel.Builder().buildOwner(G).buildUpdatedAt().buildChannelName("channel_3")/*.buildChannelUserList(channelUserList_3).buildChannelMessageList(channelMessageList_3)*/.build();
//        channelDB.createChannel(c1);
//        channelDB.createChannel(c2);
//        channelDB.createChannel(c3);
//        ((BasicChannelService) channelDB).participateChannel(B, c1.getId());
//        ((BasicChannelService) channelDB).participateChannel(C, c1.getId());
//        ((BasicChannelService) channelDB).participateChannel(E, c2.getId());
//        ((BasicChannelService) channelDB).participateChannel(F, c2.getId());
//        ((BasicChannelService) channelDB).participateChannel(H, c3.getId());
//        ((BasicChannelService) channelDB).participateChannel(I, c3.getId());
//
//        System.out.println("==========Channel Information==========");
//        System.out.println(((BasicChannelService) channelDB).getChannelList().get(c1.getId()).toString());
//        System.out.println(((BasicChannelService) channelDB).getChannelList().get(c2.getId()).toString());
//        System.out.println(((BasicChannelService) channelDB).getChannelList().get(c3.getId()).toString());
//
//        System.out.println("==========Update Channel Information==========");
//        channelDB.updateChannelField(((BasicChannelService) channelDB).getChannelList().get(c1.getId()).getId(), "channelName", "channel_1_mod");
//
//        channelDB.updateChannelField(c2.getId(), "owner", F);
//
//        System.out.println("==========Modified Channel Information==========");
//        System.out.println(channelDB.getChannelList().get(c1.getId()).toString());
//        System.out.println(channelDB.getChannelList().get(c2.getId()).toString());
//        System.out.println(channelDB.getChannelList().get(c3.getId()).toString());
//
//        System.out.println("==========Create Message and save them to messageDB==========");
//        Message m1 = new Message.Builder(A.getId(), c1.getId()).buildUpdatedAt().buildContents(A.getAlias() + " sent message in " + c1.getChannelName()).build();
//        Message m2 = new Message.Builder(B.getId(), c1.getId()).buildUpdatedAt().buildContents(B.getAlias() + " sent message in " + c1.getChannelName()).build();
//        Message m3 = new Message.Builder(C.getId(), c1.getId()).buildUpdatedAt().buildContents(C.getAlias() + " sent message in " + c1.getChannelName()).build();
//        Message m4 = new Message.Builder(D.getId(), c2.getId()).buildUpdatedAt().buildContents(D.getAlias() + " sent message in " + c2.getChannelName()).build();
//        Message m5 = new Message.Builder(E.getId(), c2.getId()).buildUpdatedAt().buildContents(E.getAlias() + " sent message in " + c2.getChannelName()).build();
//        Message m6 = new Message.Builder(F.getId(), c2.getId()).buildUpdatedAt().buildContents(F.getAlias() + " sent message in " + c2.getChannelName()).build();
//        Message m7 = new Message.Builder(G.getId(), c3.getId()).buildUpdatedAt().buildContents(G.getAlias() + " sent message in " + c3.getChannelName()).build();
//        Message m8 = new Message.Builder(H.getId(), c3.getId()).buildUpdatedAt().buildContents(H.getAlias() + " sent message in " + c3.getChannelName()).build();
//        Message m9 = new Message.Builder(I.getId(), c3.getId()).buildUpdatedAt().buildContents(I.getAlias() + " sent message in " + c3.getChannelName()).build();
//        messageDB.createMessage(m1);
//        messageDB.createMessage(m2);
//        messageDB.createMessage(m3);
//        messageDB.createMessage(m4);
//        messageDB.createMessage(m5);
//        messageDB.createMessage(m6);
//        messageDB.createMessage(m7);
//        messageDB.createMessage(m8);
//        messageDB.createMessage(m9);
//
//        System.out.println("==========Message Information==========");
//        System.out.println(channelDB.getChannelList().get(c1.getId()).getChannelMessageList().toString());
//        System.out.println(channelDB.getChannelList().get(c2.getId()).getChannelMessageList().toString());
//        System.out.println(channelDB.getChannelList().get(c3.getId()).getChannelMessageList().toString());
//
//        System.out.println("==========Update Channel Information==========");
//        messageDB.updateMessageById(m1.getId(), "message modified. channel : " + m1.getSource() + "writer : " + m1.getWriter());
//
//        System.out.println("==========Modified Message Information==========");
//        System.out.println(channelDB.getChannelList().get(c1.getId()).getChannelMessageList().toString());
//
//
//        System.out.println("==========Delete Test==========");
//        System.out.println("1. Delete m1 message, channel 1's messageList will update automatically");
//        messageDB.deleteMessage(m1.getId());
//        System.out.println(channelDB.getChannelList().get(c1.getId()).toString());
//
//        System.out.println("2. Delete c2, message of channel 2 will flush, user channel attending list related to c2 will update automatically");
//        channelDB.deleteChannel(c2.getId());
//
//        System.out.println(userDB.getUserList().get(D.getId()).toString());
//        System.out.println(userDB.getUserList().get(E.getId()).toString());
//        System.out.println(userDB.getUserList().get(F.getId()).toString());
//
//        System.out.println("3. Delete user I, I's message will flush, channels that owner of I will disappear, channel's props(messageList of the channel, " +
//                "userList of the channel will update. Also, user I.");
//
//        userDB.deleteUserById(I.getId());
//        //I는 c3에 소속되어있었고, m9을 작성. c3 채널에서 참가자 목록에서 I삭제, 메시지목록에서 A가 writer인 메시지 삭제(독립 멤버필드 삭제 검증 필요)
//        //I삭제, msgDB에서 I가쓴 메시지 삭제
//        userDB.deleteUserById(G.getId()); // 고려해야 하는 상황 : Graham은 자기 소유의 채널도 있다. null 오류같은걸로 꼬였으면 메시지 삭제, 채널삭제(nested action) 겹치면서
//        //꼬일 수 있는 위험도 있는 유저. -> 통과
//
//        ((BasicUserService) userDB).printing();
//        ((BasicChannelService) channelDB).printing();
//        ((BasicMessageService) messageDB).printing();
//
//        System.out.println("==========Independent Field==========");
//        System.out.println(c3.getChannelMessageList());
//        System.out.println(c3.getChannelUserList());
//
//
//        userFile.save(userDB.getUserList());
//        channelFile.save(channelDB.getChannelList());
//        messageFile.save(messageDB.getMessageList());
//
//
//
//
//
//
//
//
//
//    }
//}
