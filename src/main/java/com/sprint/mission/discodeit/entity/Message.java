package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends User{
    private String message;

    public Message(UUID id, String userName, String message) {
        super(id, userName);  // 부모 클래스(User)의 생성자 호출
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String newMessage){
        this.message = newMessage;
    }

}
