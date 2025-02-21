## 요구사항

### 기본 요구사항
### 컨트롤러 레이어 구현
- [X] DiscodeitApplication의 테스트 로직은 삭제하세요.
- [x] 지금까지 구현한 서비스 로직을 활용해 웹 API를 구현하세요.
  - 이때 @RequestMapping만 사용해 구현해보세요.
  ### 웹 API 요구사항
  - [x]  웹 API의 예외를 전역으로 처리하세요.

### API 테스트
- [x] Postman을 활용해 컨트롤러를 테스트 하세요.
  - Postman API 테스트 결과를 다음과 같이 export하여 PR에 첨부해주세요.
  - [스프린트 미션 4.postman_collection.json](../../%EC%8A%A4%ED%94%84%EB%A6%B0%ED%8A%B8%20%EB%AF%B8%EC%85%98%204.postman_collection.json)
  - (postman이 이상하게 저장됐는데, 다 되는 것 확인했습니다!)

### 웹 API 요구사항
### 사용자 관리
- [x] 사용자를 등록할 수 있다.
- [x] 사용자 정보를 수정할 수 있다.
- [x] 사용자를 삭제할 수 있다.
- [x] 모든 사용자를 조회할 수 있다.
- [x] 사용자의 온라인 상태를 업데이트할 수 있다.

- 권한 관리
  - [X] 사용자는 로그인할 수 있다.

### 채널 관리
- [x] 공개 채널을 생성할 수 있다.
- [X] 비공개 채널을 생성할 수 있다.
- [X] 공개 채널의 정보를 수정할 수 있다.
- [x] 채널을 삭제할 수 있다.
- [x] 특정 사용자가 볼 수 있는 모든 채널 목록을 조회할 수 있다.

### 메시지 관리
- [x] 메시지를 보낼 수 있다.
- [x] 메시지를 수정할 수 있다.
- [x] 메시지를 삭제할 수 있다.
- [x] 특정 채널의 메시지 목록을 조회할 수 있다.

### 메시지 수신 정보 관리
- [x] 특정 채널의 메시지 수신 정보를 생성할 수 있다.
- [x] 특정 채널의 메시지 수신 정보를 수정할 수 있다.
- [X] 특정 사용자의 메시지 수신 정보를 조회할 수 있다.

### 바이너리 파일 다운로드
- [x] 바이너리 파일을 1개 또는 여러 개 조회할 수 있다.
[스프린트 미션 4.postman_collection.json](../../%EC%8A%A4%ED%94%84%EB%A6%B0%ED%8A%B8%20%EB%AF%B8%EC%85%98%204.postman_collection.json)


### 심화 요구 사항
### 정적 리소스 서빙
- [x] 다음의 파일을 활용하여 사용자 목록을 보여주는 화면을 서빙해보세요.

### 생성형 AI 활용
- [ ] 생성형 AI (Claude, ChatGPT 등)를 활용해서 위 이미지와 비슷한 화면을 생성 후 서빙해보세요.


## 주요 변경사항
응답 처리할 때 기준을 정했습니다. :
Create, Read → ResponseDTO 이용
Update, Delete → State만 반환

https://thalals.tistory.com/268
Spring MVC 에서 제공하는 @ResponseBody 를 통한 JSON 대신
프론트에 섬세하게 정보를 넘겨줄 수 있는 ResponseEntity를 사용한다.


1. *Service 의 create 메서드 반환값을 DTO로 변경
2. 전역 예외 처리 패키지 생성 후 클래스 생성
3. updateUserInfo 의 Request DTO 에서 id를 분리 + updateUserInfo 파라미터 수정
- PathVariable 에서 얻은 id 를 userUpdateRequest의 id값에 매핑하거나, userUpdateRequest 로만 반환할 수 있지만,
- URL을 보고 어떤 리소스를 수정하는지 명확히 하기 위해서,
- 그런 관점에서 분리하여 진행했습니다. - - - - -> 였는데, updateUserStateByUserId() 메서드를 이용하면서 아예 삭제했습니다.
- updateChannel Request DTO 에서 id를 분리 + ChannelUpdateRequest 파라미터 수정 
- updateMessageText Request DTO 에서 id를 분리

### 모범 답안 확인 후
4. BinaryContent와 User, Message Entity 들 사이의 참조 방향 변경
- 확장성이 낮고, 한 개체만 참조 가능하다는 점, 데이터 정합성등의 이유로 변경
- 이때, 참조는 Entity 자체(x) 각 Entity의 Id
- 기존 BinaryContent -> User, Message 참조
- User, Message -> BinaryContent 참조

5. BinaryContent에 들어가는 메타데이터 추가

6. userStatus와 user의 양방향 관계 제거 후 userStatus가 userId를 참조하는 단방향 관계로 변경


## 스크린샷

## 멘토에게

