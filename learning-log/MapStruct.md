## 1. MapStruct란?

정의: MapStruct는 DTO, 엔티티 등 서로 다른 Java 객체 간의 변환(매핑)을 위한 라이브러리로, 인터페이스에 매핑 메서드만 선언하면 컴파일 시 자동으로 구현체(
Mapper 클래스)를 생성해 줍니다.

장점:

- 컴파일 타임 검증: 매핑 시 오류가 있다면 컴파일 단계에서 바로 잡을 수 있습니다.
- 빠른 성능: 런타임 시 리플렉션을 사용하지 않아 매우 빠릅니다.
- 코드 간결화: 반복적인 필드 매핑 코드를 자동으로 생성해 줍니다.
  예를 들어, DTO와 엔티티 간의 변환 작업을 수동으로 구현한다면 많은 보일러플레이트 코드가 필요하지만, MapStruct를 사용하면 인터페이스와 어노테이션 몇 줄로 이를
  해결할 수 있습니다.

## 2. 설치 방법

### Gradle 예시

    dependencies {
        implementation "org.mapstruct:mapstruct:1.5.5.Final"
        annotationProcessor "org.mapstruct:mapstruct-processor:1.5.5.Final"
    
        // Lombok을 같이 사용할 경우, Lombok 의존성은 MapStruct보다 먼저 선언되어야 합니다.
        compileOnly "org.projectlombok:lombok:1.18.24"
        annotationProcessor "org.projectlombok:lombok:1.18.24"

    }   

Lombok과 같이 사용하는 경우 Lombok 의존성을 MapStruct보다 먼저 추가해야 정상 동작합니다.
MapStruct는 Lombok의 getter, setter, builder를 이용해서 생성되므로 Lombok보다 먼저 dependency가 선언되는 경우 정상 실행이
불가능합니다.

## 3. Mapper 인터페이스 작성

Mapper는 인터페이스이기 때문에 사용할 때 방법이 두 가지 있습니다.

(1) 빌드되어 있는 구현체를 직접 생성해서 사용하거나 (직접 생성 방식)

    @Mapper
    public interface CarMapper {
        // MapStruct가 생성한 구현체를 쉽게 가져오기 위한 인스턴스 선언
        CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);
    
        @Mapping(source = "name", target = "modelName")
        @Mapping(source = "color", target = "modelColor")
        CarDto toDto(Car car);
    }

컴파일 시 **MapStruct**가 위 **인터페이스를 기반으로 CarMapperImpl 클래스를 생성**하여 실제 매핑 로직을 구현합니다.

    @Service
    public class CarService {

        public CarDto convertToDTO(Car car) {
            // 직접 생성된 구현체 인스턴스를 사용
            return CarMapper.INSTANCE.toDto(car);
        }

    }

직접 생성된 구현체 인스턴스를 들고와 사용합니다.

(2) Spring에서 주입 받는 형태입니다. (Spring DI 방식)

    @Mapper(componentModel = "spring")
    public interface CarMapper {
        @Mapping(source = "name", target = "modelName")
        @Mapping(source = "color", target = "modelColor")
        Car toCar(CarDto carDto);
    }

@Mapper(componentModel = "spring")을 사용하면, MapStruct가 생성한 Mapper 구현체가 스프링 빈으로 등록됩니다.

    @Service
    @RequiredArgsConstructor    
    public class CarService {

        private final CarMapper carMapper; // 스프링이 주입해줍니다.
    
        public Car convertToCar(CarDto carDto) {
            // 주입받은 carMapper를 사용합니다.
            return carMapper.toCar(carDto);
        }

    }

따라서 의존성 주입(DI)을 통해 사용할 수 있습니다.

저의 경우에는 Mapper 클래스를 다루는 과정에서 Bean이 아니라는 오류가 떠(인스턴스 생성을 하지 않았고, spring 지정도 하지 않았을 때) 자연스레 후자의 방식을
채택하여 진행했습니다.

### 3-1. 매핑에 여러 객체가 필요할 때

