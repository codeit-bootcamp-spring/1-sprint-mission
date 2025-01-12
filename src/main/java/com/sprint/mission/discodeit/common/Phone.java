package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.enums.RegionCode;

import java.util.regex.Pattern;

// TODO: libphonenumber 라이브러리 사용해보기!
public class Phone {
    private String phone;
    private RegionCode regionCode;

    public Phone(String phone, RegionCode regionCode){
        this.phone = phone;
        this.regionCode = regionCode;
    }

    public String getPhone() {
        return phone;
    }

    public RegionCode getRegionCode() {
        return regionCode;
    }

    public boolean validate(String phone, RegionCode regionCode){

        return true;
    }
}
