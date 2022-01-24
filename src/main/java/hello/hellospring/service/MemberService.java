package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    /*
    회원가입
     */
    public Long join(Member member) {
        //회원가입 시 이름의 중복은 불가능으로 만들자
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /*
    전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();

    }

    /*
    회원 아이디로 조회
     */
    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())       //반환하는 값이 이미 optional로 감싸져 있으므로 가능한 코드
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원 이름입니다.");
                });
    }

}
