package discodeit.dto;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelInfoDto {
    private String channelName;
    private String channelDescription;
    private List<String> userNames;
    private List<String> formattedMessages;

    public ChannelInfoDto(String channelName, String channelDescription,
                          List<String> userNames, List<String> formattedMessages) {
        this.channelName = channelName;
        this.channelDescription = channelDescription;
        this.userNames = userNames;
        this.formattedMessages = formattedMessages;
    }

    @Override
    public String toString() {
        return "채널: " + channelName +
                " / 설명: " + channelDescription +
                " \n참여 중인 사용자: " + userNames;
    }

    // 메세지까지 모두 출력
    public String fullInfoToString() {
        String result = "채널: " + channelName +
                " / 설명: " + channelDescription +
                " \n참여 중인 사용자: " + userNames;
        if (formattedMessages == null || formattedMessages.isEmpty()) {
            result += "\n현재 메세지가 없습니다.\n";
        } else {
            result += "\n" + formattedMessages.stream()
                    .collect(Collectors.joining("\n"));
        }
        return result;
    }
}
