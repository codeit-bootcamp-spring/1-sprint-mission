package com.sprint.mission.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String firstId;

    @Setter
    private String name;

    @Setter
    private String password;

    private final Instant createAt;

    @Setter
    private Instant updateAt;

    @Getter(AccessLevel.NONE)
    private final List<Channel> channelList = new ArrayList<>();

    public User(String name, String password){
        this.name = name;
        this.password = password;
        id = UUID.randomUUID();
        firstId = id.toString().split("-")[0];
        createAt = Instant.now();
    }

    public List<Channel> getChannelsImmutable(){
        return Collections.unmodifiableList(channelList);
    }

    public void addChannel(Channel channel){
        if(!channelList.contains(channel)){
            channelList.add(channel);
            channel.addUser(this);
        }
        // 굳이 if 문 만든 이유 : 불필요한 updateAt 초기화 없애기 위해
    }

    public void removeChannel(Channel channel) {
        if(channelList.contains(channel)){
            channelList.remove(channel);
            channel.removeUser(this);
        }
    }

    public void removeAllChannel(){
        for (Channel channel : channelList) {
            removeChannel(channel);
            // 유저수 초기화하려면 channels.clear 로는 안되기 때문
        }
    }

    public User setNamePassword(String name, String password) {
        this.password = password;
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password);
    }

    @Override
    public String toString() {
        return "[" + firstId + "]" + "'" + name + "'";
    }


}
