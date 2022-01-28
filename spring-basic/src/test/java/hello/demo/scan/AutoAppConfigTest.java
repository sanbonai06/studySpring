package hello.demo.scan;

import hello.demo.AutoAppConfig;
import hello.demo.member.MemberService;
import hello.demo.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;

public class AutoAppConfigTest {

    @Test
    public void BasicScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService bean = ac.getBean(MemberService.class);
        MemberServiceImpl bean1 = ac.getBean(MemberServiceImpl.class);
        System.out.println("bean = " + bean);
        System.out.println("bean1 = " + bean1);

        assertThat(bean).isSameAs(bean1);
    }

    @Test
    public void AllScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        Object bean = ac.getBean(AutoAppConfig.class);
        System.out.println("bean = " + bean);

        assertThat(bean).isInstanceOf(AutoAppConfig.class);
    }
}
