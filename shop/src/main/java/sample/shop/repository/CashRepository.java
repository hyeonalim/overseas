package sample.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Cash;
import sample.shop.domain.CashList;
import sample.shop.domain.Member;

@Repository
@RequiredArgsConstructor
public class CashRepository {

    @Autowired
    public EntityManager em;

    public Long save(Cash cash) {
        em.persist(cash);
        return cash.getNo();
    }

    public Long save(CashList cashList) {
        em.persist(cashList);
        return cashList.getNo();
    }

    public Cash findByNo(Long no) {
        return em.find(Cash.class, no);
    }

    public List<Cash> findByMember(Member member) {
        return em.createQuery("select c from Cash c where c.member =: member", Cash.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<CashList> findByMemberList(Member member) {
        return em.createQuery(
                "select cl from CashList cl where cl.cash in (select c.no from Cash c where c.member =: member)",
                CashList.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<CashList> findList() {
        return em.createQuery(
                "select cl from CashList cl", CashList.class)
                .getResultList();
    }

    public CashList findByCashListNo(Long no) {
        return em.find(CashList.class, no);
    }

}
