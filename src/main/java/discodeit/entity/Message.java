package discodeit.entity;

import java.util.UUID;

public class Message {
    private final String messageId;
    private User sendUser;
    private User receiveUser;
    private String messageDetail;
    private final long createdAt;
    private long updatedAt;
    private Channel channel;

    public Message(User sendUser, User receiveUser, Channel channel, String messageDetail) {
        messageId = UUID.randomUUID().toString();
        this.sendUser = sendUser;
        this.receiveUser = receiveUser;
        this.messageDetail = messageDetail;
        createdAt = System.currentTimeMillis();
        this.channel = channel;
    }

    // Getter
    public String getMessageId() { return messageId; }

    public User getSendUser() { return sendUser; }

    public User getReceiveUser() { return receiveUser; }

    public Channel getChannel() { return channel; }

    public String getMessageDetail() { return messageDetail; }

    public long getCreatedAt() { return createdAt; }

    public long getUpdatedAt() { return updatedAt; }

    // Setter
    public void updateMessageDetail(String messageDetail) {
        this.messageDetail = messageDetail;
        updatedAt = System.currentTimeMillis();
    }

}
