package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateMessageDto {
    @NotBlank private String userId;
    @NotBlank @Size(min = 1, max = 1000) private String content;
    private List<MultipartFile> multipart;

    public CreateMessageDto() {}
}
