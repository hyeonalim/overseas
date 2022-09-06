package sample.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Order;
import sample.shop.domain.Tracking;

@Repository
@RequiredArgsConstructor
public class TrackingRepository {

    @Autowired
    private EntityManager em;

    public Long save(Tracking tracking) {
        em.persist(tracking);
        return tracking.getNo();
    }

    public List<Tracking> findOrder(Order order) {
        return em.createQuery("select d from Delivery d where d.order =: order", Tracking.class)
                .setParameter("order", order)
                .getResultList();
    }

    public Tracking findOne(Long no) {
        return em.find(Tracking.class, no);
    }

}
