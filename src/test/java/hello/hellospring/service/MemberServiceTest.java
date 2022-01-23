package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    MemberService memberService;
    MemoryMemberRepository repository;

    //repository를 각각 new로 생성하면 다른 repository가 되는 등 문제가 발생하기때문에
    //memberservice에 생성자로 repository를 추가해준다.
    //memberservice입장에서는 외부에서 repository를 넣어주는 것 ==> DI
    @BeforeEach
    public void beforeEach() {
        repository = new MemoryMemberRepository();
        memberService = new MemberService(repository);
    }

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    void join() {
        //given 테스트에서 주어지는 것들
        Member member1 = new Member();
        member1.setName("Spring");

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
    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}