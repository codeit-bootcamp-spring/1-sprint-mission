package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private final List<Channel> channelList; // 참가중인 채널
    private long updatedAt;
    private String name;
    private String email;

    public User(String name, String email) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        channelList = new ArrayList<>();
        setName(name);
        setEmail(email);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public List<Channel> getChannelList() {
        return channelList;
    }

    private boolean setName(String name) {
        if (name.isBlank()) {
            return false;
        }
        this.name = name;
        return true;
    }

    private boolean setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
            return true;
        }
        return false;
    }

    private void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        if (setName(name)){
            updateUpdatedAt();
        } else {
            System.out.println("닉네임을 입력해주세요.");
        }
    }

    public void updateEmail(String email) {
        if (setEmail(email)){
            updateUpdatedAt();
        } else {
            System.out.println("이메일 형식에 맞지 않습니다.");
        }
    }

    public void addChannel(Channel newChannel) {
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
        return "User Name: " + name + ", User Email: " + email + ", Channel List: " + channelList;
    }
}
