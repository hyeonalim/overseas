package sample.shop.repository.JPA;

import org.springframework.data.jpa.repository.JpaRepository;

import sample.shop.domain.Project;

public interface JPAProjectRepository extends JpaRepository<Project, Long> {

}
