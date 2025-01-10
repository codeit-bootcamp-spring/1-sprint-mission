package discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Base {
    private String channelName;
    private List<User> members;
    private List<Message> messages;

    // 생성자
    public Channel(String channelName) {
        super();
        this.channelName = channelName;
        members = new ArrayList<>();
        messages = new ArrayList<>();
    }

    // Getter
    public String getChannelName() {
        return channelName;
    }
    public List<User> getUsers() {
        return members;
    }
    public List<Message> getMessages() {
        return messages;
    }

    // Update
    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }
    public void updateUsers(List<User> users) {
        this.members = users;
    }
    public void updateMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", members=" + members +
                ", messages=" + messages +
                '}';
    }
}

