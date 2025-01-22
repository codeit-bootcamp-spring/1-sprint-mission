package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel extends Common{
    //필드
    private String channelName; // 채널 이름
    private String notificationTitle; // 공지 제목
    private String notificationContent; // 공지 내용
    private String author; // 작성자

    public Channel(String channelName, String notificationTitle, String notificationContent, String author) {
        super();
        this.channelName = channelName;
        this.notificationTitle = notificationTitle;
        this.notificationContent = notificationContent;
        this.author = author;
    }
    //Getter
    public String getChannelName() {
        return channelName;
    }
    public String getNotificationTitle() {
        return notificationTitle;
    }
    public String getNotificationContent() {
        return notificationContent;
    }
    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "채널명='" + channelName + '\'' +
                ", 공지 제목='" + notificationTitle + '\'' +
                ", 공지 내용='" + notificationContent + '\'' +
                ", 작성자='" + author + '\'' +
                ", 생성='"+ getFormattedCreatedAt();
    }
    public String toStringUpdate() {
        return "채널명='" + channelName + '\'' +
                ", 공지 제목='" + notificationTitle + '\'' +
                ", 공지 내용='" + notificationContent + '\'' +
                ", 작성자='" + author + '\'' +
                ", 수정='"+ getFormattedUpdatedAt();
    }

    // update 메소드
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateAuthor(String author) {
        this.author = author;
        setUpdatedAt(System.currentTimeMillis());
    }
}
