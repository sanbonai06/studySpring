package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {

    //jpql select m from Member m where m.name = ? 으로 자동으로 쿼리문을 만들어줌
    @Override
    Optional<Member> findByName(String name);
}
