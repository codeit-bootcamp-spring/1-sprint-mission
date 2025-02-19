package com.sprint.mission.discodeit.entity.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sprint.mission.discodeit.entity.BaseObject;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.message.NullMessageContentException;
import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@JsonRootName("message")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message extends BaseObject implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "messageTitle", required = true)
    private String messageTitle;

    @JsonProperty("messageContent")
    private String messageContent;

    @JsonProperty("messageSendUser")
    private User messageSendUser;

    @JsonProperty("messageReceiveUser")
    private User messageReceiveUser;

    public Message(UUID uuid) {}

    public Message() {
    }


    public Message(String title, String messageContent, User messageSendUser, User messageReceiveUser) {
        super();
        setMessageTitle(title);
        setMessageContent(messageContent);
        setSenderAndReceiver(messageSendUser, messageReceiveUser);
    }

    public Message(UUID messageId, String title, String messageContent, User messageSendUser, User messageReceiveUser) {
        super(messageId);
        setMessageTitle(title);
        setMessageContent(messageContent);
        setSenderAndReceiver(messageSendUser, messageReceiveUser);
    }

    private void setMessageTitle(String title) {
        checkMessageTitle(title);
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

    private void checkMessageTitle(String messageTitle) {
        if (messageTitle == null || messageTitle.isEmpty()) {
            throw new NullMessageTitleException();
        }

    }

    private void checkMessageContent(String messageContent) {
        if (messageContent == null || messageContent.isEmpty()) {
            throw new NullMessageContentException();
        }

    }

    private void checkSenderAndReceiver(User messageSendUser, User messageReceiveUser) {
        if (messageSendUser == null || messageReceiveUser == null) {
            throw new UserNotFoundException();
        } else if (messageSendUser.equals(messageReceiveUser)) {
            throw new UserNotFoundException("메시지를 보내는 사람과 받는 사람이 같습니다.");
        }
    }

    private void checkSender(User sender) {
        if (sender == null) {
            throw new UserNotFoundException("보낸 사람을 다시 지정하세요.");
        }
    }

    private void checkReceiver(User receiver) {
        if (receiver == null) {
            throw new UserNotFoundException("받는 사람을 다시 지정하세요.");
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
