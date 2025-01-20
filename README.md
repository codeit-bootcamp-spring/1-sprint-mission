요구사항
기본 요구사항
File IO를 통한 데이터 영속화
[ ]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[ ]  클래스 패키지명: com.sprint.mission.discodeit.service.file

[ ]  클래스 네이밍 규칙: File[인터페이스 이름]

[ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.

객체 직렬화/역직렬화 가이드

[ ]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

스크린샷
![사용자_파일서비스_출력값.png](data/%EC%82%AC%EC%9A%A9%EC%9E%90_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)
![채널_파일서비스_출력값.png](data/%EC%B1%84%EB%84%90_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)
![메시지_파일서비스_출력값.png](data/%EB%A9%94%EC%8B%9C%EC%A7%80_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)


서비스 구현체 분석
[ ] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요.

[ ] "비즈니스 로직"과 관련된 코드를 식별해보세요.

[ ] "저장 로직"과 관련된 코드를 식별해보세요.

크게 두 구현체의 공통점과 차이점을 알 수 있습니다.

두 구현체의 공통점
1. 인터페이스 구현
   두 클래스 모두 *interface를 구현합니다.
2. 기능
   CRUD(Create, Read, Update, Delete) 기능을 제공합니다.

두 구현체의 차이점
1. 비즈니스 로직
   두 구현체 모두 유사하지만 데이터를 저장하는 방식이 다르기 때문에 약간의 차이가 있습니다.
   JCF*Service = 데이터가 메모리에 저장되어 있으므로 Map.containsKey나 Map.get을 통해 빠르게 확인할 수 있다.
   FileUserService = 파일에서 데이터를 읽어와야 하므로, List.stream()을 사용해 ID를 확인 해야한다.

2. 저장 로직
   두 구현체에서 완전히 다른 방식으로 구현 됩니다.

JCF*Service:
private final Map<String, Channel> channels = new HashMap<>();

코드처럼 메모리에서 데이터를 관리합니다.

File*Service:
private static final String FILE_PATH = "users.data";

private void saveAllUsers(List<User> users) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
        oos.writeObject(users);  // 파일에 직렬화하여 저장
    }   
}
위 코드처럼 데이터를 직렬화하여 파일을 생성해서 파일에 저장하여 영속성을 보장합니다.

레포지토리 설계 및 구현
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

심화 요구 사항

관심사 분리를 통한 레이어 간 의존성 주입

[ ] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[ ] 클래스 패키지명: com.sprint.mission.discodeit.service.basic

[ ] 클래스 네이밍 규칙: Basic[인터페이스 이름]

[ ] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.

[ ] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.

[ ] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)

[ ] Basic*Service 구현체를 활용하여 테스트해보세요.



