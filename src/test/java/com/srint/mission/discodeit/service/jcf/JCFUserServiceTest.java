package com.srint.mission.discodeit.service.jcf;

import com.srint.mission.discodeit.entity.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class JCFUserServiceTest {

    JCFUserService jcfUserService= new JCFUserService();

    @Test
    public void 유저_등록() throws Exception {
        //given
        User user = new User("userA", "abc@code.net","1234");

        //when
        UUID id = jcfUserService.save(user);

        //then
        Assert.assertEquals(user, jcfUserService.findOne(id));
    }


}