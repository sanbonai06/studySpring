package hello.demo.autowired;

import hello.demo.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean{

        //member는 스프링 빈이 관리하지 않으므로 스프링 빈에 관리되는 것이 없는 세터 -> 호출 자체가 안됨
        @Autowired(required = false)
        public void NoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }
        //@Nullable은 주입 할 값이 널이면 null을 주입함
        @Autowired
        public void NoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }
        //Optional<>은 주입할 값이 널이면 Optional.empty를 주입
        @Autowired
        public void NoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }

    }
}
