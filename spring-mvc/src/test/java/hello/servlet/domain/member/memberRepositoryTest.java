package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class memberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    public void save() {
        //given
        Member member = new Member("spring", 20);
        //when
        Member saveMember = memberRepository.save(member);
        //then
        Member findMember = memberRepository.findById(member.getId());
        assertThat(saveMember).isSameAs(findMember);
    }

    @Test
    public void findAll() {
        Member spring = new Member("spring", 20);
        Member kim = new Member("kim", 24);

        Member saveSpring = memberRepository.save(spring);
        Member saveKim = memberRepository.save(kim);

        List<Member> findAllMember = memberRepository.findAll();

        assertThat(findAllMember.size()).isEqualTo(2);
        assertThat(findAllMember).contains(spring, kim);
    }
}
