package com.sprint.mission.discodeit.entity;

public class Channel extends AbstractEntity {
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
    public String getDescription() {
        return description;
    }

    public void update(String name, String description){
        this.name = name;
        this.description = description;
        updateTimestamp();
    }
}

