package com.sprint.mission.discodeit.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    private UUID id; // 파일 ID

    private String filename; // 원본 파일명
    private String fileType; // MIME 타입 (예: image/png, application/pdf)
    private String filePath; // 서버에 저장된 파일 경로
}
