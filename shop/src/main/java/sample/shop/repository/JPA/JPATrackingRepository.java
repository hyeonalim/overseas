package sample.shop.repository.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.shop.domain.Tracking;

public interface JPATrackingRepository extends JpaRepository<Tracking, Long> {

}
