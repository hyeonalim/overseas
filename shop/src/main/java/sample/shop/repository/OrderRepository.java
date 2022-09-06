package sample.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;
import sample.shop.domain.Order;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @Autowired
    public EntityManager em;

    public Long save(Order order) {
        em.persist(order);
        return order.getNo();
    }

    public List<Order> findMyProject(Member member) {
        return em.createQuery(
                "select o from Order o where o.project in (select p.no from Project p where p.member =: member)",
                Order.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Order> findMyOrder(Member member) {
        return em.createQuery("select o from Order o where o.member =: member", Order.class)
                .setParameter("member", member)
                .getResultList();
    }

    public Order findOne(Long no) {
        return em.find(Order.class, no);
    }

}
