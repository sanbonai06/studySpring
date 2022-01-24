package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional      //테스트케이스가 끝나면 자동으로 commit전에 roll back() 해줌
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository repository;

    @Test
    void join() {
        //given 테스트에서 주어지는 것들
        Member member1 = new Member();
        member1.setName("spring");

        //when  실제로 테스트 해야하는 것
        Long saveId = memberService.join(member1);
        //then
        Member findMember = memberService.findOne(saveId).get();
        org.assertj.core.api.Assertions.assertThat(member1.getName()).isEqualTo(findMember.getName());

    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setName("Spring");

        Member member2 = new Member();
        member2.setName("Spring");
        //when
        memberService.join(member1);
        //첫번째 인자 에러를 기대한다. 두번째 인자의 로직이 실행될 때,,
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

/*        try{
            memberService.join(member2);
            fail();
        } catch(IllegalStateException e) {
            org.assertj.core.api.Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원 이름입니다.");
        }
*/
        //then
    }
}