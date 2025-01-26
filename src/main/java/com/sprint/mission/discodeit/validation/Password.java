package com.sprint.mission.discodeit.validation;

public class Password {
    private String password;

    // 대문자, 소문자, 숫자, 특수문자 각각 하나 이상 포함, 6자리 이상
    public static String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{6,}$";

    // 생성자
    public Password(String password) {
        password = checkNull(password);
        passwordValidation(password);
        this.password = password;
    }

    // 비밀번호 수정
    public void updatePassword(String password) {
        password = checkNull(password);
        passwordValidation(password);
        this.password = password;
    }

    // 비밀번호 유효성 검사
    public void passwordValidation(String password) {
        if (matchPassword(password)) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호 형식입니다.\n" +
                    "비밀번호는 대문자, 소문자, 숫자, 특수문자를 각각 하나 이상 포함해야 하며, 6자리 이상이어야 합니다.");
        }
    }

    // 비밀번호가 null인지 확인하고 아니라면 trim()하여 반환
    public String checkNull(String password) {
        if (password == null || password.isBlank()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        return password.trim();
    }

    // 비밀번호 정규식 확인
    public boolean matchPassword(String password) {
        return !password.matches(PASSWORD_REGEX);
    }

    @Override
    public String toString() {
        return password;
    }
}
