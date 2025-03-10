package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "binary_contents")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent{
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "type_id")
    private UUID typeId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

//    @Lob
//    @JdbcTypeCode(SqlTypes.BINARY)
//    @Column(name = "bytes", columnDefinition = "BYTEA")
//    private byte[] bytes;

//    protected BinaryContent() { }

    //, byte[] bytes
    public BinaryContent(UUID typeId, String originalFilename, Long size, String contentType) {
        this.createdAt = Instant.now();
        this.typeId = typeId;
        this.fileName = originalFilename;
        this.size = size;
        this.contentType = contentType;
//        this.bytes = bytes;
    }
}