- 파라미터로 특정 객체를 전달할 때
  Mapstruct 문서 3.4 라벨 참고

  @Mapper(componentModel = "spring")
  public interface CarMapper {
  @Mapping(source = "name", target = "modelName")
  @Mapping(source = "color", target = "modelColor")
  @Mapping(source = "ownerDto.name", target = "modelColor")
  Car toCar(OwnerDto owerDto, CarDto carDto);
  }

source 매핑값을 가지고 올 대상에 {객체 명}.{필드명}으로 지정해주면 된다.

- 만약 참조하고자 하는 클래스가 다른 **매퍼 클래스**라면,
  Mapstruct 문서 5.5 라벨 참고

      @Mapper(uses=DateMapper.class)
      public interface CarMapper {
          CarDto carToCarDto(Car car);
      }

@Mapper의 속성 uses를 이용한다.

- 만약 참고하고자 하는 것이 **필드에 선언된 또 다른 객체의 필드**라면
  Mapstruct 문서 5.3 라벨 참고

  @Mapper
  public interface FishTankMapper {

                  @Mapping(target = "fish.kind", source = "fish.type")
                  @Mapping(target = "fish.name", ignore = true)
                  @Mapping(target = "ornament", source = "interior.ornament")
                  @Mapping(target = "material.materialType", source = "material")
                  @Mapping(target = "quality.report.organisation.name", source = "quality.report.organisationName")
                  FishTankDto map( FishTank source );

  }

{객체를 참조한 필드명}.{해당 객체의 필드명} 형태로 사용해야한다.

### 3-2. 매퍼에 사용자 정의 메서드를 추가해야할 때

Mapstruct 문서 3.3라벨 참고

    @Mapper(componentModel = "spring")
    public interface CarMapper {
        @Mapping(...)
        ...
        CarDto carToCarDto(Car car);
  
        default PersonDto personToPersonDto(Person person) {
            //hand-written mapping logic
    }

Java8부터는 Interface에서 default를 통해서 기본 메서드로 사용자 정의 메서드를 만들 수 있다.

    @Mapper
    public abstract class CarMapper {

        // 추가 필드: 외부 의존성을 주입받아 사용할 수 있습니다.
        @Autowired
        protected EngineRepository engineRepository;
    
        @Mapping(...)
        ...
        public abstract CarDto carToCarDto(Car car);
    
        public PersonDto personToPersonDto(Person person) {
            //hand-written mapping logic
        }
    }

abstract을 통해서 구현한다면, 매퍼 클래스에 추가 필드를 선언할 수 있다는 이점이 존재한다.

### 사용자 정의 메서드 활용 @Named 어노테이션 - Mapping#qualitiedByName 연계

Mapstruct 문서 5.10라벨 참고

    @Mapper
    public interface MovieMapper {
    
           @Mapping( target = "category", qualifiedByName = "CategoryToString", defaultValue = "DEFAULT" )
           GermanRelease toGerman( OriginalRelease movies );
      
           @Named("CategoryToString")
           default String defaultValueForQualifier(Category cat) {
               // some mapping logic
           }
    }

Mapping#qualifiedByName -> Mapping#qualifiedByMapStruct가 해당 메서드를 사용하도록 강제한다.

[편리한 객체 간 매핑을 위한 MapStruct 적용기 (feat. SENS)
](https://medium.com/naver-cloud-platform/%EA%B8%B0%EC%88%A0-%EC%BB%A8%ED%85%90%EC%B8%A0-%EB%AC%B8%EC%9E%90-%EC%95%8C%EB%A6%BC-%EB%B0%9C%EC%86%A1-%EC%84%9C%EB%B9%84%EC%8A%A4-sens%EC%9D%98-mapstruct-%EC%A0%81%EC%9A%A9%EA%B8%B0-8fd2bc2bc33b)

[MapStruct](https://ordilov.github.io/posts/mapstruct)

[MapStruct Document](https://mapstruct.org/documentation/stable/reference/html/)

----
https://splin.tistory.com/12
MapStruct가 ModelMapper보다 속도가 빠르다는 글

----

## error

[Mapstruct는 제네릭 유형을 지원할 수 없습니다.
](https://stackoverflow.com/questions/72992591/mapstruct-cannot-support-generic-type)