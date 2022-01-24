package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;     //spring boot가 자동으로 만들어줌, DB와의 통신과 같은 정형화된 코드들을 다 들고 있어서 자동으로 쿼리문 생성

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    //pk를 쓰는게 아닌 쿼리문들은 jpa 쿼리문을 따로 작성해줘야함
    //JPA를 쓰려면 반드시 transaction처리가 필요함 ==> Service계층에 Transaction처리 해주면 됨
    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                                    .setParameter("name", name)
                                    .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class).getResultList();
        return result;
    }
}
