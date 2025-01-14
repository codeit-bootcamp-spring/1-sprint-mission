package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Message extends Data {

    private String userName;
    private List<String> textList = new ArrayList<>();
    private Long createdAt;
    private Long updatedAt;

    public Message(String userName, List<String> textList) {
        this.userName = userName;
        this.textList = textList;
        this.createdAt = System.currentTimeMillis(); // 생성 시간
        this.updatedAt = this.createdAt;           // 초기 생성 시간과 동일
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getTextList() {
        return textList;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }

    public void setTextList(List<String> textList) {
        this.textList = textList;
        this.updatedAt = System.currentTimeMillis(); // 수정 시간 갱신
    }
}