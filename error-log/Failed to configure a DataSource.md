        Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
        2025-03-12T14:01:31.880+09:00 ERROR 56604 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   :
        
        ***************************
        APPLICATION FAILED TO START
        ***************************
        
        Description:
        
        Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
        
        Reason: Failed to determine a suitable driver class

        Action:
        
        Consider the following:
        If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
        If you have database settings to be loaded from a particular profile you may need to activate it (no
        profiles are currently active).

-----
application.yml

        spring:
            config:
                activate:
                    on-profile: dev

spring.config.activate.on-profile: 프로파일 활성화 시 사용할 속성을 정의한다.
즉, spring.config.activate.on-profile: dev 설정은 dev 프로파일이 활성화될 때만 아래의 설정이 적용되도록 한다.
이로 인해 데이터베이스 연결 설정이 누락되어 오류가 발생했다.
주로 단일 파일 내에서 여러 프로파일을 설정할 때 필요한 속성이란 것이다.

왜 문제가 일어났는가? :
(1) dev 가 active 되어 있지 않아 바로 하단에 있던 DB 설정이 무시됐다.
(2) 애초에 application-dev.yml, application.yml 이 따로 있는 상황에서 on-profile 을 넣으면 안됐다.

해결 :
-> 그래서 삭제했습니다.


-----
[[SpringBoot]Profile 사용하기](https://lordofkangs.tistory.com/320)
