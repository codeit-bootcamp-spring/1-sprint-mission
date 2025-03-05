package com.sprint.mission.discodeit.service.jcf.interfaces;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface JCFMessageServiceInterface {


    public void showInfoMessage(Message message);
    public Message saveMessage(String text, User user, Channel channel, boolean haveContent , int contents);
    public void deleteMessage(Message message);
    public void modifyMessage(Message message, String content);
}
