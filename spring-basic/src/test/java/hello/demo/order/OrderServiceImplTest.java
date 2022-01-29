package hello.demo.order;

import hello.demo.discount.FixDiscountPolicy;
import hello.demo.member.Grade;
import hello.demo.member.Member;
import hello.demo.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    @Test
    void createOrder() {
        Member member = new Member(1L, "peter", Grade.VIP);
        MemoryMemberRepository memoryMemberRepository = new MemoryMemberRepository();
        memoryMemberRepository.save(member);
        FixDiscountPolicy fixDiscountPolicy = new FixDiscountPolicy();
        fixDiscountPolicy.discount(member, 10000);
        OrderServiceImpl orderService = new OrderServiceImpl(memoryMemberRepository,fixDiscountPolicy);
        Order order = orderService.createOrder(1L, "itemA", 10000);
        assertThat(order.calculatePrice()).isEqualTo(9000);
    }

}