package discodeit.enity;

import java.util.UUID;

public class Message extends Common{
    private String content;
    private User sender;
    private Channel channel;

    public Message() {
        super();
    }
    public Message(User sender, Channel channel, String content){
        super();
        this.content=content;
        this.sender=sender;
        this.channel=channel;
    }

    //Getter
    public String getContent(){
        return content;
    }
    public User getSender(){
        return sender;
    }
    public Channel getChannel() { return channel; }
    // Update
    public void updateContent(String content){
        this.content=content;
        updateClass();
    }
}
