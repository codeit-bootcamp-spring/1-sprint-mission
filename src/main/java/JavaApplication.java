import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.PassWordFormatException;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 설계의 주안점 : 1. 하나의 단위행동을 할 때, 영향력이 전파되며 일어나는 일을 직접 메소드 호출 하는것이 아닌, 근본적인 메소드 호출 하나로 최대한 해결하려고 시도
 * ex 1) 채널 삭제 -> 채널 삭제, 채널에 속한 메시지 전부 삭제(msgDB), 채널에 참가하던 유저들에 대해, 참가중인 채널리스트에서 해당 채널 삭제(User.Attending),
 *
 * ex 2) 유저 삭제 -> 유저 삭제, 유저가 참여하던 채널들에 대해, 채널의 참가자 리스트에서 해당 유저 삭제(Channel.chUL),
 * 채널의 props인 해당 채널에 작성된 메시지에서, 유저가 쓴 메시지 삭제(ch.chMsgList), 유저가 주인인 채널 삭제
 * 메시지 리스트에서 유저가 쓴 메시지 삭제(msgDB.msgList) -> by 완전탐색
 * (**이렇게 구현 못함(유저가 참여하던 채널들만 순회하며, 유저가 작성한
 * 메시지 삭제(메시지리스트 완전탐색 피하기)) -> 메시지 리스트가 메시지 uuid로 만든 map이니까 이런건 불가)
 *
 *
 * 2. 성능을 고려한 멤버필드 (Channel props -> channelUserList, channelMessageList)...
 *  디스코드 서비스에서 가장 많이 사용하는게 채널입장해서 채팅 -> 채널의 유저, 메시지모음은 빨라야 할 것 같다(완탐 -> Hash)
 * -> 독립적으로 작동하는 느낌. crud 마다 혹시 바뀌어야하는지 고민하면서 머리 아파지는 상황
 *
 *  설계 변화 : 1. 필요해 보이는 독립 멤버필드 마음대로 + 클래스간 연결 될 수 있는(마치 db fk처럼) 멤버필드 난립 -> 구현 난이도 너무 올라감
 *           2. (커밋 히스토리에 순환참조 문제로 존재) 독립 멤버필드 줄이기 + composition -> 순환참조 이슈. 해결법?
 *           3. Singleton [*]
 *
 * 3. 리플랙션을 통한 유지보수성 : updatable props 증가 시 리플랙션이 없다면, dbms에서 # of updateFunc == # of updatable props로 계속 업데이트 필요?
 * 쓰면 엔티티 클래스만 유지보수하면 해결 (사실 별로 안 중요한거 같기도 한 기능)
 */
