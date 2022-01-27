package hello.demo.singleton;

import hello.demo.AppConfig;
import hello.demo.member.Member;
import hello.demo.member.MemberRepository;
import hello.demo.member.MemberService;
import hello.demo.member.MemberServiceImpl;
import hello.demo.order.OrderService;
import hello.demo.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 DI컨테이너")
    public void pureContainer() {
        AppConfig appConfig = new AppConfig();
        //1. 호출할때 마다 객체를 새로 생성하는지 알아보자
        MemberService memberService1 = appConfig.memberService();
        MemberService memberService2 = appConfig.memberService();
        MemberService memberService3 = appConfig.memberService();
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        System.out.println("memberService3 = " + memberService3);

        assertThat(memberService1).isNotSameAs(memberService2);
        //고객이 100명이면 객체가 100개 생성되는거나 마찬가지
        //이를 해결하기 위해 고안된 디자인 패턴이 싱글톤 패턴이다.
    }

    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    public void singleTonUse() {
        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        //instance를 비교할때는 isSmaeAs를 쓴다.
        assertThat(singletonService1).isSameAs(singletonService2);
    }

    @Test
    @DisplayName("싱글톤 컨테이너 테스트")
    public void singletonContainerTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);
        assertThat(memberService1).isSameAs(memberService2);
    }


}
