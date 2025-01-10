package mission1.entity;


import java.util.Objects;
import java.util.UUID;

public class User {

    private final UUID id;
    private String name;
    private final String firstId;
    private String password;
    private final Long createAt;
    private Long updateAt;

    public User(String name, String password){
        this.name = name;
        this.password = password;
        id = UUID.randomUUID();
        String string = id.toString();
        firstId = string.split("-")[0];
        createAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void setName(String name) {
        updateAt = System.currentTimeMillis();
        this.name = name;
    }

    public void setPassword(String password) {
        updateAt = System.currentTimeMillis();
        this.password = password;
    }

    public User setNamePassword(String name, String password) {
        updateAt = System.currentTimeMillis();
        this.password = password;
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, createAt, updateAt);
    }

    @Override
    public String toString() {
        return "[" + firstId + "]" + "'" + name + "'";
    }


}
