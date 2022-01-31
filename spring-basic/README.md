# 스프링 개념 기본

## 순수 자바코드로 예제 만들기
* 순수한 자바코드로 (스프링 없이) 회원과 주문 예제를 만들었다
* 객체 지향 언어의 원칙인 OCP, DIP원칙을 준수하기 위해 인터페이스(역할 담당) 구현체 (로직 담당)을 구분해서 만들었다.
* AppConfig파일 없이 직접 인터페이스에 구현체를 연결하는 방식으로 작성
* 그러자 문제 발생 (할인 정책을 변경 시 클라이언트 코드를 수정해야 하는 문제 발생)
-> 위에서 말한 객체 지향 언어의 원칙 OCP(확장은 가능, 변경은 불가능)과 DIP(구현체에 의존하면 안된다)를 어긴 코드가 되어버렸다.
* OCP는 실제로 작동하는 클라이언트 코드를 수정함으로써, DIP는 인터페이스인 추상체에만 의존해야 하지만 구현체에도 의존함으로써 어긴 것이다.
* 따라서 AppConfig파일을 만들어 설정을 따로 관리하자 (기획 관리자의 역할)
* AppConfig파일에서 추상체에 구현체를 따로 생성하여 주입함으로써 OCP, DIP의 원칙을 지킬 수 있었다.
-> 실제 클라이언트 코드에서는 생성자로 추상체를 선언함
* 할인 정책 변경과 같은 서비스 정책 변경 시 AppConfig파일의 구현체 주입만 수정하면 되므로 훨씬 간편하고 의존성을 줄인 훌륭한 방식

## 스프링으로의 변경
* 순수 자바코드와 스프링의 차이를 알아보자
* AppConfig파일에 @Configuration 어노테이션으로 스프링 설정 파일로 등록
* @Bean 어노테이션으로 각각의 설정 메서드들을 스프링 컨테이너에 등록 
-> 메서드들은 등록될 때 메서드들의 이름으로 등록된다. (ac.getBean()함수에 이용)
* 실제 동작하는 App파일에 

```
	ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);	// AppConfig파일에 있는 @Bean어노테이션이 붙은 메서드들을 스프링 컨테이너에 등록
        MemberService memberService = ac.getBean("memberService", MemberService.class);		// 스프링 컨테이너에는 메서드의 이름으로 등록되므로 getBean함수로 가져온다.
        OrderService orderService = ac.getBean("orderService", OrderService.class);
```
위의 코드를 넣어준다.

## 싱글톤
* 싱글톤 패턴을 사용해야 하는 이유: 각 스레드마다의 사용자들이 클라이언트 서비스를 호출한다면 사용자의 수만큼 서비스 인스턴스들이 새롭게 생겨남
-> 자원낭비
* 즉, 싱글톤 패턴을 사용하자...
```
    //자기 자신을 static으로 가지고 있기 때문에 딱 한개만 가지고 있게 된다.
    private static final SingletonService instance = new SingletonService();

    //만들어진 싱글톤을 끌어오는 방법
    public static SingletonService getInstance() {
        return instance;
    }

    //프라이빗 생성자로 만들어서 외부에서 호출되는 걸 막음
    private SingletonService() {
    }
```
* 위 코드와 같이 싱글톤 패턴을 사용하면 문제점들이 발생 (코드자체가 길어짐, OCP, DIP위반 등등...)
* 하지만 스프링 컨테이너는 스프링 빈 등록시 자동으로 싱글톤으로 등록해준다. (위와 같은 문제들도 해결해줌)
* @Configuration, @Bean의 어노테이션으로 싱글톤 등록(싱글톤 컨테이너) -> 이미 만들어진 객체에서 인스턴스를 공유하기 때문에 자원을 효과적으로 관리하고 효율적으로 처리할 수 있게됨 
* 하지만 싱글톤 컨테이너를 사용할 때는 객체의 유지성(state)을  주의해야한다.
* 예를 들어 A 고객이 주문을 하고, 곧이어 B 고객이 주문을 했다고 가정하자. 뒤이어 A 고객이 주문 내역을 호출했을 때 만약 주문 서비스 객체가 stateful하다면, A 고객의 주문내역은 B 고객의 주문내역으로 바뀌어 있을 것이다. -> 즉, 스프링 빈은 항상
무상태 (stateless)상태로 설계해야한다.
* 실제 AppConfig 코드를 보면 memoryRepository()를 여러번 호출 하는 것을 볼 수 있다.
```
@Configuration
public class AppConfig {

    //리팩토링 결과 각 서비스들의 역할들이 들어나고 각 역할마다 선택할 구현체로 리턴함
    @Bean
    public MemberService memberService() {
        System.out.println("AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("AppConfig.orderService");
        return new OrderServiceImpl(
                 memberRepository(),
                    discountPolicy()
        );
    }
}
```
하지만 memoryRepository를 까보면 모두 같은 인스턴스인 것을 확인 할 수 있다.
-> @Configuration 어노테이션이 붙은 AppConfig객체를 스프링 빈에 등록하면, 실제 AppConfig 객체가 등록되는 것이 아니라 CGLIB 라이브러리가 붙은 AppConfig 객체를 상속받은 객체가 등록된다. 이 라이브러리가 코드를 조작하여 
이미 스프링 컨테이너에 등록된 객체는 그 인스턴스를 재활용한다. 따라서 모든 memberRepository는 같은 인스턴스이다.


