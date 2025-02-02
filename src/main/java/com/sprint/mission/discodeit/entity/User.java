package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;


public class User extends BaseEntity {
    private String userName;
    private String userEmail;
    private List<Channel> channelList; //유저가 가지고 있는 채널 리스트

    //생성자
    public User(String userName, String userEmail) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.channelList = new ArrayList<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public void addChannel(Channel channel) {
        this.channelList.add(channel);
    }
    public void removeChannel(Channel channel){
        this.channelList.remove(channel);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + getId() +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", channelList=" + channelList +
                '}';
    }
}