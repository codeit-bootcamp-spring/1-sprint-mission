package com.sprint.mission.discodeit.entity;

public class Channel extends Base{
    private String name;
    private String description;

    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        getUpdatedAt();
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
        getUpdatedAt();
    }
}
