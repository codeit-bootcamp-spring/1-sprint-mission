package com.srint.mission.discodeit.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User{

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    private String username;
    private String email;
    private String password;
    private List<Channel> MyChannels;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.username = username;
        this.email = email;
        this.password = password;
        MyChannels = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Channel> getMyChannels() {
        return MyChannels;
    }

    public void setUsername(String username) {
        this.username = username;
        setUpdatedAt();
    }

    public void setEmail(String email) {
        this.email = email;
        setUpdatedAt();
    }

    public void setPassword(String password) {
        this.password = password;
        setUpdatedAt();
    }

    public void deleteMyChannels(Channel channel){
        //가입 여부 확인
        if(!MyChannels.contains(channel)){
            throw new IllegalStateException("채널에 가입하지 않았습니다.");
        }
        MyChannels.remove(channel);
    }

    //도메인에서 어느정도까지 로직을 수행해야하는지 확실한 개념이 부족.
    //도메인에서 누구에게 연관관계 책임을 부여할 지 감이 안잡힘.

    public boolean userCompare(User user){
        if(this.equals(user)) return true;
        else return false;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
