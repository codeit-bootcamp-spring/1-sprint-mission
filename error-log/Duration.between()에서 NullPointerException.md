## null일 때의 호출

프론트 엔드 코드를 적용시킨 후, index.html에서 로그인을 진행하니 아래와 같은 오류가 떴다.

    java.lang.NullPointerException: Cannot invoke "java.time.temporal.Temporal.until(java.time.temporal.Temporal, java.time.temporal.TemporalUnit)" because "startInclusive" is null
        at java.base/java.time.Duration.between(Duration.java:490) ~[na:na]
        at com.sprint.mission.discodeit.entity.UserStatus.isConnectedNow(UserStatus.java:49) ~[main/:na]

`startInclusive`가 `null`이라서, java.time.temporal.Temporal.until을 호출할 수 없다고 하고 있다.
`startInclusive`는 `Duration.between()` 메서드에서 첫 번째 파라미터로

    public boolean isConnectedNow() {
        this.now = Instant.now();
        isOnline = Duration.between(lastConnectAt, now).toMinutes() <= 5; // UserStatus.java:49
        return isOnline;
    }

UserStatus의 lastConnectAt이 `null`로 들어오고 있다는 것이었다.

그런데 이때 Postman을 이용하고 index.html 로 접속하지 않았을 땐 해당 오류가 발생하지 않았다.
index.html에서 lastConnectAt을 보내는 과정에서 문제가 있을 수 있다고 판단하여, 제일 먼저 필드명이 매칭되고 있는지 확인했다.

User 온라인 상태 업데이트 API 스펙의 Repuest 에서 Json로 전달되고 있는 데이터의 key명이 `newLastActiveAt` 인 걸 확인했다.
서버 상에서는 해당 데이터를 `lastActiveAt`으로 받고 있었으므로 데이터가 매핑되지 않아 `null`로 전송되고 있었던 거였다.

실제로 key 명칭과 필드 명칭을 통일하니 해당 오류가 일어나지 않았다.