package hello.test.miniproject.repository;

import hello.test.miniproject.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository {

    Member memberSave(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByName(String name);

    List<Member> findAll();

    void deleteMember(Long id);

    void clearMember();
}
