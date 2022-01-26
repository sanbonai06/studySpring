package hello.demo.member;

public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    //오로지 MemberRepository라는 인터페이스에만 의존하는 코드, 하지만 생성자를 통해 어떤 저장소를 쓸지를 받아옴 DI 구현
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
