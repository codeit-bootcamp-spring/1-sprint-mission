package com.sprint.mission.discodeit.DTO.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageImageRequest {
    private String title;
    private String body;
    private List<char[]> imgs;
}