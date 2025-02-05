Spring 핵심 개념 이해하기
[ ] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.

**JavaApplication의 초기화 방식**
public static void main(String[] args) {
    // 직접 객체를 생성하는 방식
    UserRepository userRepository = new FileUserRepository();
    ChannelRepository channelRepository = new FileChannelRepository();
    MessageRepository messageRepository = new FileMessageRepository();

    // Service 객체를 직접 생성
    UserService userService = new BasicUserService(userRepository);
    ChannelService channelService = new BasicChannelService(channelRepository);
    MessageService messageService = new BasicMessageService(messageRepository, channelRepository, userRepository);
}


**DiscodeitApplication의 초기화 방식**
public static void main(String[] args) {
// Spring IoC 컨테이너에서 Bean을 가져옴
ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

    // Spring이 관리하는 객체를 가져옴 (Dependency Injection)
    UserService userService = context.getBean(UserService.class);
    ChannelService channelService = context.getBean(ChannelService.class);
    MessageService messageService = context.getBean(MessageService.class);
}



IoC Container(제어의 역전)
기존 에는 개발자가 new 키워드를 사용해서 객체를 직접 생성하고 관리했다. 그렇지만 spring 방식으로 했을 때
spring의 IoC 컨테이너가 객체의 생성 및 의존성 주입을 담당하게 된다. 개발자는 context.getBean()을 사용해서 필요할 때
객체를 가져오기만 하면 된다
Dependency Injection
기존에는 UserRepository 객체를 먼저 만들고, BasicUserService에 직접 주입해야 한다.
Spring방식은 Spring이 자동으로 필요한 객체에 찾아서 넣어준다
Bean
기존에는 new로 생성하는 일반적인 Java 객체이며 필요할 때마다 직접 생성해야 한다.
Spring 방식은 @Service, @Repository, @Component 등의 어노테이션을 사용하면 Spring이 자동으로 Bean으로 등록한다
필요할 때마다 Spring Context에서 Bean을 가져오기만 하면된다.