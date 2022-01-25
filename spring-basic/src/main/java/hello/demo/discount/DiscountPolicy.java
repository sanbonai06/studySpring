package hello.demo.discount;

import hello.demo.member.Member;

public interface DiscountPolicy {

    //할인대상금액을 리턴하자
    int discount(Member member, int price);

}
