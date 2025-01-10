package com.sprint.mission.service.jcf;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public interface EntityService {
    public void showInfoUser(User user);
    public User creatUser(String nickName);
    public void deleteUser(User user);
    public void modifyUser(User user, String newName);

    public void showInfoChannel(Channel channel);
    public Channel saveChannel(String nickName, User user);
    public void deleteChannel(Channel channel);
    public void modifyChannel(Channel channel, String newName);

    public void showInfoMessage(Message message);
    public Message saveMessage(String content, User user, Channel channel);
    public void deleteMessage(Message message);
    public void modifyMessage(Message message, String content);
}
