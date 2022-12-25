## Repository test

***

__```JpaRepository```__ 를 상속하는 __```MembershipRepository```__ 인터페이스를 통해 
DB에 접근하므로 **@DataJpaTest** 어노테이션을 이용해서 테스트 진행

(**@SpringBootTest**를 이용하게 되면 너무 무겁고, mocking을 하면 실제 db와 다른 환경)

### DataJpaTest
- JPA를 이용하는데 필요한 빈들만 가져온다.
- 테스트가 끝나면 데이터베이스를 RollBack한다.
- 기본적으로 인메모리 DB 사용 (H2)

@Autowired를 통해 repository를 주입하는데 다음과 같이 코드를 작성하면 문제가 된다.

```
@Autowired
val membershipRepository = MembershipRepository()
```

MembershipRepoistory는 인터페이스로 생성자가 없어 위와 같이 작성하면 컴파일 오류가 발생한다.

***Interface MembershipRepository does not have constructors***

다음과 같이 **lateinit** 키워드을 이용해서 코드를 작성하도록 한다.

```
@Autowired
lateinit var membershipRepository: MembershipRepository
```

### lateinit

초기화 지연 프로퍼라고 한다. (프로퍼티의 초기화를 나중에 하기 위한 키워드)

다음과 같은 제약사항 존재
- var(mutable) 프로퍼티만 사용 가능
- non-null 프로퍼티만 사용 가능
- getter / setter가 없는 프로퍼티만 사용 가능
- primitive type 프로퍼티는 사용 불가
- 클래스 생성자에서 사용 불가
- 로컬 변수로 사용 불가

```
class LateInitExample {
    private var name: String // (x)
}
```

코틀린은 nullable, non-nullable에 대한 검사가 엄격하기 때문에 위와 같이 non-nullable한 필드는 선언과 동시에 초기화가 되어야 한다.

위와 같은 상황일 때, null이 아님이 확실하지만 초기화를 미룰 때, lateinit을 사용한다.