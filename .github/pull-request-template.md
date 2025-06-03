# 요구사항

## 비동기 적용하기

- [ ] `BinaryContentStorage`의 파일 업로드 로직을 비동기적으로 처리하세요.
    - 비동기 처리 시 MDC의 Request ID, SecurityContext의 인증 정보가 유지되도록 하세요.
- [ ] 업로드 중 예외가 발생한 경우, 자동 재시도 메커니즘을 구현하세요.
    - Spring Retry의 `@Retryable` 어노테이션을 사용해 재시도 정책(횟수, 대기 시간 등)을 설정하세요.
- [ ] 재시도 횟수를 초과한 경우, 실패 정보(`AsyncTaskFailure`)를 기록하는 복구 로직을 실행하세요.
    - `@Recover` 어노테이션을 사용해 재시도 실패 처리 메서드를 구현하세요.
    - 실패 정보(`AsyncTaskFailure`)에는 MDC의 Request ID를 포함하여 추적이 가능하도록 하세요.

- [ ] 파일 업로드 메소드를 호출한 곳(`BasicMessageService`, `BasicUserService` 등)에서 비동기 처리 성공/실패에 따라 `BinaryContent`의 상태를 업데이트 하세요.
    - `BinaryContent` 엔티티에 업로드 상태를 나타내는 속성(`uploadStatus`)을 추가하세요.
        - `WAITING`: 업로드 대기 중 (파일 저장 요청만 완료된 상태)
        - `SUCCESS`: 업로드 완료
        - `FAILED`: 업로드 실패 (모든 재시도 실패)
    - `WAITING`으로 초기화하세요.
    - 업로드 성공 시, SUCCESS로 업데이트하세요.
    - 업로드 실패 시, FAILED로 업데이트하세요.
        - 의도적인 예외를 발생시켜 테스트해보세요.

    - [ ] 동기 처리와 비동기 처리 간의 성능 차이를 비교해보세요.
        - 파일 업로드 로직에 의도적인 지연(`Thread.sleep(...)`)을 발생시키세요.
        - `@Timed` 어노테이션을 활용하여 API의 실행 시간을 측정하고, 동기/비동기 처리 방식 간 응답 속도 차이를 분석하고 PR에 해당 내용을 포함하세요.

## 알림 기능 추가하기

- [ ] ReadStatus 엔티티를 리팩토링하세요.
    - 새로운 메시지에 대한 알림 활성화 여부를 관리하는 속성(`notificationEnabled`)을 추가하세요.
    - 사용자가 해당 알림 설정을 변경할 수 있게 **API**를 수정하세요.
    - `PRIVATE` 채널은 알림을 활성화하는 것을 기본으로 합니다.
    - `PUBLIC` 채널은 알림을 비활성화하는 것을 기본으로 합니다.

- [ ] 알림 관련 API를 구현하세요.
    - 알림 조회, 확인(삭제)은 요청자 본인의 알림에 대해서만 수행할 수 있습니다.
    - 알림 조회 및 삭제 시 현재 요청의 인증 정보로 요청자를 식별합니다.
    - 알림 확인은 삭제를 통해 수행합니다.
    - `receiverId`: 알림을 수신할 User의 id입니다.
    - `targetId`는 optional 입니다.

| 기능           | 엔드포인트                                      | 요청                       | 응답                         |
|--------------|--------------------------------------------|--------------------------|----------------------------|
| 알림 조회        | GET /api/notifications	                    | Header Authorization: …	 | - 200 List<NotifcationDto> |
| 알림 삭제(알림 확인) | DELETE /api/notifications/{notificationId} | Header Authorization: …  | - 204 Void                 |

- [ ] 다음 상황 발생 시, 해당 사용자에게 알림을 발행하세요.
    - 사용자가 알림을 활성화한 채팅방(`ReadStatus.notificationEnabled == true`)에 메시지가 등록된 경우
        - `type: NEW_MESSAGE, targetId: channelId`
    - 사용자의 권한(Role)이 변경된 경우
        - `type: ROLE_CHANGED, targetId: userId`
    - 해당 사용자의 요청에서 발생한 비동기 작업이 실패하여 실패 로그가 기록된 경우
        - `type: ASYNC_FAILED, targetId: requestId`

- [ ] 알림은 **Spring Event** 기반의 비동기 방식으로 발행되도록 구현하세요.
    - `@Async` + `@EventListener` 구조로 비동기 처리하세요.
    - 이벤트 처리 중 실패가 발생할 경우, 자동 재시도 메커니즘을 도입하세요.
    - 이벤트 발행 및 리스닝은 트랜잭션 경계(transaction boundary)를 고려하여 수행하세요.
        - `@TransactionalEventListener`

## 캐시 적용하기

- [ ] `Caffeine` 캐시를 적용하세요.
- [ ] 캐시가 필요하다고 판단되는 로직에 캐시를 적용하세요.
    - 예시:
        - 사용자별 채널 목록 조회
        - 사용자별 알림 목록 조회
        - 사용자 목록 조회
- [ ] 데이터 변경 시, 캐시를 갱신 또는 무효화하는 로직을 구현하세요.
    - `@CacheEvict`, `@CachePut`, **Spring Event** 등을 활용하세요.
    - 예시 :
        - 새로운 채널 추가 → 채널 목록 캐시 무효화
        - 알림 추가 → 알림 목록 캐시 무효화
        - 채널에 사용자 추가 → 사용자 목록 캐시 무효화

- [ ] 캐시 적용 전후의 차이를 비교해보세요
    - SQL 실행 횟수
    - 응답 시간
