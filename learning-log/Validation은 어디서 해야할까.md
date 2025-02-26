## 검증은 어디서 해야할까?

스프린트 5 Message 생성 API에서 Channel 또는 User을 찾을 수 없을 때 404 에러 처리를 해야하는데, 문득 검증 로직은 어느 레이어 수준에서 작성되어야 할까
고민되어 찾아본 자료들입니다.

- [Validation 코드는 어디에 작성해야 할까? - The 3 types of validation logics](https://littlemobs.com/blog/three-types-of-validation-logics/)
  파이썬 기반의 설명

- [T.I.L #43 검증 로직 어디에?](https://velog.io/@qoxowkd0716/T.I.L-43-%EA%B2%80%EC%A6%9D-%EB%A1%9C%EC%A7%81-%EC%96%B4%EB%94%94%EC%97%90)

- [[Java] 사용자 입력의 검증과 분석은 어떤 계층에서 수행해야 할까](https://velog.io/@dgh06175/%EC%82%AC%EC%9A%A9%EC%9E%90-%EC%9E%85%EB%A0%A5%EC%9D%98-%EA%B2%80%EC%A6%9D%EA%B3%BC-%EB%B6%84%EC%84%9D%EC%9D%80-%EC%96%B4%EB%96%A4-%EA%B3%84%EC%B8%B5%EC%97%90%EC%84%9C-%EC%88%98%ED%96%89%ED%95%B4%EC%95%BC-%ED%95%A0%EA%B9%8C)

- [Validation(입력값 검증)의 최적의 장소는 어디일까?
  ](https://starkying.tistory.com/entry/Model-%EA%B0%9D%EC%B2%B4%EC%97%90%EC%84%9C%EB%A7%8C%ED%81%BC%EC%9D%80-Validation%EC%9D%84-%ED%95%84%EC%88%98%EB%A1%9C-%ED%95%98%EC%9E%90)

- [Validation 책임과 범위는 어떻게 가져가야할까?
  ](https://systemdata.tistory.com/82)

*Validation 의 목적에 따라서 위치가 달라진다.*
목적을 잘 고려하여 위치를 선정해야한다.

- [Update 할 때 조건검사(price >= 0) 는 어디서 하는게 좋은 방법일까요?](https://www.inflearn.com/community/questions/277568/update-%ED%95%A0-%EB%95%8C-%EC%A1%B0%EA%B1%B4%EA%B2%80%EC%82%AC-price-gt-0-%EB%8A%94-%EC%96%B4%EB%94%94%EC%84%9C-%ED%95%98%EB%8A%94%EA%B2%8C-%EC%A2%8B%EC%9D%80-%EB%B0%A9%EB%B2%95%EC%9D%BC%EA%B9%8C%EC%9A%94)

CRS 패턴을 채택하고 있다는 전제하에,
CRS 모든 레이어에서 검증하고자 하는 데이터에 대한 모든 확인을(+ 서버뿐만 아니라 클라이언트에서도 한 번 더 하는 것 또한)하는 것이 가장 안전하지만 현실적으로 어려운 부분이
있기에, 일반적으로 아래와 같은 기준을 가지고 검증을 시도한다고 한다.

- HTTP 요청 파라미터에 대한 건, 컨트롤러에서
- 내부 DB 조회, 외부 호출이 필요한 검증은, 서비스에서
- 해당 Entity 가 가지고 있는 데이터 만으로 모두 검증할 수 있다면, 엔티티에서

*Message 생성시* *Channel* 또는 *User*을 찾을 수 없을 때 검증해야 한다는 건, 내부 DB 조회를 거친 후 검증해야 하므로 서비스 레이어에서 검증했다.

