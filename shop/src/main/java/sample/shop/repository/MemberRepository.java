package sample.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    @Autowired
    public EntityManager em;

    /**
     * 회원가입
     * 
     * @param member
     */
    public Long save(Member member) {
        em.persist(member);
        return member.getNo();
    }

    public Member findOne(Long no) {
        return em.find(Member.class, no);
    }

    public List<Member> findOneByNo(Long no) {
        return em.createQuery("select m from Member m where m.no =: no", Member.class)
                .setParameter("no", no)
                .getResultList();
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findById(String id) {
        return em.createQuery("select m from Member m where m.id =: id", Member.class)
                .setParameter("id", id)
                .getResultList();
    }

    public List<Member> findByMember(String id, String pwd) {
        return em.createQuery("select m from Member m where m.id =: id and m.pwd =: pwd", Member.class)
                .setParameter("id", id)
                .setParameter("pwd", pwd)
                .getResultList();
    }

    /*
     * // 회원 비밀번호 비교 (맞으면 풀고 아니면 닫고)
     * public List<Member> compareByPwd(String username, String pwd) {
     * return em.
     * createQuery("select m from Member m where m.username =: username and m.pwd =: pwd"
     * , Member.class)
     * .setParameter("username", username)
     * .setParameter("pwd", pwd)
     * .getResultList();
     * }
     */
}
