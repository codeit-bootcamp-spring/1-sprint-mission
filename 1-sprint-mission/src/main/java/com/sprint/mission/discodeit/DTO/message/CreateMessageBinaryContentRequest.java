package com.sprint.mission.discodeit.DTO.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageBinaryContentRequest {
    private String title;
    private String body;
    private List<BinaryContent> binaryContents;
}