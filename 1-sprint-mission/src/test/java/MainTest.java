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
        assertEquals("등록된 유저가 없습니다.", userService.ReadUserAll(), "실패1.");

        userService.CreateUserDefault("고구마");
        userService.CreateUserDefault("김치");
        userService.CreateUserDefault("우유");
        userService.CreateUserDefault("김치");
        assertNotNull(userService.ReadUser("고구마"), "실패2");
        assertEquals("해당하는 유저가 없습니다.", userService.ReadUser("돼지"));
        userService.UpdateUserName("고구마","감자");
        assertEquals("해당하는 유저가 없습니다.", userService.ReadUser("고구마"));
        assertNotNull(userService.ReadUser("고구마"), "감자");
        assertFalse(userService.DeleteUser("고구마"));
        assertTrue(userService.DeleteUser("감자"));

    }

    @Test
    public void testMessageService() {
        MessageService messageService=new JCFMessageService();
        assertEquals("등록된 메세지가 없습니다.", messageService.ReadMessageAll(), "실패.");
        messageService.CreateMessageDefault("안녕하세요","감자입니다");
        assertNotNull(messageService.ReadMessageAll(), "실패");
        assertNotNull(messageService.ReadMessage("안녕하세요"), "실패");
        assertEquals("해당하는 메세지가 없습니다.", messageService.ReadMessage("반가워요"));
        assertTrue(messageService.UpdateMessageBody("안녕하세요","고구마일껄요"));
        assertFalse(messageService.UpdateMessageBody("반가워요","고구마일껄요"));
        assertTrue(messageService.DeleteMessage("안녕하세요"));
        assertFalse(messageService.DeleteMessage("반가워요"));
        assertEquals("등록된 메세지가 없습니다.", messageService.ReadMessageAll(), "실패.");
        messageService.CreateMessageDefault("감자튀김이 꿈인","감자입니다");
        messageService.CreateMessageDefault("감자튀김은","버거킴");
        assertNotNull(messageService.ReadMessageAll(), "실패");

    }

    @Test
    public void testChannelService(){
        ChannelService channelService=new JCEFChannelService();
        assertEquals("등록된 채널이 없습니다.", channelService.ReadChannelAll(), "실패.");
        channelService.CreateChannelDefault("탄수화물");
        assertNotNull(channelService.ReadChannelAll(), "실패");
        assertNotNull(channelService.ReadChannel("탄수화물"), "실패");
        assertEquals("해당하는 채널이 없습니다.", channelService.ReadChannel("단백질"));
        assertFalse(channelService.UpdateChannelName("단백질","지방"));
        assertTrue(channelService.UpdateChannelName("탄수화물","지방"));
        assertEquals("해당하는 채널이 없습니다.", channelService.ReadChannel("탄수화물"));
        assertNotNull(channelService.ReadChannel("지방"), "실패");
        assertFalse(channelService.DeleteChannel("단백질"));
        assertTrue(channelService.DeleteChannel("지방"));
        assertEquals("등록된 채널이 없습니다.", channelService.ReadChannelAll(), "실패.");

    }
}


