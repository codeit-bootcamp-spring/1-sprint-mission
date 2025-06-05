package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.async.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.file.FileUploadFailedException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final String bucket;
    private final int expirationSeconds;

    @Async("contextAwareExecutor")
    @Retryable(
            value = {FileUploadFailedException.class},
            maxAttempts = 3, //3번까지 시도
            backoff = @Backoff(delay = 1000)
    )
    public CompletableFuture<Void> putAsync(UUID id, byte[] data,
            Consumer<BinaryContentUploadStatus> statusCallback) {
        try {
            put(id, data);
            statusCallback.accept(BinaryContentUploadStatus.SUCCESS);
            return CompletableFuture.completedFuture(null);
        } catch (FileUploadFailedException e) {
            throw new FileUploadFailedException();
        }
    }

    @Recover
    public CompletableFuture<Void> recover(FileUploadFailedException e, UUID id, byte[] data,
            Consumer<BinaryContentUploadStatus> statusCallback) {
        String requestId = MDC.get("requestId");
        String taskName = "FileUpload";
        String reason = e.getMessage();

        AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, reason);
        log.error("[UPLOAD RECOVERY] 비동기 작업 실패 - {}", failure);
        statusCallback.accept(BinaryContentUploadStatus.FAILED);
        return CompletableFuture.failedFuture(e);
    }


    @Override
    public UUID put(UUID id, byte[] data) {
        String key = id.toString();
        log.info("put to s3: id={}, size={}", id, data.length);
        s3.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentLength((long) data.length)
                        .build(),
                RequestBody.fromBytes(data));
        log.info("s3 업로드 완료: {}", key);
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        String key = id.toString();
        ResponseBytes<GetObjectResponse> response = s3.getObject(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build(),
                ResponseTransformer.toBytes());
        return new ByteArrayInputStream(response.asByteArray());
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto binaryContentDto) {
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(
                builder -> builder.getObjectRequest(
                                r -> r.bucket(bucket)
                                        .key(binaryContentDto.getId().toString())
                                        .responseContentType(binaryContentDto.getContentType()))
                        .signatureDuration(Duration.ofSeconds(expirationSeconds)));

        return ResponseEntity.status(302)
                .header("Location", presignedRequest.url().toString())
                .build();
    }
}
