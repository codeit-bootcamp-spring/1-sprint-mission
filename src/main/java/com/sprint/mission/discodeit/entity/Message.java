package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Message extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String messageTitle;
    private String messageContent;
    private User messageSendUser;
    private User messageReceiveUser;

    public Message(String title, String messageContent, User messageSendUser, User messageReceiveUser) {
        super();
        setMessageTitle(title);
        setMessageContent(messageContent);
        setSenderAndReceiver(messageSendUser, messageReceiveUser);
    }

    private void setMessageTitle(String title) {
        checkMessageContent(title);
        this.messageTitle = title;
        setUpdatedAt();
    }

    private void setMessageContent(String messageContent) {
        checkMessageContent(messageContent);
        this.messageContent = messageContent;
        setUpdatedAt();
    }

    private void setSenderAndReceiver(User sender, User receiver) {
        checkSenderAndReceiver(sender, receiver);
        setSender(sender);
        setReceiver(receiver);
        setUpdatedAt();
    }

    private void setSender(User sender) {
        checkSender(sender);
        this.messageSendUser = sender;
        setUpdatedAt();
    }

    private void setReceiver(User receiver) {
        checkReceiver(receiver);
        this.messageReceiveUser = receiver;
        setUpdatedAt();
    }


    private void checkMessageContent(String messageContent) {
        if (messageContent == null || messageContent.isEmpty()) {
            throw new IllegalArgumentException("메시지 내용을 작성해주세요.");
        }

    }

    private void checkSenderAndReceiver(User messageSendUser, User messageReceiveUser) {
        if (messageSendUser == null || messageReceiveUser == null) {
            throw new IllegalArgumentException("메시지를 보내는 사람과 받는 사람을 작성해주세요.");
        } else if (messageSendUser.equals(messageReceiveUser)) {
            throw new IllegalArgumentException("메시지를 보내는 사람과 받는 사람이 같습니다.");
        }
    }

    private void checkSender(User sender) {
        if (sender == null) {
            throw new IllegalArgumentException("보낸 사람을 다시 지정하세요.");
        }
    }

    private void checkReceiver(User receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException("받는 사람을 다시 지정하세요.");
        }
    }

    public String updateMessageTitle(String updateMessageTitle) {
        setMessageTitle(updateMessageTitle);
        return this.messageTitle;
    }

    public String updateMessageContent(String updateMessageContent) {
        setMessageContent(updateMessageContent);
        return this.messageContent;
    }

    public void updateMessage(String title, String content) {
        setMessageTitle(title);
        setMessageContent(content);
    }

    public User updateSendUser(User updateSendUser) {
        setSender(updateSendUser);
        return this.messageSendUser;
    }

    public User updateReceiveUser(User updateReceiveUser) {
        setReceiver(updateReceiveUser);
        return this.messageReceiveUser;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public UUID getMessageId() {
        return getId();
    }

    public String getMessageContent() {
        return messageContent;
    }

    public User getMessageSendUser() {
        return messageSendUser;
    }

    public User getMessageReceiveUser() {
        return messageReceiveUser;
    }

    public Long getCreatedAt() {
        return getCreatedAtBaseObject();
    }

    public Long getUpdatedAt() {
        return getUpdatedAtBaseObject();
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageTitle='" + messageTitle + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", messageSendUser=" + messageSendUser +
                ", messageReceiveUser=" + messageReceiveUser +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(getId(), message.getId()) &&
                Objects.equals(messageTitle, message.messageTitle) &&
                Objects.equals(messageContent, message.messageContent) &&
                Objects.equals(messageSendUser, message.messageSendUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), messageTitle, messageContent, messageSendUser);
    }

}
