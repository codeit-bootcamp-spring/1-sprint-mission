import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.PassWordFormatException;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.ArrayList;
import java.util.List;

public class JavaApplication {
    public static void main(String[] args) throws PassWordFormatException {
        JCFUserService userDB = new JCFUserService();
        JCFChannelService channelDB = new JCFChannelService();
        JCFMessageService messageDB = new JCFMessageService();
        userDB.setChannelService(channelDB);
        userDB.setMessageService(messageDB);
        channelDB.setUserService(userDB);
        channelDB.setMessageService(messageDB);
        messageDB.setChannelService(channelDB);



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
        channelDB.updateChannelField(c2.getId(), "owner", F.getId());

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
        System.out.println(channelDB.getChannelList().get(c2.getId()).getChannelMessageList().toString());
        System.out.println(userDB.getUserList().get(D.getId()).toString());
        System.out.println(userDB.getUserList().get(E.getId()).toString());
        System.out.println(userDB.getUserList().get(F.getId()).toString());

        System.out.println("2. Delete user A, A's message will flush, channels that owner of A will disappear, channel's props(messageList of the channel, " +
                "userList of the channel will update. Also, user A.");

        userDB.deleteUserById(A.getId());

        userDB.printing();
        channelDB.printing();
        messageDB.printing();








    }
}
