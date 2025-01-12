package com.sprint.mission.discodeit.enums;

public enum RegionCode {
    // TODO: 더 잘 활용할 수 있는 방법 생각해보기
    KR(),
    US(),
    ;

    public static RegionCode fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("지역 코드는 null이 될 수 없습니다.");
        }
        try {
            return RegionCode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("잘못된 지역 코드: " + value);
        }
    }
}
