package com.sprint.mission.discodeit.dto.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PrivateChannelCreateDTO {

    private String name;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<UUID> userList;
    //UUID 오류 나서 String으로 받고, 사용할 때 UUID로 변환
    public PrivateChannelCreateDTO( String name,
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
        List<String> userList){
        this.name = name;
        this.userList= userList.stream().map(UUID::fromString).collect(Collectors.toList());
    }
}
