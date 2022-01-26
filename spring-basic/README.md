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
