//package com.sprint.mission.discodeit.service.file;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//
//import java.io.IOException;
//
//public interface FileInterface {
//    public void showInfoUser(User user) throws IOException, ClassNotFoundException;
//    public User saveUser(String nickName) throws IOException, ClassNotFoundException;
//    public void deleteUser(User user) throws IOException, ClassNotFoundException;
//    public void modifyUser(User user, String newName) throws IOException, ClassNotFoundException;
//
//    public void showInfoChannel(Channel channel) throws IOException, ClassNotFoundException;
//    public Channel saveChannel(String nickName, User user) throws IOException, ClassNotFoundException;
//    public void deleteChannel(Channel channel) throws IOException, ClassNotFoundException;
//    public void modifyChannel(Channel channel, String newName) throws IOException, ClassNotFoundException;
//
//    public void showInfoMessage(Message message) throws IOException, ClassNotFoundException;
//    public Message saveMessage(String content, User user, Channel channel) throws IOException, ClassNotFoundException;
//    public void deleteMessage(Message message) throws IOException, ClassNotFoundException;
//    public void modifyMessage(Message message, String content) throws IOException, ClassNotFoundException;
//}
