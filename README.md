# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

# 👾 Discodeit

---
## 프로젝트 소개

---
- Java로 만든 채팅 서비스 입니다.

## 주요 기능

---

### 🍇 유저
- 유저 생성 기능
    - 이메일, 핸드폰 번호 중복 불가
- 유저 정보 업데이트 기능
    - 이름
    - 이메일
    - 핸드폰 번호
    - 비밀번호
- 유저 탈퇴 기능
    - 가입한 채널에서 자동 퇴장
    - 메시지에서 **(알 수 없음)**으로 표시

### 🍇 채널
- 채널 생성 기능
    - 채널을 생성한 user를 owner로 지정
- 채널 참가 기능
- 채널 탈퇴 기능
- owner 채널 조작 기능
    - 채널 이름 변경
    - 채널 소개 변경
    - 채널 삭제

### 🍇 메시지
- 채널에서 메시지 주고 받기 기능
- 메시지 수정 기능
- 메시지 삭제 기능

## 실행 결과 예시

---
```
// 유저1 조회
user1님의 정보입니다.
Name: user1
Email: user1@codeit.com
Phone number: 010-1234-5678
Joined Channels: 

// 채널 조회
channel1 | user1의 채널입니다.
Owner: user1
Participants: user2, user3
-- Messages -- 
user1: 안녕하세요~
user2: user2라고 합니다.
user3: 잘 부탁하빈다. ^^

// 유저2 회원 탈퇴, 유저3 메시지 수정
channel1 | user1의 채널입니다.
Owner: user1
Participants: user3
-- Messages -- 
user1: 안녕하세요~
(알 수 없음): user2라고 합니다.
user3: 잘 부탁합니다. ^^
```