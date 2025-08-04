## My Check List
- [X] 클래스의 이름 규칙을 지키자.
- [X] Factory Method 유무와 method 이름 규칙을 지키자.
- [ ] 주문의 결제가 처리되기 전 상품의 status를 통해 payment 가능 여부를 검증해야 한다.
- [ ] 주문의 결제가 처리된다면 stock을 차감하고 product의 status 변경 필요가 있는지 확인해야 한다.

### 🏷 Product / Brand 도메인

- ✅ Checklist
  - [x]  상품 정보 객체는 브랜드 정보, 좋아요 수를 포함한다.
  - [X]  상품의 정렬 조건(`latest`, `price_asc`, `likes_desc`) 을 고려한 조회 기능을 설계했다.
  - [X]  상품은 재고를 가지고 있고, 주문 시 차감할 수 있어야 한다. -> 결제 시 차감된다.
  - [X]  재고는 감소만 가능하며 음수 방지는 도메인 레벨에서 처리된다.

- 👍 Like 도메인
  - [X]  좋아요는 유저와 상품 간의 관계로 별도 도메인으로 분리했다
  - [X]  중복 좋아요 방지를 위한 멱등성 처리가 구현되었다
  - [X]  상품의 좋아요 수는 상품 상세/목록 조회에서 함께 제공된다
  - [X]  단위 테스트에서 좋아요 등록/취소/중복 방지 흐름을 검증했다

- 🛒 Order 도메인
  - [X]  주문은 여러 상품을 포함할 수 있으며, 각 상품의 수량을 명시한다.
  - [X]  주문 시 상품의 재고 차감, 유저 포인트 차감 등을 수행한다.
  - [X]  재고 부족, 포인트 부족 등 예외 흐름을 고려해 설계되었다.
  - [X]  단위 테스트에서 정상 주문 / 예외 주문 흐름을 모두 검증했다.

- 🧩 도메인 서비스
  - [ ]  도메인 간 협력 로직은 Domain Service에 위치시켰다. -> Application Service에 위치 시켰는데?
  - [ ]  상품 상세 조회 시 Product + Brand 정보 조합은 도메인 서비스에서 처리했다 -> Application Service에 위치 시켰는데?
  - [ ]  복합 유스케이스는 Application Layer에 존재하고, 도메인 로직은 위임되었다 -> 단순 Usecase가 Facade에 위치하는 것 같은데?
  - [ ]  도메인 서비스는 상태 없이, 도메인 객체의 협력 중심으로 설계되었다 -> 도메인 서비스가 Repository 가지고 있는데 상태를 가지고 있는게 아닐지?

- **🧱 소프트웨어 아키텍처 & 설계**
  - [X]  전체 프로젝트의 구성은 아래 아키텍처를 기반으로 구성되었다
      - Application → **Domain** ← Infrastructure
  - [X]  Application Layer는 도메인 객체를 조합해 흐름을 orchestration 했다
  - [ ]  핵심 비즈니스 로직은 Entity, VO, Domain Service 에 위치한다 -> 핵심 비지니스가 Facade에 있지는 않은가?
  - [X]  Repository Interface는 Domain Layer 에 정의되고, 구현체는 Infra에 위치한다
  - [X]  패키지는 계층 + 도메인 기준으로 구성되었다 (`/domain/order`, `/application/like` 등)
  - [ ]  테스트는 외부 의존성을 분리하고, Fake/Stub 등을 사용해 단위 테스트가 가능하게 구성되었다