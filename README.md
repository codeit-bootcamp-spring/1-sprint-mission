# Spring 백엔드 트랙 1기 스프린트 미션 제출 리포지토리

## 스프린트 미션 대상 서비스: 디스코드

[![codecov](https://codecov.io/github/silvarge/1-sprint-mission/graph/badge.svg?token=00FSRXRYS0)](https://codecov.io/github/silvarge/1-sprint-mission)

---

## Sprint 11 개요

- 비동기 기반 메시지 처리 및 알림 시스템 추가
- 캐시를 활용한 성능 최적화
- Kafka를 통한 이벤트 기반 처리
- Redis를 이용한 데이터 캐싱

## 구현 요구사항

### 기본 요구사항

- 비동기 적용하기
    - 파일 업로드 로직을 @Async 기반 비동기 처리로 리팩토링
    - 비동기 예외 처리 핸들러 구성
- 알림 기능 추가
    - Spring Event 기반 비동기 이벤트로 알림 발행
- 캐시 적용 (로컬 캐시)
    - Caffeine + Spring Cache 조합을 통해 로컬 캐시 구현
    - 다음 기능에 캐시 적용:
        - 사용자별 채널 목록 조회
        - 사용자별 알림 목록 조회
        - 사용자 목록 조회

### 심화 요구사항

- Spring Kafka 도입
    - 기존 Spring Event 구조를 Kafka 기반 이벤트 처리 방식으로 리팩토링
    - Kafka Consumer를 통해 알림 생성 로직 처리
- Redis Cache 도입
    - 기존 로컬 캐시를 Redis 기반 전역 캐시로 교체
    - TTL 설정, 캐시 키 전략, RedisCacheManager 사용

### 트러블 슈팅

- Kafka 기반 이벤트 처리 방식으로 리팩토링 중 역직렬화 이슈
    - 이벤트 정의 시 데이터 저장/전달의 기능만 하는 것으로 생각해서 Record class로 구현
    - 역직렬화 관련 예외가 발생해서 Record class를 일반 class로 변경하니 잘 됨 (이유는 알아볼 예정)
- Redis Cache를 도입하며, 기존 코드 리팩토링 중 Instant 데이터 타입의 직렬화/역직렬화 이슈
    - 예외 메시지에 나와있던대로, "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" 를 추가하여, Instant를
      직렬화/역직렬화 할 수 있도록 함