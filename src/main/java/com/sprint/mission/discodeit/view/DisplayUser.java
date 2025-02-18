package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.dto.userDto.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.userDto.FindUserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.Comparator;
import java.util.List;

public class DisplayUser {

    // 단일 사용자 정보 출력
    public static void displayUserInfo(FindUserResponseDto user) {
        System.out.println(
                "이메일: " + user.getEmail()
                        + ", 이름: " + user.getName()
                        + ", 닉네임: " + user.getNickname()
                        + ", 전화번호: " + user.getPhoneNumber()
                        + ", 접속 여부: " + user.isOnline()
                        + "\n"
        );
    }

    // 단일 사용자 정보 출력
    public static void displayUserInfo(CreateUserRequestDto user) {
        System.out.println(
                "이메일: " + user.email()
                        + ", 이름: " + user.name()
                        + ", 닉네임: " + user.nickname()
                        + ", 전화번호: " + user.phoneNumber()
                        + "\n"
        );
    }

    // 모든 사용자 정보 출력
    public static void displayAllUserInfo(List<FindUserResponseDto> data) {
        data.stream()
                // 정렬 시 @ 앞까지만 잘라서 오름차순
                .sorted(Comparator.comparing(user -> user.getEmail().split("@")[0]))
                .forEach(DisplayUser::displayUserInfo);
    }


}
