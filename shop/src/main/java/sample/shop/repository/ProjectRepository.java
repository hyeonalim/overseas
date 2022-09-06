package sample.shop.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;
import sample.shop.domain.Project;

@Repository
@RequiredArgsConstructor
public class ProjectRepository {

    @Autowired
    public EntityManager em;

    // 생성
    public Long save(Project project) {
        em.persist(project);
        return project.getNo();
    }

    // 조회
    public List<Project> findAll() {
        return em.createQuery("select p from Project p", Project.class)
                .getResultList();
    }

    // 조회(하나만)
    public Project findOne(Long no) {
        return em.find(Project.class, no);
    }

    public List<Project> findByMember(Member member) {
        return em.createQuery("select p from Project p where p.member =: member", Project.class)
                .setParameter("member", member)
                .getResultList();
    }

    public List<Project> findByName(String name) {
        return em.createQuery("select p from Project p where p.name =: name", Project.class)
                .setParameter("name", name)
                .getResultList();
    }

    public List<Project> findOneById(Long no) {
        return em.createQuery("select p from Project p where p.no =: no", Project.class)
                .setParameter("no", no)
                .getResultList();
    }

    public List<Project> surchOrderStatus(String word) {
        return em.createQuery("select p from Project p where p.orderStatus =: word", Project.class)
                .setParameter("no", word)
                .getResultList();
    }

    public List<Project> surchCountry(String word) {
        return em.createQuery("select p from Project p where p.country =: word", Project.class)
                .setParameter("no", word)
                .getResultList();
    }
}
