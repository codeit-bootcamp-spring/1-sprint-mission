## 상황 :

        java.io.IOException: Stream Closed
        at java.base/java.io.FileInputStream.length0(Native Method) ~[na:na]
        at java.base/java.io.FileInputStream.length(FileInputStream.java:358) ~[na:na]
        at java.base/java.io.FileInputStream.readAllBytes(FileInputStream.java:280) ~[na:na]
        at com.sprint.mission.discodeit.mapper.BinaryContentMapper.mapBytes(BinaryContentMapper.java:38) ~[main/:na]
        at com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl.toDto(BinaryContentMapperImpl.java:29) ~[main/:na]
        at com.sprint.mission.discodeit.service.basic.BasicBinaryContentService.createBinaryContent(BasicBinaryContentService.java:37) ~[main/:na]

User 객체를 생성함과 동시에 프로필 이미지인 BinaryContent 객체를 생성하던 도중 오류가 발생했다.
``BinaryContentMapper.mapBytes()`` 메서드에서 ``FileInputStream.readAllBytes()``를 호출하자, 이미 스트림이 닫혀 읽어올 수
없다는 IOException을 일으켰다.

## 문제 상황이 일어난 이유: : 스트림을 닫고 반환했다.

LocalBinaryContentStorage.java 에서 InputStream 을 반환하는 로직

        @Override
        public InputStream get(UUID id) {
        // 파일 가져올 경로 지정
        Path filePath = resolvePath(id);
        File file = filePath.toFile();
        
            // 파일 불러오기 로직
            try (FileInputStream inputStream = new FileInputStream(file)){;
              return inputStream;
            } catch (IOException e) {
              e.printStackTrace();
              return null;
            }
        }

try-with-resources 구문을 이용하면서 try의 괄호 안에서 ``FileInputStream inputStream = new FileInputStream(file)``
을
선언했고, 이 과정에서 Stream 자원이 자동으로 닫힌 것이 문제였다. LocalBinaryContentStorage.java 내부에서 스트림을 닫은 상태로 반환해서 Mapper
단에서 IOException이 발생했던 것이다.

    -> 디버깅을 통해 확인했을 때, try 까지는 fileInputStream.closed : false 를 유지하다가 catch 이후 fileInputStream.closed : true 가 되는 것을 확인했다.

## 해결 : 스트림을 열고 반환하자.

LocalBinaryContentStorage.java

        @Override
        public InputStream get(UUID id) {
        // 파일 가져올 경로 지정
        Path filePath = resolvePath(id);
        File file = filePath.toFile();
        
            // 파일 불러오기 로직
            try {
              FileInputStream fileInputStream = new FileInputStream(file);
              return fileInputStream;
            } catch (IOException e) {
              e.printStackTrace();
              return null;
            }
        }

``FileInputStream inputStream = new FileInputStream(file)``을 try 괄호가 아닌 내부에 선언한 후 반환했다.
즉, 스트림을 닫지 않은 채로 반환했다.

BinaryContentMapper.java

    @Named("mapBytes")
    byte[] mapBytes(UUID id) {
        try (InputStream inputStream = binaryContentStorage.get(id)) {  // 스트림을 닫도록 설정
            return inputStream.readAllBytes();  // 한 번만 읽고, 데이터를 바이트 배열로 반환
        } catch (IOException e) {
            throw new RuntimeException("Failed to read binary content for ID: " + id, e);
        }
    }

Mapper 단에서 try-with-resources 구문을 넣어 스트림을 닫아 자원을 관리한다.

자원 관리의 문제는 Mapper 단으로 넘기지만, 여기서도 try-catch 문은 유지한다. 파일을 읽는 중 생길 수있는 다양한 오류에 대해서 대응하기 위해서이다.
eg. 파일이 존재하지 않는다면 IOException의 하위 클래스인 FileNotFoundException 발생시켜야 한다.