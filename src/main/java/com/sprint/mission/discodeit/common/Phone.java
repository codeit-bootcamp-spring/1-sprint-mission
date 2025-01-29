package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.enums.RegionCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// TODO: libphonenumber 라이브러리 사용해보기!
@Getter
@Setter
public class Phone implements Serializable {
    private String phoneNum;
    private RegionCode regionCode;

    public Phone(String phoneNum, RegionCode regionCode) {
        this.phoneNum = phoneNum;
        this.regionCode = regionCode;
    }
}