## 컴포넌트 스캔
* 지금까지는 설정파일을 만들어 수동으로 빈을 등록했다. 이번에는 자동 빈 등록을 알아보자.
```
@ComponentScan(
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
    Configuration.class))
```
위와 같이 설정파일에 @ComponentScan 어노테이션과 필터를 붙이면 해당하는 @Component 어노테이션이 붙은 객체들을 읽어온다.
* 또, 설정파일에서 의존관계주입도 없어졌기 때문에 @Autowired로 의존관계주입을 해주어야한다.
* 순서: @ComponentScan이 동작하면서, @Component 어노테이션이 붙은 모든 객체들을 스프링 빈에 등록한다. -> @Autowired 어노테이션이 붙은 코드들을 처리하며, 이미 스프링 빈에 등록된 객체를 찾아 의존관계주입을 실행
-> 기본 조회 전략은 타입이 같은 객체부터..
* @ComponentScan{ basePackages = "hello.demo" } => 탐색 시작 위치 패키지 지정
* 권장하는 방법: 따로 패키지 위치를 지정하지 않고 설정 파일 자체를 프로젝트 최상단에 두는 방법.

## 의존관계 주입
* 4가지 방법:

-생성자 주입: 생성자를 통해 바로 의존관계주입을 함, 생성자 호출 시점에 딱 1번 호출되므로 불편, 필수 의존관계에 적용 (생성자가 1개만 있으면 @Autowired가 필요 없음)

-수정자 주입(setter): 수정자 메서드(setter)를 통해 의존관계주입을 함, 선택, 변경 의존관계에 적용, 주입할 의존관계가 없으면 @Autowired(required=false)로 지정하면 됨.

-필드 주입

-일반 메서드 주입

* 주입 할 의존관계가 없을 때 옵션 처리를 해주면 동작 가능.

-@Autowired(required=false): 자동 주입할 대상이 없으면 메서드 자체가 호출 X

-@Nullable: 자동 주입할 대상이 없으면 NULL이 입력됨

-@Optional<>: 자동 주입할 대상이 없으면 Optional.empty가 입력됨

* 요즘 실무에서나 스프링 자체가 생성자 주입을 선호하는 추세이다. 

-대부분의 의존관계들은 애플리케이션 종료 전까지 변하면 안되므로 메서드를 public으로 열어두는 수정자 주입이나 다른 여러가지 방법들 보다는 생성자 주입을 이용하자.

-생성자 주입을 이용 시 final 키워드를 사용하게 되는데, final키워드는 값의 누락을 컴파일 오류로 막아준다.

* 롬복 라이브러리를 이용하여 @RequiredArgsConstructor 어노테이션을 이용하면 생성자를 따로 선언할 필요 없이 자동으로 빈등록을 해준다. (실제로는 생성자 코드를 자동으로 입력해 주는 어노테이션)
* 타입으로 빈 조회시 2개 이상이 조회되면 해결하는 방법

-@Autowired 필드명을 조회하고 싶은 빈 이름으로 설정해줌 
```
@Autowired
private DiscountPolicy rateDiscountPolicy
```

-@Qualifier("mainDiscountPolicy")

-@Primary를 붙인 객체가 먼저 조회됨

* 빈으로 다형성을 활용하는 코드를 다룰 때, List, Map을 이용하자.
		
## 빈 생명주기 콜백
* 프로젝트를 진행 하다보면 DB를 연결하거나 끊는다던지 네트워크 소켓을 열어놓아야 하는 등의 상황들이 발생 할 수 있다. 이러한 상황들은 스프링 빈이 등록된 직후나 스프링 빈 컨테이너가 종료되기 직전에 행해지는 경우가 많다.
* 이러한 경우에는 어떻게 코드를 짜야하는지 알아보자.
* 첫번째 방법: 인터페이스로 InitializingBean, DisposableBean 등록하기

-> 위 방법은 빈으로 등록하려는 객체에 직접 implements로 인터페이스를 상속 받는 것이다. 두 인터페이스를 상속받으면 두 개의 메서드가 생성되는데 각각의 메서드에 실행하고자 하는 메서드들을 넣어주면 된다.

단점: 스프링에 매우 의존적, 생성되는 메서드의 이름을 변경 불가, 외부 라이브러리에는 적용 불가

* 두번째 방법: 수동으로 빈 등록 시(설정 파일에서 빈 등록) 
```
@Bean(initMethod = "", destroyMethod="")
```

-> 위 방법은 @Configuration을 이용한 자동 빈 등록 방법이 아닌 수동으로 빈 등록 시 사용 가능한 방법이다. initMethod와 destoryMethod에 각각 실행하고자 하는 메서드들을 넣어주면 실행된다.
참고로 destroyMethod는 추론을 디폴트 값으로 갖기 때문에, 실행하고자 하는 메서드명이 close, shutdown 등 이면 굳이 등록을 안해줘도 자동으로 실행된다.

단점: 어쩔수 없이 필연적으로 수동 빈 등록 방법을 사용해야 한다. (자동 빈 등록 방법 사용 시 불가능)

* 세번째 방법: @PostConstruct, @PreDestory 사용

-> 위 방법은 자동 빈 등록 방법 사용 시 별도의 메서드 생성 없이 @PostConstruct, @PreDestory 어노테이션을 실행하고자 하는 각각의 메서드에 붙이면 된다.
자바 표준 사용이기 때문에 스프링이 아닌 별도의 컨테이너에서도 사용 가능 

단점: 외부라이브러리는 수정이 불가능 하므로 어노테이션을 붙일 수 없다. -> 외부라이브러리에는 사용이 불가능 하다. -> 외부라이브러리 사용 시에는 수동으로 빈을 등록 한 후 두번째 방법을 사용하도록 하자
