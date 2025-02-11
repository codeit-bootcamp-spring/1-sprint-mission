package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Getter
public class Channel {
    final private UUID id;
    final private Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private ChannelType channelType;
    private final Map<User,UserStatus> userList ;
    private  Map<User,ReadStatus> readStatusList;
    private final List<Message> messageList;




    private Channel(String channelName,ChannelType channelType, Map<User,ReadStatus> readStatusList){
        this.id=UUID.randomUUID();
        this.createdAt= Instant.now();
        this.updatedAt=null;
        this.channelName=channelName;
        this.channelType=channelType;
        this.readStatusList=readStatusList;
        this.userList=new HashMap<>();
        this.messageList=new ArrayList<>();

    };
    private Channel(UUID id,Instant createdAt,Instant updatedAt,String channelName,ChannelType channelType,Map<User,UserStatus> userList
            ,Map<User,ReadStatus> readStatusList,List<Message> messageList){
        this.id=id;
        this.createdAt= createdAt;
        this.updatedAt=updatedAt;
        this.channelName=channelName;
        this.channelType=channelType;
        this.userList=userList;
        this.readStatusList=readStatusList;
        this.messageList=messageList;
    };
    public static Channel createDefaultChannel(String channelName){
        return new Channel(channelName,ChannelType.PUBLIC,null);
    }

    public static Channel createChannelAll(UUID id,Instant createdAt,Instant updatedAt,String channelName,ChannelType channelType,Map<User,UserStatus> userList
    ,Map<User,ReadStatus> readStatusList,List<Message> messageList){
        return new Channel(id,createdAt,updatedAt,channelName,channelType,userList,readStatusList,messageList);
    }

    public static Channel createPrivateChannel(){
        Map<User,ReadStatus> readStatusList =new TreeMap<>();
        return new Channel("",ChannelType.PRIVATE,readStatusList);
    }

    public void updateUpdatedAt() {
        this.updatedAt= Instant.now();
    }
    public void updateChannelName(String ChannelName){
        this.channelName= ChannelName;
    }

    public void addUser(User user,UserStatus userStatus){
/// /빈 호출 순서
        System.out.println(user.getName()+"    "+userStatus.getId());
        System.out.println(channelName);
        userList.put(user,userStatus);

        for(Map.Entry<User,UserStatus> a: userList.entrySet()){
            System.out.println(a.getKey().getName());
            System.out.println(a.getValue().getUserId());
        }

    }


    public ReadStatus addReadStatus(User user){
        ReadStatus readStatus= ReadStatus.setUpReadStatus(user.getId(),this.id,false);
        readStatusList.put(user,readStatus);
        return readStatus;
    }

    public void addMessage(Message m){
        messageList.add(m);
    }

    public String readUserList(){
        StringBuilder userNames = new StringBuilder();
        for (Map.Entry<User, UserStatus> entry : userList.entrySet()) {
            userNames.append(entry.getKey().getName()).append("\n");
        }
        return userNames.toString();
    }

    public String readMessageList(){
        StringBuilder MessageName = new StringBuilder();
        for (Message  a: messageList) {
            MessageName.append(a.getTitle()).append("\n");
        }
        return MessageName.toString();
    }

    public List<UUID> getMessageIdList(){
        return messageList.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    public List<UUID> getUserIdList(){
        return userList.keySet().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
    public List<UUID> getUserStatusIdList(){
        return userList.values().stream()
                .map(UserStatus::getId)
                .collect(Collectors.toList());
    }
    public List<UUID> getReadStatysIdKist(){
        if(readStatusList==null)
            return new ArrayList<>();
        return readStatusList.values().stream()
                .map(ReadStatus::getId)
                .collect(Collectors.toList());
    }

    public void deleteUser(String name){
        User target=null;
        for(Map.Entry<User, UserStatus> userEntry: userList.entrySet()){
            if (name.equals(userEntry.getKey().getName())){
                target=userEntry.getKey();
            }
        }
        if(target!=null) {
            userList.remove(target);
            if(readStatusList!=null)
                readStatusList.remove(target);
        }
    }

    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        display.append("ChannelName: ").append(channelName)
                .append(" | ID: ").append(id)
                .append("\nCreatedAt: ").append(createdAt)
                .append(" | UpdatedAt: ").append(updatedAt == null ? "없음" : updatedAt)
                .append("\nChannelType: ").append(channelType)
                .append("\nUsers: ").append(userList.size()).append("명")
                //.append("\nReadStatus: ").append(readStatusList.size()).append("개")
                .append("\nMessages: ").append(messageList.size()).append("개")
                .append("\n");

        return display.toString();
    }



}
