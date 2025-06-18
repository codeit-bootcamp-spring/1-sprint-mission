package com.sprint.mission.discodeit.storage;

/**
 * @Slf4j
 * @Component
 * @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3") public class
 * S3BinaryContentStorage implements BinaryContentStorage {
 * <p>
 * private final String accessKey; private final String secretKey; private final String region;
 * private final String bucket; private final int presignedUrlExpiration;
 * <p>
 * private final String DIR_ROOT = "test/"; private final String FILENAME_LINK_CHAR = "_";
 * <p>
 * private S3Client s3Client;
 * <p>
 * public S3BinaryContentStorage(
 * @Value("${discodeit.storage.s3.access-key-id}") String accessKey,
 * @Value("${discodeit.storage.s3.secret-access-key}") String secretKey,
 * @Value("${discodeit.storage.s3.region}") String region,
 * @Value("${discodeit.storage.s3.bucket}") String bucket,
 * @Value("${discodeit.storage.s3.presigned-url-expiration}") int presignedUrlExpiration ) {
 * this.accessKey = accessKey; this.secretKey = secretKey; this.region = region; this.bucket =
 * bucket; this.presignedUrlExpiration = presignedUrlExpiration; }
 * @PostConstruct private void init() { s3Client = getS3Client(); }
 * @Override public UUID put(UUID fileId, MultipartFile file) { try { File fileObj =
 * convertMultipartFileToFile(file); s3Client.putObject(PutObjectRequest.builder() .bucket(bucket)
 * .key(createKey(fileId, file.getOriginalFilename())) .build(),
 * Paths.get(fileObj.getAbsolutePath())); fileObj.delete(); return fileId; } catch (IOException ioe)
 * { throw new SaveFailedS3Exception(fileId); } }
 * @Override public InputStream get(UUID fileId) { ListObjectsV2Request request =
 * ListObjectsV2Request.builder() .bucket(bucket) .prefix(DIR_ROOT + fileId.toString()) .build();
 * <p>
 * ListObjectsV2Response response = s3Client.listObjectsV2(request); String key =
 * response.contents().stream() .map(S3Object::key) .findFirst() .orElseThrow(() -> new
 * FileNotFoundS3DirectoryException(fileId));
 * <p>
 * return s3Client.getObject(GetObjectRequest.builder() .bucket(bucket) .key(key) .build()); }
 * @Override public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto) {
 * try { String key = createKey(binaryContentResponseDto.id(), binaryContentResponseDto.fileName());
 * String contentType = binaryContentResponseDto.contentType();
 * <p>
 * String presignedUrl = generatePresignedUrl(key, contentType);
 * <p>
 * return ResponseEntity .status(HttpStatus.FOUND) .location(URI.create(presignedUrl)) .build(); }
 * catch (Exception e) { // todo: exception log.warn("Presigned URL 생성 실패: {}", e); throw new
 * FailedToCreateUrl(binaryContentResponseDto.fileName()); } }
 * <p>
 * private S3Client getS3Client() { return S3Client.builder() .region(Region.of(region))
 * .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) .build(); }
 * <p>
 * private String generatePresignedUrl(String key, String contentType) { try (S3Presigner presigner
 * = S3Presigner.builder() .region(Region.of(region))
 * .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) .build()) {
 * GetObjectRequest objectRequest = GetObjectRequest.builder() .bucket(bucket) .key(key)
 * .responseContentType(contentType) .build(); GetObjectPresignRequest presignRequest =
 * GetObjectPresignRequest.builder() .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
 * .getObjectRequest(objectRequest) .build(); PresignedGetObjectRequest presignedRequest =
 * presigner.presignGetObject(presignRequest); log.info("Presigned URL generated → [{}]",
 * presignedRequest.url()); log.info("Presigned URL method: [{}]",
 * presignedRequest.httpRequest().method());
 * <p>
 * return presignedRequest.url().toExternalForm(); } }
 * <p>
 * private File convertMultipartFileToFile(MultipartFile file) throws IOException { File convertFile
 * = new File(file.getOriginalFilename()); try (FileOutputStream fos = new
 * FileOutputStream(convertFile)) { fos.write(file.getBytes()); return convertFile; } catch
 * (IOException e) { // todo: exception 생성 필요 throw new
 * FailedToFileConvert(file.getOriginalFilename()); } }
 * <p>
 * private String createKey(UUID fileId, String originalFilename) { return DIR_ROOT +
 * fileId.toString() + FILENAME_LINK_CHAR + originalFilename; } }
 */