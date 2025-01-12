# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## 🌱 스프린트 미션 1 

### Static Factory Method
생성자를 통한 객체 생성 -> `static factory method`를 통한 객체 간접 관리


```java
public static void main(String[] args) {
    // 기존 객체 생성
    var user = new User(new USerName("SB_1기_백재우"));
    
    // static factory method
    var username = UserName.from("SB_1기_백재우");
    User.from(username);
}
```
### Singleton pattern

```java
class UserConverter {
    private static UserConverter INSTANCE;

    private UserConverter {
    }

    public static synchronized UserConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserConverter();
        }

        return INSTANCE;
    }
}

public static void main(String[] args) {
    var userConverter = UserConverter.getInstance();
}
```