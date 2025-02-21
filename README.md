## Spring 핵심 개념 이해하기

JavaApplication: 기존의 수동 객체 생성 방식

- 기존의 JavaApplication 은 객체를 직접 생성하거나, 팩토리를 활용하여 수동으로 생성및 의존성 주입을 해주었다
- 객체간의 의존성을 직접 관리해야 하는 불편함이 있었다. 

DiscodeitApplication: Spring의 IoC 컨테이너 & DI 활용 방식

- Spring 이 객체를 Bean 으로 등록
- Spring IoC 컨테이너가 객체를 자동으로 관리
- DI 를 활용하여 개발자가 의존성을 직접 주입하는 것이 아닌 Spring 의 autowired 혹은 RequiredArgsConstructor 를 사용하여 자동 주입
- application.yaml 과 @ConditionalOnProperty 를 사용하여 자동으로 구현체를 환경에 맞게 변경
- 

