package discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String introduction;
    private User owner;

    public Channel(String name, String introduction, User owner) {
        long currentUnixTime = System.currentTimeMillis() / 1000;
        this.id = UUID.randomUUID();
        this.createdAt = currentUnixTime;
        this.updatedAt = currentUnixTime;

        this.name = name;
        this.introduction = introduction;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis() / 1000;
    }

    public void update(String name, String introduction) {
        boolean updated = updateName(name) || updateIntroduction(introduction);
        if (updated) {
            updateUpdatedAt();
        }
    }

    public boolean updateName(String name) {
        if (this.name.equals(name)) {
            return false;
        }
        this.name = name;
        return true;
    }

    public boolean updateIntroduction(String introduction) {
        if (this.introduction.equals(introduction)) {
            return false;
        }
        this.introduction = introduction;
        return true;
    }

    @Override
    public String toString() {
        return String.format(
                name + " | " + introduction + System.lineSeparator()
                        + "Owner: " + owner.getName() + System.lineSeparator()
        );
    }
}
