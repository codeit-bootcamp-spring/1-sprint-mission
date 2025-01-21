package discodeit.entity;

import discodeit.ChannelType;

import java.util.ArrayList;
import java.util.List;

public class Channel extends Base {
    private String name;
    private String description;
    private ChannelType type;

    // 생성자
    public Channel(String name, String description, ChannelType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ChannelType getType() {
        return type;
    }

    // Update
    public void update(String newName, String newDescription, ChannelType newType) {
        boolean anyValueUpdated = false;

        if(newName != null && !newName.equals(name)) {
            name = newName;
            anyValueUpdated = true;
        }
        if(newDescription != null && !newDescription.equals(description)) {
            description = newDescription;
            anyValueUpdated = true;
        }
        if(newType != null && !newType.equals(type)) {
            type = newType;
            anyValueUpdated = true;
        }

        if(anyValueUpdated) {
            this.updateUpdatedAt();
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}

