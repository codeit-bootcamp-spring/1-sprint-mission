package discodeit.enity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Channel extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    String description;

    ChannelType type;
    List<UUID> usersId;


    public Channel(String name, String description, ChannelType type) {
        super();
        this.name = name;
        this.description = description;
        this.type = type;
        usersId = new ArrayList<>();
    }


    //Getter
    public String getName() {
        return name;
    }

    public List<UUID> getUsers() {
        return usersId;
    }

    public String getDescription() {
        return description;
    }

    //Update
    public void updateName(String name) {
        if (name == null) return;
        this.name = name;
        updateClass(System.currentTimeMillis());
    }

    public void updateDescription(String description) {
        if (description.equals(this.description)) return;
        this.description = description;
        updateClass(System.currentTimeMillis());
    }

    // 객체 직렬화 부분에서 ser 파일에 저장할 형식으로 추가
    @Override
    public String toString() {
        return "Channel{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                '}';
    }

    // 객체를 UUID로 비교하기 위해
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Channel channel = (Channel) obj;
        return Objects.equals(this.getId(), channel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }


}
