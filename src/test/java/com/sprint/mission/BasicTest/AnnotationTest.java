package com.sprint.mission.BasicTest;

import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.ChannelType;
import com.sprint.mission.entity.main.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnnotationTest {

    @Test
    void toStringTest() {
        User user = new User("testName", "testEmail", "testPassword", null);
        System.out.println("user = " + user);
        //user = User(id=3cae1ede-9fc9-4449-b9ce-3f4fb6e44fc4, name=testName, email=testPassword, password=testEmail, profileImgId=null)

        Channel channel = new Channel("채널A", "설명 Test", ChannelType.PRIVATE);
        System.out.println("channel = " + channel);
        //channel = Channel(id=d1792a56-1141-42db-b871-eea106b0aa87, channelType=PRIVATE, name=채널A, description=설명 Test)
    }
}
