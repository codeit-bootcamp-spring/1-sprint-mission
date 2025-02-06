package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * defines Basic as cache + DBMS
 * data load + implementation CRUD
 */
public class BasicUserService implements UserService {
    private static BasicUserService instance;
    private Map<UUID, User> UserList;
    public BasicUserService() {
        this.UserList = new HashMap<>();
    }
    public static BasicUserService getInstance() {
        if (instance == null) {
            instance = new BasicUserService();
        }
        return instance;
    }

    @Override
    public void createUser(User user) {
        UserList.put(user.getId(), user);
    }
    /*
     * To Maximize advantage of HashMap,
     * Using get method is appropriate.
     */
    @Override // Read Information of User using UUID
    public User readUserById(UUID userId) {
        return UserList.get(userId);
    }


    /**
     *
     * applied reflection for extension(member field feature update)
     */
    @Override
    public void updateUserField(UUID userId, String fieldName, String contents) {
        String filePath = "/Users/kimsangho/Desktop/sprintMission/1-sprint-mission/src/main/java/com/sprint/mission/discodeit/properties/UpdatableUserField.txt";
        //String filePath = "../../properties/UpdatableUserField.txt"; //if set up file path like this, it doesn't work
        User user = UserList.get(userId);
        if (user == null) {
            System.out.println("User doesn't exist!");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String existingFieldName;
            while ((existingFieldName = br.readLine()) != null) {
                if (existingFieldName.equals(fieldName)) {

                    Method setterMethod = user.getClass().getMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), String.class);
                    setterMethod.invoke(user, contents);
                    user.setUpdatedAt();
                    break;
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteUserById(UUID userId) {
        User user = UserList.remove(userId);
        Map<UUID, Channel> chList = BasicChannelService.getInstance().getChannelList();
/** refactor: apply Stream API instead of for-each
 for (UUID channelId : user.getAttending()) {

 Channel channel = channelService.readChannelInfo(channelId);
 if (channel != null) {
 chList.remove(userId);
 }
 } //channel attendance deletion

 */



        user.getAttending().forEach(channelId -> {
            Channel channel = BasicChannelService.getInstance().readChannelInfo(channelId);
            if (channel != null) {
                channel.getChannelMessageList().removeIf(msg -> msg.getWriter().equals(userId));
            }
        }); //deletion message as source of channel(Channel class's message related props)



        user.getAttending().forEach(channelId -> {
            Channel channel = BasicChannelService.getInstance().readChannelInfo(channelId);
            if (channel != null) {
                channel.getChannelUserList().removeIf(usr -> usr.getId().equals(userId));
            }
        }); //deletion user as participant of channel (Channel class's user related props)


        chList.entrySet()
                .removeIf(entry -> entry
                        .getValue()
                        .getOwner()
                        .equals(userId));
        //deletion user's channel.

        List<Message> msgList = BasicMessageService.getInstance().getMessageList();
        msgList =  msgList.stream()
                .filter(msg -> !msg
                        .getWriter()
                        .equals(userId)
                ).collect(Collectors.toList());
        //deletion user's message


    }


    @Override
    public Map<UUID, User> getUserList() {
        return UserList;
    }

    public void setUserList(Map<UUID, User> userList) {
        this.UserList = userList;
    }

    public void printing() {
        UserList.entrySet().stream()
                .map(entry->entry.getValue().toString())
                .forEach(s -> System.out.println(s));
    }
}
