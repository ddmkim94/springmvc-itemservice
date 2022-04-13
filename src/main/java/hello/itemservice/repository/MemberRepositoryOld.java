package hello.itemservice.repository;

import hello.itemservice.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // final 필드를 매개변수로 한 생성자를 생성
public class MemberRepositoryOld {

    private final EntityManager em;

    // 회원 등록
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 조회 (식별자)
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    // 모든 회원 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 회원 조회 (이름)
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
