import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import static org.mockito.Mockito.mock;

public class JavaApplication {
    public static void main(String[] args) {
        JCFUserService userService = new JCFUserService(mock(InputHandler.class));
        JCFChannelService channelService = new JCFChannelService();
        JCFMessageService messageService = new JCFMessageService();
        System.out.println("등록-------------------------------------------------------------------------------------------\n");

        // 등록
        System.out.println("Registering users...");
        userService.Create("고양이");
        userService.Create("겨울");
        userService.Create("봄");
        userService.Create("수정용User");
        userService.Create("삭제용User");

        // 채널 생성
        System.out.println("Creating channels...");
        channelService.Create(userService.getUser("고양이"), "고양이채널");
        channelService.Create(userService.getUser("겨울"), "겨울채널");
        channelService.Create(userService.getUser("겨울"), "비밀채널");
        channelService.Create(userService.getUser("수정용User"), "수정용Channel");
        channelService.Create(userService.getUser("고양이"), "삭제용Channel");


        // 메세지 생성
        System.out.println("Creating message...");
        messageService.Create(channelService.getChannel("고양이채널"), "치즈고양이는 위에서 보니 왕돈까스같다");
        messageService.Create(channelService.getChannel("겨울채널"), "겨울이채널에 메세지 생성");
        messageService.Create(channelService.getChannel("비밀채널"), "비밀채널에 메세지 생성");
        messageService.Create(channelService.getChannel("수정용Channel"), "수정용Message");
        messageService.Create(channelService.getChannel("고양이채널"), "삭제용Message");


        System.out.println("\n조회-------------------------------------------------------------------------------------------\n");
        
        // user 조회
        System.out.println("Reading all users...");
        userService.ReadAll(); // 전체 유저 조회
        System.out.println("Reading user by nickname...");
        userService.ReadUser("겨울"); // 특정 유저 조회
        
        System.out.println("-------");

        // channel 조회
        System.out.println("Reading all channels...");
        channelService.ReadAll();   // 전체 채널 조회
        System.out.println("Reading channel by name...");
        channelService.ReadChannel("겨울채널"); // 특정 채널 조회

        System.out.println("-------");
        
        // message 조회
        System.out.println("Reading all messages...");
        messageService.ReadAll(); // 전체 메세지 조회
        System.out.println("Reading all messages by number...");
        messageService.ReadMessage(0); // 특정 메세지 조회


        System.out.println("\n수정및 재조회-----------------------------------------------------------------------------------\n");

        // 수정된 데이터 조회, user
        System.out.println("Updating nickname of '수정용User'...");
        userService.UpdateNickname("수정용User"); // '수정용User' -> '가을'
        userService.ReadUser("가을"); // 수정된 닉네임 조회

         
        System.out.println("-------");

        // 수정된 데이터 조회, channel
        System.out.println("Updating channel name of '수정용UChannel'...");
        channelService.Update("수정용Channel"); // '수정용Channel' -> '가을채널'
        channelService.ReadChannel("가을채널"); // 수정된 채널 조회

        System.out.println("-------");

        // 수정된 데이터 조회, message
        System.out.println("Updating message text...");
        messageService.Update(3); // '수정용Message' 수정 (Key로 찾는 게 아니라 수정 문구 첨부x)
        messageService.ReadMessage(3);

        System.out.println("\n삭제및 재조회------------------------------------------------------------------------------------\n");

        // user 삭제
        System.out.print("User 삭제 전 : ");
        userService.ReadAll();
        System.out.println("Deleting user '삭제용User'...");
        userService.DeleteUser("삭제용User"); // '삭제용User' 유저 삭제
        System.out.print("User 삭제 후 : ");
        userService.ReadAll(); // 삭제 후 전체 유저 조회



        System.out.println("-------");
        // channel 삭제
        System.out.print("Channel 삭제 전 ) \n");
        channelService.ReadAll();
        System.out.println("Deleting channel '삭제용Channel'...");
        channelService.DeleteChannel("삭제용Channel"); // '삭제용Channel' 채널 삭제
        System.out.print("Channel 삭제 후 ) \n");
        channelService.ReadAll(); // 삭제 후 전체 채널 조회


        System.out.println("-------");
        // message 삭제
        System.out.print("Message 삭제 전 ) \n");
        messageService.ReadAll();
        System.out.println("Deleting channel '삭제용Message'...");
        messageService.DeleteMessage(4); // idx 4번 '삭제용Message' 삭제
        System.out.print("Message 삭제 후 ) \n");
        messageService.ReadAll();
    }
}
