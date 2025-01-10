package discodeit.entity;


public class Message extends Base {
    private String content;
    private User sender;
    private Channel channel;

    // 생성자
    public Message(String content, User sender, Channel channel) {
        super();
        this.content = content;
        this.sender = sender;
        this.channel = channel;
    }

    // Getter
    public String getContent() {
        return content;
    }
    public User getSender() {
        return sender;
    }
    public Channel getChannel() {
        return channel;
    }

    // Update
    public void updateContent(String content) {
        this.content = content;
    }
    public void updateSender(User sender) {
        this.sender = sender;
    }
    public void updateChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender +
                '}';
    }
}