public class JavaApplication {
    public static void main(String[] args) throws PassWordFormatException {
        JCFUserService userDB = JCFUserService.getInstance();
        JCFChannelService channelDB = JCFChannelService.getInstance();
        JCFMessageService messageDB = JCFMessageService.getInstance();



        //userDB.createUser(new User.Builder().buildEmail("a@b"). ... .build()); -> 하고싶었는데, user A, B, C alias가 필요.
        //없으면 -> sout(userDB.getUserList().~~find userA -> by traversing userList using PKable props(ex email)
        // -> 유저리스트에서 유저 찾아서 출력하는 행동 하나조차 고생, 전통적 pk인 uuid는 user alias가 없어서 또 못씀...
        System.out.println("==========Create User and save them to userDB==========");
        User A = new User.Builder().buildAlias("Alice").buildEmail("Alice@co.kr").buildPassWord("longEnoughPassWord!").buildUpdatedAt().build();
        User B = new User.Builder().buildAlias("Bob").buildEmail("Bob@co.kr").buildPassWord("longPassWordForBob").buildUpdatedAt().build();
        User C =new User.Builder().buildAlias("Carl").buildEmail("Carl@co.kr").buildPassWord("longPassWordForCarl!").buildUpdatedAt().build();
        User D =new User.Builder().buildAlias("Dave").buildEmail("Dave@co.kr").buildPassWord("longPassWordForDave!").buildUpdatedAt().build();
        User E =new User.Builder().buildAlias("Eul").buildEmail("Eul@co.kr").buildPassWord("longPassWordForEul!").buildUpdatedAt().build();
        User F =new User.Builder().buildAlias("Fred").buildEmail("Fred@co.kr").buildPassWord("longPassWordForFred!").buildUpdatedAt().build();
        User G =new User.Builder().buildAlias("Graham").buildEmail("Graham@co.kr").buildPassWord("longPassWordForGraham!").buildUpdatedAt().build();
        User H =new User.Builder().buildAlias("Henry").buildEmail("Henry@co.kr").buildPassWord("longPassWordForHenry!").buildUpdatedAt().build();
        User I =new User.Builder().buildAlias("Ian").buildEmail("Ian@co.kr").buildPassWord("longPassWordForIan!").buildUpdatedAt().build();
        userDB.createUser(A);
        userDB.createUser(B);
        userDB.createUser(C);
        userDB.createUser(D);
        userDB.createUser(E);
        userDB.createUser(F);
        userDB.createUser(G);
        userDB.createUser(H);
        userDB.createUser(I);

        System.out.println("==========User Information==========");
        System.out.println(userDB.getUserList().get(A.getId()).toString());
        System.out.println(userDB.getUserList().get(B.getId()).toString());
        System.out.println(userDB.getUserList().get(C.getId()).toString());
        System.out.println(userDB.getUserList().get(D.getId()).toString());
        System.out.println(userDB.getUserList().get(E.getId()).toString());
        System.out.println(userDB.getUserList().get(F.getId()).toString());
        System.out.println(userDB.getUserList().get(G.getId()).toString());
        System.out.println(userDB.getUserList().get(H.getId()).toString());
        System.out.println(userDB.getUserList().get(I.getId()).toString());

        System.out.println("==========Update User Information==========");
        userDB.updateUserField(userDB.getUserList().get(A.getId()).getId(), "alias", "Alice_mod");
        userDB.updateUserField(B.getId(), "email", "Bob_mod");
        userDB.updateUserField(C.getId(), "passWord", "Carl_modified_passWord");

        System.out.println("==========Modified User Information==========");
        System.out.println(userDB.getUserList().get(A.getId()).toString());
        System.out.println(userDB.getUserList().get(B.getId()).toString());
        System.out.println(userDB.getUserList().get(C.getId()).toString());

        System.out.println("==========Create Channel and allocate Users into channel==========");
        /**
         * A, B, C user -> ch_1
         * D, E, F user -> ch_2
         * G, H, I user -> ch_3
         */
        List<User> channelUserList_1 = new ArrayList<>();
        List<User> channelUserList_2 = new ArrayList<>();
        List<User> channelUserList_3 = new ArrayList<>();
        List<Message> channelMessageList_1 = new ArrayList<>();
        List<Message> channelMessageList_2 = new ArrayList<>();
        List<Message> channelMessageList_3 = new ArrayList<>();
        Channel c1 = new Channel.Builder().buildOwner(A).buildUpdatedAt().buildChannelName("channel_1").buildChannelUserList(channelUserList_1).buildChannelMessageList(channelMessageList_1).build();
        Channel c2 = new Channel.Builder().buildOwner(D).buildUpdatedAt().buildChannelName("channel_2").buildChannelUserList(channelUserList_2).buildChannelMessageList(channelMessageList_2).build();
        Channel c3 = new Channel.Builder().buildOwner(G).buildUpdatedAt().buildChannelName("channel_3").buildChannelUserList(channelUserList_3).buildChannelMessageList(channelMessageList_3).build();
        channelDB.createChannel(c1);
        channelDB.createChannel(c2);
        channelDB.createChannel(c3);
        channelDB.participateChannel(B, c1.getId());
        channelDB.participateChannel(C, c1.getId());
        channelDB.participateChannel(E, c2.getId());
        channelDB.participateChannel(F, c2.getId());
        channelDB.participateChannel(H, c3.getId());
        channelDB.participateChannel(I, c3.getId());

        System.out.println("==========Channel Information==========");
        System.out.println(channelDB.getChannelList().get(c1.getId()).toString());
        System.out.println(channelDB.getChannelList().get(c2.getId()).toString());
        System.out.println(channelDB.getChannelList().get(c3.getId()).toString());

        System.out.println("==========Update Channel Information==========");
        channelDB.updateChannelField(channelDB.getChannelList().get(c1.getId()).getId(), "channelName", "channel_1_mod");
        channelDB.updateChannelField(c2.getId(), "owner", F);

        System.out.println("==========Modified Channel Information==========");
        System.out.println(channelDB.getChannelList().get(c1.getId()).toString());
        System.out.println(channelDB.getChannelList().get(c2.getId()).toString());
        System.out.println(channelDB.getChannelList().get(c3.getId()).toString());

        System.out.println("==========Create Message and save them to messageDB==========");
        Message m1 = new Message.Builder(A.getId(), c1.getId()).buildUpdatedAt().buildContents(A.getAlias() + " sent message in " + c1.getChannelName()).build();
        Message m2 = new Message.Builder(B.getId(), c1.getId()).buildUpdatedAt().buildContents(B.getAlias() + " sent message in " + c1.getChannelName()).build();
        Message m3 = new Message.Builder(C.getId(), c1.getId()).buildUpdatedAt().buildContents(C.getAlias() + " sent message in " + c1.getChannelName()).build();
        Message m4 = new Message.Builder(D.getId(), c2.getId()).buildUpdatedAt().buildContents(D.getAlias() + " sent message in " + c2.getChannelName()).build();
        Message m5 = new Message.Builder(E.getId(), c2.getId()).buildUpdatedAt().buildContents(E.getAlias() + " sent message in " + c2.getChannelName()).build();
        Message m6 = new Message.Builder(F.getId(), c2.getId()).buildUpdatedAt().buildContents(F.getAlias() + " sent message in " + c2.getChannelName()).build();
        Message m7 = new Message.Builder(G.getId(), c3.getId()).buildUpdatedAt().buildContents(G.getAlias() + " sent message in " + c3.getChannelName()).build();
        Message m8 = new Message.Builder(H.getId(), c3.getId()).buildUpdatedAt().buildContents(H.getAlias() + " sent message in " + c3.getChannelName()).build();
        Message m9 = new Message.Builder(I.getId(), c3.getId()).buildUpdatedAt().buildContents(I.getAlias() + " sent message in " + c3.getChannelName()).build();
        messageDB.createMessage(m1);
        messageDB.createMessage(m2);
        messageDB.createMessage(m3);
        messageDB.createMessage(m4);
        messageDB.createMessage(m5);
        messageDB.createMessage(m6);
        messageDB.createMessage(m7);
        messageDB.createMessage(m8);
        messageDB.createMessage(m9);

        System.out.println("==========Message Information==========");
        System.out.println(channelDB.getChannelList().get(c1.getId()).getChannelMessageList().toString());
        System.out.println(channelDB.getChannelList().get(c2.getId()).getChannelMessageList().toString());
        System.out.println(channelDB.getChannelList().get(c3.getId()).getChannelMessageList().toString());

        System.out.println("==========Update Channel Information==========");
        messageDB.updateMessageById(m1.getId(), "message modified. channel : " + m1.getSource() + "writer : " + m1.getWriter());

        System.out.println("==========Modified Message Information==========");
        System.out.println(channelDB.getChannelList().get(c1.getId()).getChannelMessageList().toString());


        System.out.println("==========Delete Test==========");
        System.out.println("1. Delete m1 message, channel 1's messageList will update automatically");
        messageDB.deleteMessage(m1.getId());
        System.out.println(channelDB.getChannelList().get(c1.getId()).toString());

        System.out.println("2. Delete c2, message of channel 2 will flush, user channel attending list related to c2 will update automatically");
        channelDB.deleteChannel(c2.getId());
       // System.out.println(channelDB.getChannelList().get(c2.getId()).getChannelMessageList().toString());
        System.out.println(userDB.getUserList().get(D.getId()).toString());
        System.out.println(userDB.getUserList().get(E.getId()).toString());
        System.out.println(userDB.getUserList().get(F.getId()).toString());

        System.out.println("3. Delete user I, I's message will flush, channels that owner of I will disappear, channel's props(messageList of the channel, " +
                "userList of the channel will update. Also, user I.");

        userDB.deleteUserById(I.getId());
        //I는 c3에 소속되어있었고, m9을 작성. c3 채널에서 참가자 목록에서 I삭제, 메시지목록에서 A가 writer인 메시지 삭제(독립 멤버필드 삭제 검증 필요)
        //I삭제, msgDB에서 I가쓴 메시지 삭제
        userDB.deleteUserById(G.getId()); // 고려해야 하는 상황 : Graham은 자기 소유의 채널도 있다. null 오류같은걸로 꼬였으면 메시지 삭제, 채널삭제(nested action) 겹치면서
        //꼬일 수 있는 위험도 있는 유저. -> 통과

        userDB.printing();
        channelDB.printing();
        messageDB.printing();
        System.out.println("==========Independent Field==========");
        System.out.println(c3.getChannelMessageList());
        System.out.println(c3.getChannelUserList());








    }
}
