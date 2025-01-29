package discodeit.enity;

import java.io.Serializable;
import java.util.Objects;

public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;

    private transient String password;


    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Update
    public void updateName(String name) {
        if (name == null || name.equals(this.name)) return;
        this.name = name;
        updateClass(System.currentTimeMillis());
    }

    public void updateEmail(String email) {
        if (email == null || email.equals(this.email)) return;
        this.email = email;
        updateClass(System.currentTimeMillis());
    }

    public void updatePassword(String password) {
        if (password == null || password.equals(this.password)) return;
        this.password = password;
        updateClass(System.currentTimeMillis());
    }

    // 객체 직렬화 부분에서 ser 파일에 저장할 형식으로 추가
    @Override
    public String toString() {
        return "User{" +
                "id='" + this.getId() + '\'' +
                ", name='" + this.name + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

    // UUID만으로 객체를 비교하기 위해 추가
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
