//package com.sprint.mission.discodeit.service.file;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
//import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//
//public class FileService implements Serializable, FileInterface {
//
//    private static final FileService INSTANCE = new FileService();
//    private FileService(){}
//    private final SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
//    public static FileService getInstance() {
//        return INSTANCE;
//    }
//
//    @Override
//    public void showInfoUser(User user) throws IOException, ClassNotFoundException {
//        System.out.println((FileUserRepository.getInstance().getUser(user)));
//    }
//
//    @Override
//    public User saveUser(String nickName) throws IOException, ClassNotFoundException{
//        return FileUserRepository.getInstance().createUser(nickName);
//    }
//
//    @Override
//    public void deleteUser(User user) throws IOException, ClassNotFoundException {
//        FileUserRepository.getInstance().deleteUser(user.getId());
//        FileChannelRepository.getInstance().deleteUser(user.getId());
//        FileMessageRepository.getInstance().modifyUserName(user.getId(),"Undefined");
//    }
//
//    @Override
//    public void modifyUser(User user, String newName) throws IOException, ClassNotFoundException {
//        FileUserRepository.getInstance().modifyUser(user.getId(),newName);
//        if(!FileChannelRepository.getInstance().getChannels().isEmpty()){
//            FileChannelRepository.getInstance().modifyChannelUserName(user.getId(),newName);
//        }
//        if(!FileMessageRepository.getInstance().getMessages().isEmpty()){
//            FileMessageRepository.getInstance().modifyUserName(user.getId(),newName);
//        }
//    }
//
//    @Override
//    public void showInfoChannel(Channel channel) throws IOException, ClassNotFoundException {
//        if(FileChannelRepository.getInstance().getChannel(channel) == null) System.out.println("없는 서버입니다");
//        else System.out.println(FileChannelRepository.getInstance().getChannel(channel));
//    }
//
//    @Override
//    public Channel saveChannel(String nickName, User user) throws IOException, ClassNotFoundException {
//        return FileChannelRepository.getInstance().createChannel(nickName,FileUserRepository.getInstance().getUser(user));
//    }
//
//    @Override
//    public void deleteChannel(Channel channel) throws IOException, ClassNotFoundException {
//        FileChannelRepository.getInstance().deleteChannel(channel.getChannelId());
//        FileMessageRepository.getInstance().deletedChannelMessage(channel.getChannelId());
//    }
//
//    @Override
//    public void modifyChannel(Channel channel, String newName) throws IOException, ClassNotFoundException {
//        FileChannelRepository.getInstance().modifyChannel(channel.getChannelId(), newName);
//        FileMessageRepository.getInstance().modifyChannelName(channel.getChannelId(),newName);
//    }
//
//    public void joinChannel(Channel channel, User user) throws IOException, ClassNotFoundException {
//        FileChannelRepository.getInstance().joinChannel(channel.getChannelId(),user.getId(),user.getName());
//    }
//
//    @Override
//    public void showInfoMessage(Message message) throws IOException, ClassNotFoundException {
//        System.out.println(FileMessageRepository.getInstance().getMessage(message));
//    }
//
//    @Override
//    public Message saveMessage(String content, User user, Channel channel) throws IOException, ClassNotFoundException {
//        return FileMessageRepository.getInstance().createMessage(content,FileUserRepository.getInstance().getUser(user).getId(), FileUserRepository.getInstance().getUser(user).getName(), channel.getChannelId(), channel.getName());
//    }
//
//    @Override
//    public void deleteMessage(Message message) throws IOException, ClassNotFoundException {
//        FileMessageRepository.getInstance().deleteMessage(message.getMessageId());
//    }
//
//    @Override
//    public void modifyMessage(Message message, String content) throws IOException, ClassNotFoundException {
//        FileMessageRepository.getInstance().modifyMessage(message.getUserId(),message.getMessageId(),content);
//    }
//}
//
