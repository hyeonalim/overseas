package sample.shop.repository.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.shop.domain.Member;

public interface JPAMemberRepository extends JpaRepository<Member, Long> {

}
