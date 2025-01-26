//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.service.ChannelService;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.*;
//
//
//public class JCFChannelService implements ChannelService {
//    private static JCFChannelService instance;
//    private final Map<UUID, Channel> ChannelList;
//
//    public JCFChannelService() {
//        this.ChannelList = new HashMap<>();
//    }
//
//    public static JCFChannelService getInstance() {
//        if (instance == null) {
//            instance = new JCFChannelService();
//        }
//        return instance;
//    }
//
//    public void participateChannel(User user, UUID chId) {
//        JCFUserService.getInstance().getUserList().get(user.getId()).setAttending(chId);
//        ChannelList.get(chId).getChannelUserList().add(user);
//    }
//
//    public List<User> getParticipants(UUID chId) {
//        return ChannelList.get(chId).getChannelUserList();
//    }
//
//    public List<Message> getMessagesFromChannel(UUID chId) {
//        return ChannelList.get(chId).getChannelMessageList();
//    }
//    public Map<UUID, Channel> getChannelList() {
//        return ChannelList;
//    }
//    @Override
//    public void createChannel(Channel channel) {
//        ChannelList.put(channel.getId(), channel); //JCFChannelList update
//        participateChannel(channel.getOwner(), channel.getId());
//        //participate's == below two lines
////        ChannelList.get(channel.getId()).getChannelUserList().add(channel.getOwner()); //channel's attending userList update
////        JCFUserService.getInstance().getUserList().get(channel.getOwner()).getAttending().add(channel.getId()); //owner attending
//    }
//    @Override
//    public Channel readChannelInfo(UUID id) {
//        Channel ch = ChannelList.get(id);
//        return ch;
//    }
//    @Override
//    public <T> void updateChannelField(UUID channelId, String fieldName, T contents) {
//        String filePath = "/Users/kimsangho/Desktop/sprintMission/1-sprint-mission/src/main/java/com/sprint/mission/discodeit/properties/UpdatableChannelField.txt";
//        //String filePath = "../../properties/UpdatableChannelField.txt";
//        Channel channel = ChannelList.get(channelId);
//        if (channel == null) {
//            System.out.println("Channel doesn't exist!");
//        }
//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String existingFieldName;
//            while ((existingFieldName = br.readLine()) != null) {
//                if (existingFieldName.equals(fieldName)) {
//                    String setterMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
//                    Method setterMethod = channel.getClass().getMethod(setterMethodName, contents.getClass());
//                    setterMethod.invoke(channel, contents);
//                    channel.setUpdatedAt();
//                    break;
//                }
//
//
//
//            }
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//
//
//
//    @Override //erase channel : 1. erase channel. 2. erase messages(JCFmsgList) source from this channel. 3. erase user's attending
//    public void deleteChannel(UUID idOfChannel) {
//        Channel ch = ChannelList.get(idOfChannel);
//        List<Message> srcMsg = ch.getChannelMessageList();
//        List<User> relatedUser = ch.getChannelUserList();
//
//
//
//
//        JCFMessageService.getInstance().getMesageList().removeIf(msg -> srcMsg.contains(msg));
//
//
//        ChannelList.remove(idOfChannel);
//
////        srcMsg.stream()
////                .map(msg -> msg.getId())
////                .forEach(JCFMessageService.getInstance()::deleteMessage);
//        //delete messages source from this channel <- JCFMsgList
//
//        //빌트인 함수가 최적화도 잘되고, 빠르겠지만, relatedUsr수가 매우 많다면? findFirst가 일종의 break기능?
//        for (User usr : relatedUser) {
//            //usr.getAttending().removeIf(ch_name -> ch_name.equals(idOfChannel));
//            //VS below
//            UUID tmp = usr.getAttending().stream()
//                    .filter(channel -> channel.equals(idOfChannel))
//                    .findFirst().orElse(null);
//
//            if (tmp != null) {
//                usr.getAttending().remove(tmp);
//            }
//        }
//    }
//
////        for(Message msg : relatedmsg) {
////            MessageService.deleteMessage(msg.getId());
////        }
//
////        for (User ulist: ch.getChannelUserList()) {
////            UserService.deleteUserById(ulist.getId());
////        }
//
//
//
//
//
//
////        MessageList.entrySet().removeIf(entry -> entry.getValue().getSource().equals(idOfChannel));
////        List<User> userList = ch.getChannelUserList();
////        for (User user : userList) {
////            user.getAttending().removeIf(channelName -> channelName.equals(ch.getChannelName()));
////            EveryMessageOfUser.remove(user.getId());
////        }
////        ChannelMessage.remove(idOfChannel);
//
//
//        public void printing() {
//            ChannelList.entrySet().stream()
//                    .map(entry->entry.getValue().toString())
//                    .forEach(s -> System.out.println(s));
//        }
//
//
//}
//
