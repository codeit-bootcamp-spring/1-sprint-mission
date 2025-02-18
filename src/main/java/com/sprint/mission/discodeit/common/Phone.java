package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.enums.RegionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// TODO: libphonenumber 라이브러리가 존재한다는 점
@Getter
@Setter
@AllArgsConstructor
public class Phone implements Serializable {
    private String phone;
    private RegionCode regionCode;

    @Override
    public String toString() {
        return phone;
    }
}
