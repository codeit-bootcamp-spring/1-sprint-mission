package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.Set;

public class ChannelEntity extends CommonEntity {
    // 채널명
    private String channelName;
    // 채널설명
    private String description;
    // 허용된 유저 ID
    private Set<String> authorizedUsers;
    // 채널 삭제 플래그 channelDelFlg ( 0 : default, 1 : deleted )
    private String channelDelFlg;

    public ChannelEntity(String channelName, String description) {
        super();
        this.authorizedUsers = new HashSet<>();
        this.channelName = channelName;
        this.description = description;
        this.channelDelFlg = "0";
    }

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription(){
        return description;
    }

    public void updateDescription(String description){
        this.description = description;
    }

    public Set<String> getAuthorizedUsers(){
        return authorizedUsers;
    }

    public void updateAuthorizedUsers(Set<String> authorizedUsers) {
        this.authorizedUsers = authorizedUsers;
    }

    public String getChannelDelFlg() {
        return channelDelFlg;
    }

    public void updateChannelDelFlg(String channelDelFlg) {
        this.channelDelFlg = channelDelFlg;
    }
}
