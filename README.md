# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## sprint mission 2 

## 요구사항
## 기본 요구사항
### File IO를 통한 데이터 영속화

[ ]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[ ]  클래스 패키지명: com.sprint.mission.discodeit.service.file

[ ]  클래스 네이밍 규칙: File[인터페이스 이름]

[ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.

### 객체 직렬화/역직렬화 가이드

[ ]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

### 서비스 구현체 분석

[ ] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요.

[ ] "비즈니스 로직"과 관련된 코드를 식별해보세요.

[ ] "저장 로직"과 관련된 코드를 식별해보세요.

### 레포지토리 설계 및 구현

[ ] "저장 로직"과 관련된 기능을 도메인 모델 별 인터페이스로 선언하세요.

[ ] 인터페이스 패키지명: com.sprint.mission.discodeit.repository

[ ] 인터페이스 네이밍 규칙: [도메인 모델 이름]Repository

[ ] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.

[ ] 클래스 패키지명: com.sprint.mission.discodeit.repository.jcf

[ ] 클래스 네이밍 규칙: JCF[인터페이스 이름]

[ ] 기존에 구현한 JCF*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

[ ] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요.

[ ] 클래스 패키지명: com.sprint.mission.discodeit.repository.file

[ ] 클래스 네이밍 규칙: File[인터페이스 이름]

[ ] 기존에 구현한 File*Service 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

### 심화 요구 사항

관심사 분리를 통한 레이어 간 의존성 주입

[ ] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[ ] 클래스 패키지명: com.sprint.mission.discodeit.service.basic

[ ] 클래스 네이밍 규칙: Basic[인터페이스 이름]

[ ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.

[ ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.

[ ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)

[ ] Basic*Service 구현체를 활용하여 테스트해보세요.

### 코드 템플릿
```
public class JavaApplication {
    static User setupUser(UserService userService) {
        User user = userService.create("woody", "woody@codeit.com", "woody1234");
        return user;
    }

    static Channel setupChannel(ChannelService channelService) {
        Channel channel = channelService.create(ChannelType.PUBLIC, "공지", "공지 채널입니다.");
        return channel;
    }

    static void messageCreateTest(MessageService messageService, Channel channel, User author) {
        Message message = messageService.create("안녕하세요.", channel.getId(), author.getId());
        System.out.println("메시지 생성: " + message.getId());
    }

    public static void main(String[] args) {
        // 서비스 초기화
        // TODO Basic*Service 구현체를 초기화하세요.
        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        // 셋업
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        // 테스트
        messageCreateTest(messageService, channel, user);
    }
}
```
[ ]  JCF*Repository  구현체를 활용하여 테스트해보세요.

[ ]  File*Repository 구현체를 활용하여 테스트해보세요.

[ ] 이전에 작성했던 코드(JCF*Service 또는 File*Service)와 비교해 어떤 차이가 있는지 정리해보세요.
