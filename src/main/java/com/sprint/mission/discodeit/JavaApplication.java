package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCF_channel;
import com.sprint.mission.discodeit.service.jcf.JCF_message;
import com.sprint.mission.discodeit.service.jcf.JCF_user;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        JCF_channel jcf_channel = new JCF_channel();
        JCF_user jcf_user = new JCF_user();
        JCF_message jcf_message = new JCF_message();
        for(int i = 0; i < 10; i++) {
            Channel channel = new Channel(UUID.randomUUID(), System.currentTimeMillis());
            User user = new User(UUID.randomUUID(), System.currentTimeMillis());
            Message message = new Message(UUID.randomUUID(), System.currentTimeMillis());
            jcf_channel.Creat(channel);
            jcf_user.Creat(user);
            jcf_message.Creat(message);

        }
        //전체 조회
        System.out.println("user 전체 조회 \n " + jcf_user.AllWrite().toString());

        //부분 조회
        User user = new User(UUID.randomUUID(), System.currentTimeMillis());
        jcf_user.Creat(user);
        System.out.println("user 부분 조회 \n " + jcf_user.Write(user.GetId()).toString());

        //수정
        User updateUser = new User(UUID.randomUUID(), System.currentTimeMillis());
        jcf_user.Update(user, updateUser);
        System.out.println("user 수정된 데이터 조회 \n " + jcf_user.Write(updateUser.GetId()).toString());

        //삭제
        jcf_user.Delete(updateUser);
        System.out.println("user 전체 조회 \n" + jcf_user.AllWrite().toString());
    }
}
