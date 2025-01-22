package discodeit.enity;

import java.util.ArrayList;
import java.util.List;

public class User extends Common {

    private String name;
    private String email;
    private List<Channel> channels;

    public User() {
        super();
    }

    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
        channels = new ArrayList<>();
    }

    //Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    // Update
    public void updateName(String name) {
        this.name = name;
        updateClass();
    }

    public void updateEmail(String email) {
        this.email = email;
        updateClass();
    }
}
