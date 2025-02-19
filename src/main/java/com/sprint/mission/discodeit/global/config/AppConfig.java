package com.sprint.mission.discodeit.global.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//기존 import 대신 allow-bean-definition-overriding을 true로 바꾸게 된다면 어렵다 해서 componentscan으로 변경
@ComponentScan(basePackages = "com.sprint.mission.discodeit.global.config")
public class AppConfig {

}

/*

🚨 왜 추천하지 않는가?
중복 Bean이 등록되는 근본적인 문제를 해결하지 못함

원래 FileRepositoryConfig와 JCFRepositoryConfig는 하나만 활성화되어야 하는데, @Import로 인해 둘 다 로드되면서 충돌하는 거야.
allow-bean-definition-overriding=true를 사용하면 단순히 하나를 덮어써서 오류를 막을 뿐, 어떤 Bean이 활성화될지 예측하기 어려워.
디버깅이 어려워짐

애플리케이션 실행 시 어떤 Repository 설정이 적용됐는지 불명확해질 수 있어.
FileRepositoryConfig와 JCFRepositoryConfig 중 어떤 것이 덮어씌워졌는지 로깅을 확인하지 않으면 알 수 없음.
의도치 않은 Bean 교체 가능성

만약 다른 설정에서도 Bean 이름이 충돌하면, 의도하지 않게 기존 Bean이 대체될 수 있어.
특히 Spring Boot에서는 allow-bean-definition-overriding을 true로 설정하면 개발자가 예상하지 못한 동작이 발생할 수도 있어.

 */