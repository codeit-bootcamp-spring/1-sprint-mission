<<<<<<< Updated upstream
# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## 1. 미션의 주요 기능과 목적
이 프로젝트는 디스코드와 유사한 서비스를 구현하기 위해 각 도메인 모델에 필요한 정보를 도출하고, 이를 Java 클래스로 구현하는 미션입니다. 목표는 도메인 모델 설계부터 CRUD 기능까지 구현을 포함하여, 실제 서비스 구현에 필요한 기초 작업을 연습하는 것입니다.

### 1.1. 도메인 모델링
이 프로젝트에서는 다음과 같은 주요 도메인 모델을 정의하고 구현합니다:
- **User**: 시스템의 사용자를 나타내는 모델입니다.
- **Channel**: 사용자들이 메시지를 주고받는 채널을 나타내는 모델입니다.
- **Message**: 각 채널에서 주고받는 메시지를 나타내는 모델입니다.

### 1.2. 서비스 설계 및 구현
도메인 모델별로 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 정의하고, 이를 구현하는 클래스를 작성하여 Java Collections Framework(JCF)를 활용한 데이터 관리 및 서비스 구현을 합니다.
- 각 도메인 모델에 대해 기본적인 CRUD 메소드들이 구현됩니다.

### 1.3. 메인 클래스 구현하기
메인 클래스에서는 다음 기능들을 테스트합니다:
- **등록**: 사용자, 채널, 메시지를 등록합니다.
- **조회(단건, 다건)**: 등록된 사용자, 채널, 메시지를 조회합니다.
- **수정**: 기존 데이터를 수정합니다.
- **수정된 데이터 조회**: 수정된 데이터를 조회하여 반영 여부를 확인합니다.
- **삭제**: 데이터를 삭제합니다.
- **삭제 확인**: 삭제된 데이터가 제대로 반영되었는지 확인합니다.

## 2. 기술 스택 및 개발 환경

- **Java JDK 17**: 최신 Java 버전으로, 객체지향 프로그래밍 및 다양한 Java 기능을 사용합니다.
- **Gradle**: 빌드 시스템으로 Groovy DSL을 사용하여 의존성 관리와 빌드를 자동화합니다.
- **IntelliJ IDEA 2024.3.1.1**: Java 개발에 최적화된 IDE로, 프로젝트의 주요 개발 환경입니다.

## 3.프로젝트 구조

```bash
├── com/sprint/mission/discodeit
│   ├── entity           # 도메인 모델(User, Channel, Message)
│   ├── service          # CRUD 인터페이스 및 구현체
│   ├── service/jcf      # JCF 기반 CRUD 구현체
└── src/main/java        # 주요 소스 코드
```
- entity: 도메인 모델인 User, Channel, Message 클래스가 포함됩니다.
- service: CRUD 인터페이스 및 구현체가 포함됩니다.
- service/jcf: Java Collections Framework를 활용한 구현체들이 포함됩니다.

## 4. 기능 상세
### 4.1 도메인 모델
- **User**: 시스템의 사용자 정보(아이디, 생성일시 등)를 관리합니다.
- **Channel**: 메시지를 주고받는 채널을 정의하며, 사용자들이 속할 수 있습니다.
- **Message**: 채널에서 주고받는 메시지로, 발송자 및 내용을 포함합니다.
## 5.2. CRUD 기능
- 각 도메인 모델(User, Channel, Message)에 대해 CRUD 기능을 제공합니다.
- create(), read(), update(), delete() 메소드가 각각 구현되어 있으며, 각 도메인 모델에 대한 데이터 조작이 가능합니다.
=======
# 0-spring-mission
스프린트 미션 모범 답안 리포지토리입니다.
>>>>>>> Stashed changes
