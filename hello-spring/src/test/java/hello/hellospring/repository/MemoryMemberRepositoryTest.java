package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
//테스트를 먼저 만들고 그 다음 구현하는 것을 테스트 주도 개발 (TTD)
class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();
    //테스트를 만들 때는 의존관계 없이 설계하자
    @AfterEach      //하나의 테스트가 끝날때마다 레포를 지워 해당 어노테이션은 하나의 메서드가 끝날 때 마다 실행하라는 뜻
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save(){
        Member member = new Member();
        member.setName("Dave");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);   //첫번째 인자와 두번째 인자가 같은지 테스트
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("James");
        member2.setName("MinJun");

        repository.save(member1);
        repository.save(member2);

        Member result = repository.findByName("James").get();
        assertThat(result).isEqualTo(member1);

    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        Member member2 = new Member();

        member1.setName("wonjin");
        member2.setName("minjun");

        repository.save(member1);
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2);

    }
}
