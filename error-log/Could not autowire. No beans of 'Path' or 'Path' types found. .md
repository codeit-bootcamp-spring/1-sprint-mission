Could not autowire. No beans of 'Path' or 'Path' types found.

문제 상황 :

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
    public BinaryContentStorage binaryContentStorage() {
        return new LocalBinaryContentStorage(Paths.get(rootPath)); // 여기서 Path를 생성하려는 시도
    }

해결 :

    @Bean
    @ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
    public BinaryContentStorage binaryContentStorage() {
        Path root = Paths.get(rootPath);  // 일반 객체로 생성
        return new LocalBinaryContentStorage(root);  // LocalBinaryContentStorage는 빈으로 반환
    }

왜 이게 이렇게 바꿔야 되는지는 파악 못함(정신없음, 다음에 찾아보겠음...)