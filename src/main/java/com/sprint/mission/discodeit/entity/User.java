package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
public class User extends BaseEntity {
    private String userName;
    private String userEmail;
    private List<Channel> channelList; //유저가 가지고 있는 채널 리스트
    private String password;

    //생성자
    public User(String userName, String userEmail, String password) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
        this.channelList = new ArrayList<>();
    }
}