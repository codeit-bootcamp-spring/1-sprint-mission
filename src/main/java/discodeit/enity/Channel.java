package discodeit.enity;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Common {
    String name;
    List<User> users;
    List<Message> messages;

    public Channel() {
        super();
    }

    public Channel(String name) {
        super();
        this.name = name;
        users = new ArrayList<>();
        messages = new ArrayList<>();
    }


    //Getter
    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    //Update
    public void updateName(String name) {
        this.name = name;
        updateClass();
    }
}
