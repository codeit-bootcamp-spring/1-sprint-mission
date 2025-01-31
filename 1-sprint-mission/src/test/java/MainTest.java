import com.sprint.mission.discodeit.repository.jcf.JCFChannelService;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageService;
import com.sprint.mission.discodeit.repository.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {


    @Test
    public void testUserService() {
        UserService userService=new JCFUserService();
        //UserService userService=new FileUserServiece();
        assertEquals("등록된 유저가 없습니다.", userService.readUserAll(), "실패1.");

        userService.createNewUser("고구마");
        userService.createNewUser("김치");
        userService.createNewUser("우유");
        userService.createNewUser("김치");
        assertNotNull(userService.readUser("고구마"), "실패2");
        assertEquals("해당하는 유저가 없습니다.", userService.readUser("돼지"));
        userService.updateUserName("고구마","감자");
        assertEquals("해당하는 유저가 없습니다.", userService.readUser("고구마"));
        assertNotNull(userService.readUser("고구마"), "감자");
        assertFalse(userService.deleteUser("고구마"));
        assertTrue(userService.deleteUser("감자"));

    }

    @Test
    public void testMessageService() {
        MessageService messageService=new JCFMessageService();
        //MessageService messageService=new FileMessageService();
        assertEquals("등록된 메세지가 없습니다.", messageService.readMessageAll(), "실패.");
        messageService.createNewMessage("안녕하세요", "감자입니다");
        assertNotNull(messageService.readMessageAll(), "실패");
        assertNotNull(messageService.readMessage("안녕하세요"), "실패");
        assertEquals("해당하는 메세지가 없습니다.", messageService.readMessage("반가워요"));
        assertTrue(messageService.updateMessageBody("안녕하세요", "고구마일껄요"));
        assertFalse(messageService.updateMessageBody("반가워요", "고구마일껄요"));
        assertTrue(messageService.deleteMessage("안녕하세요"));
        assertFalse(messageService.deleteMessage("반가워요"));
        assertEquals("등록된 메세지가 없습니다.", messageService.readMessageAll(), "실패.");
        messageService.createNewMessage("감자튀김이 꿈인", "감자입니다");
        messageService.createNewMessage("감자튀김은", "버거킴");
        assertNotNull(messageService.readMessageAll(), "실패");

    }

    @Test
    public void testChannelService(){
        ChannelService channelService=new JCFChannelService();
        //ChannelService channelService=new FileChannelService();
        assertEquals("등록된 채널이 없습니다.", channelService.readChannelAll(), "실패.");
        channelService.createNewChannel("탄수화물");
        assertNotNull(channelService.readChannelAll(), "실패");
        assertNotNull(channelService.readChannel("탄수화물"), "실패");
        assertEquals("해당하는 채널이 없습니다.", channelService.readChannel("단백질"));
        assertFalse(channelService.updateChannelName("단백질", "지방"));
        assertTrue(channelService.updateChannelName("탄수화물", "지방"));
        assertEquals("해당하는 채널이 없습니다.", channelService.readChannel("탄수화물"));
        assertNotNull(channelService.readChannel("지방"), "실패");
        assertFalse(channelService.deleteChannel("단백질"));
        assertTrue(channelService.deleteChannel("지방"));
        assertEquals("등록된 채널이 없습니다.", channelService.readChannelAll(), "실패.");

    }
}


