package com.sprint.mission.discodeit.common;

import com.fasterxml.jackson.annotation.JsonValue;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// TODO: libphonenumber 라이브러리가 존재한다는 점
@Getter
@Setter
@AllArgsConstructor
public class Phone implements Serializable {
    @JsonValue
    private String phone;
    private RegionCode regionCode;

    public enum RegionCode {
        KR(),
        US(),
        ;

        public static RegionCode fromString(String value) {
            if (StringUtils.isBlank(value)) {
                throw new IllegalArgumentException("지역 코드는 null이 될 수 없습니다.");
            }
            try {
                return RegionCode.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("잘못된 지역 코드: " + value);
            }
        }
    }

    @Override
    public String toString() {
        return phone;
    }

}
