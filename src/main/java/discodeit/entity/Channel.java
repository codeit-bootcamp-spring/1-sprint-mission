package discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String introduction;

    public Channel(String name, String introduction) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        id = UUID.randomUUID();
        createdAt = currentUnixTime;
        updatedAt = currentUnixTime;

        this.name = name;
        this.introduction = introduction;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
