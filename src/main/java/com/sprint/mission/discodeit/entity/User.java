package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String email;
    private List<Channel> channelList; // 참가중인 채널

    public User(String name, String email) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        channelList = new ArrayList<>();
        updateName(name);
        updateEmail(email);
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    public void setUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        if (name.isBlank()){
            System.out.println("닉네임을 입력해주세요.");
        } else {
            this.name = name;
        }
    }

    public void updateEmail(String email) {
        if (isValidEmail(email)){
            this.email = email;
        } else {
            System.out.println("이메일 형식에 맞지 않습니다.");
        }
    }

    public void addChannal(Channel newChannel) {
        channelList.add(newChannel);
    }

    public void removeChannel(Channel removeChannel) {
        channelList.remove(removeChannel);
    }

    private boolean isValidEmail(String email) {
        // 이메일 유효성 검사
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public String displayInfoUser() {
        return "User Name: " + name + ", User Email: " + email + "Channel List: " + channelList;
    }
}
