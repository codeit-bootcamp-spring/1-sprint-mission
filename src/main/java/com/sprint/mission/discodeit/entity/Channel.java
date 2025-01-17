package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String channel;
    private String description;
    private final User creator;//final로 변경

    public Channel(String channel, String description, User creator) {
        super();
        this.channel = channel;
        this.description = description;
        this.creator = creator;
    }

    public User getCreator() {
        return creator;
    }

    public String getChannel() {
        return channel;
    }

    public String getDescription() {
        return description;
    }

    public void update(String channel, String description) {
        this.channel = channel;
        this.description = description;
        update();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channel='" + channel + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                '}';
    }
}
