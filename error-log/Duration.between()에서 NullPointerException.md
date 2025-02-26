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