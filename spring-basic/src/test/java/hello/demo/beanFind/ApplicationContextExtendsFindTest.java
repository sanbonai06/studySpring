package hello.demo.beanFind;

import hello.demo.discount.DiscountPolicy;
import hello.demo.discount.FixDiscountPolicy;
import hello.demo.discount.RateDiscountPolicy;
import hello.demo.member.MemberService;
import hello.demo.member.MemberServiceImpl;
import hello.demo.order.OrderService;
import hello.demo.order.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(testConfig.class);

    @Test
    @DisplayName("부모 타입으로 조회 시 자식이 2개 이상 있으면 중복 오류 발생")
    void findBeanByParentTypeDuplicate() {
        //DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }

    @Test
    @DisplayName("중복 오류가 발생했으므로 빈 이름을 지정해주자")
    void findBeanByName() {
        //DiscountPolicy bean = ac.getBean(DiscountPolicy.class);
        DiscountPolicy bean1 = ac.getBean("rateDiscountPolicy",DiscountPolicy.class);
        DiscountPolicy bean2 = ac.getBean("fixDiscountPolicy", DiscountPolicy.class);
        assertThat(bean1).isInstanceOf(DiscountPolicy.class);
        assertThat(bean2).isInstanceOf(DiscountPolicy.class);

    }

    @Test
    @DisplayName("특정 하위 타입으로 조회")
    void findBeanBySubType() {
        RateDiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
        assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
    }

    @Test
    @DisplayName("부모 타입으로 전부 조회")
    void findBeanByParenType() {
        Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
        for (String key : beansOfType.keySet()) {
            assertThat(beansOfType.get(key)).isInstanceOf(DiscountPolicy.class);
        }
    }

    @Test
    @DisplayName("최상위인 Object타입으로 조회")
    void findBeanByObject() {
        Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
       // assertThat(beansOfType.size()).isEqualTo(2);
        for (String key : beansOfType.keySet()) {
            System.out.println("key = " + key + " values = " + beansOfType.get(key));
        }
    }
    @Configuration
    static class testConfig {

        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }


    }
}
