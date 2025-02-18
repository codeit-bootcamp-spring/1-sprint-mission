package com.sprint.mission.discodeit.vo;

import java.io.Serial;
import java.io.Serializable;

public class PhoneNumber implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String phoneNumber;

    // 전화번호 정규식
    public static final String PHONENUMBER_REGEX1 = "\\d{3}-\\d{4}-\\d{4}$";
    public static final String PHONENUMBER_REGEX2 = "\\d{10,11}$";

    // 생성자
    public PhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumberValidation(phoneNumber);
        this.phoneNumber = phoneNumber.trim();
    }

    // 전화번호 유효성 검사
    public String phoneNumberValidation(String phoneNumber) {

        phoneNumber = checkNull(phoneNumber);

        if (matchPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("올바르지 않은 전화번호 형식입니다.");
        }

        return phoneNumber;
    }

    // 전화번호가 null인지 확인하고 아니라면 trim()하여 반환
    public String checkNull(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()){
            throw new IllegalArgumentException("전화번호를 입력해주세요.");
        }

        return phoneNumber.trim();
    }

    // 전화번호 형식 확인
    public boolean matchPhoneNumber(String phoneNumber) {

        // 정규식 둘 다 충족하지 못할 때 true 리턴
        return !phoneNumber.matches(PHONENUMBER_REGEX1)
                && !phoneNumber.matches(PHONENUMBER_REGEX2);
    }

    @Override
    public String toString(){
        return phoneNumber;
    }
}
