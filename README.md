# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리
---
# 👾 Discodeit


## 프로젝트 소개


- Java로 만든 채팅 서비스 입니다.

## 주요 기능



### 🍇 유저
- 유저 생성 기능
    - 이메일, 핸드폰 번호 중복 불가
- 유저 정보 업데이트 기능
    - 이름
    - 이메일
    - 핸드폰 번호
    - 비밀번호
- 유저 삭제 기능
- 유저 객체 직렬화 기능 (.ser)

### 🍇 채널
- 채널 생성 기능
    - 채널을 생성한 user를 owner로 지정
- 채널 업데이트 기능
  - 이름
  - 소개
- 채널 삭제 기능
- 채널 객체 직렬화 기능 (.ser)

### 🍇 메시지
- 메시지 수정 기능
- 메시지 삭제 기능
- 메시지 객체 직렬화 기능 (.ser)

## 실행 결과 예시


```
유저 생성
user님의 정보입니다.
Name: user
Email: user@codeit.com
Phone number: 010-1111-1111

유저 조회(단건)
user님의 정보입니다.
Name: user
Email: user@codeit.com
Phone number: 010-1111-1111

유저 조회(다건): 2

유저 수정
uuuuser님의 정보입니다.
Name: uuuuser
Email: user@codeit.com
Phone number: 010-1111-1111

유저 삭제: 1

채널 생성
공지 | 공지 채널입니다.
Owner: user

채널 조회(단건)
공지 | 공지 채널입니다.
Owner: user

채널 조회(다건): 2

채널 수정
공지 | 공지입니다
Owner: user

채널 삭제: 1

메시지 생성
user: 안녕하세요.

메시지 조회(단건)
user: 안녕하세요.

메시지 조회(다건): 2

메시지 수정
user: 반갑습니다.

메시지 삭제: 1
```
---
