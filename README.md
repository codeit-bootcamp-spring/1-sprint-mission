요구사항
기본 요구사항
File IO를 통한 데이터 영속화
[ ]  다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.

[ ]  클래스 패키지명: com.sprint.mission.discodeit.service.file

[ ]  클래스 네이밍 규칙: File[인터페이스 이름]

[ ]  JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.

객체 직렬화/역직렬화 가이드

[ ]  Application에서 서비스 구현체를 File*Service로 바꾸어 테스트해보세요.

서비스 구현체 분석
[ ] JCF*Service 구현체와 File*Service 구현체를 비교하여 공통점과 차이점을 발견해보세요.
[ ] "비즈니스 로직"과 관련된 코드를 식별해보세요.
[ ] "저장 로직"과 관련된 코드를 식별해보세요.
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


스크린샷
![사용자_파일서비스_출력값.png](data/%EC%82%AC%EC%9A%A9%EC%9E%90_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)
![채널_파일서비스_출력값.png](data/%EC%B1%84%EB%84%90_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)
![메시지_파일서비스_출력값.png](data/%EB%A9%94%EC%8B%9C%EC%A7%80_%ED%8C%8C%EC%9D%BC%EC%84%9C%EB%B9%84%EC%8A%A4_%EC%B6%9C%EB%A0%A5%EA%B0%92.png)
