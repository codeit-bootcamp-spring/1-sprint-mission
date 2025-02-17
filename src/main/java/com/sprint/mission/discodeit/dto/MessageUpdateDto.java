package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class MessageUpdateDto {

    @NotNull
    private String userId;
    @NotBlank
    private String content;
    private List<MultipartFile> binaryContent;

    public MessageUpdateDto(){}
}
