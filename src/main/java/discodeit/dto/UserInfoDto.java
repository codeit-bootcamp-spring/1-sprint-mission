package discodeit.dto;

import java.util.List;

// 유저 정보를 읽기 위한 DTO
public class UserInfoDto {
    private String userName;
    private String userEmail;
    private Long created_at;
    private List<String> channelNames;  // 현재 소속된 채널명

    public UserInfoDto(String userName, String userEmail,
                       Long created_at, List<String> channelNames) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.created_at = created_at;
        this.channelNames = channelNames;
    }

    @Override
    public String toString() {
        return "이메일: " + userEmail +
                " / 이름: " + userName +
                " / 생성 시간: " + created_at +
                "\n소속 채널: " + ((channelNames == null || channelNames.isEmpty()) ? "" : channelNames);
    }
}
