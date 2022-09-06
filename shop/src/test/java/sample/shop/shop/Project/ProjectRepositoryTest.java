package sample.shop.shop.Project;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import sample.shop.controller.data.ProjectForm;
import sample.shop.domain.Member;
import sample.shop.domain.Project;
import sample.shop.service.MemberService;
import sample.shop.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class ProjectRepositoryTest {

    @Autowired
    ProjectService projectService;

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    // 생성
    @Test
    @Transactional
    @Rollback(false)
    public void test생성() {
        Long no = 2L;

        Member myMember = memberService.myInfo(no);

        if (myMember != null) {
            String name = "project2";
            int price = 1000;
            int quantity = 5;
            String exp = "테스트입니다2";

            ProjectForm project = new ProjectForm();

            project.setName(name);
            // project.setPrice(price);
            project.setQuantity(quantity);
            project.setExp(exp);
            project.setMember(myMember);

            // Long projectNo = projectService.upload(project, no);

            // log.info(projectNo);
        }

    }

    // 조회
    @Test
    @Rollback(false)
    public void test조회() {
        List<Project> projects = projectService.surchAll();

        for (int cnt = 0; cnt < projects.size(); cnt++) {
            log.info(projects.get(cnt).getName());
        }
    }

    // 상세 조회
    @Test
    @Rollback(false)
    public void test상세조회() {
        Long no = 5L;

        Project project = projectService.surchOne(no);

        log.info(project.getName());
    }

    // 프로젝트조회(회원)
    @Test
    @Rollback(false)
    public void test조회_회원() {
        Long no = 2L;

        Member member = memberService.myInfo(no);
        List<Project> projects = projectService.surchMember(member);

        for (int cnt = 0; cnt < projects.size(); cnt++) {
            log.info(projects.get(cnt).getName());
            System.out.println("*****************");
        }
    }

    // 프로젝트조회(My)

    // 프로젝트 삭제
    @Test
    @Transactional
    @Rollback(false)
    public void test삭제() {
        Long memberNo = 2L;

        Member myMember = memberService.myInfo(memberNo);

        Long projectNo = 5L;

        projectService.delete(projectNo, myMember);

    }

    // 프로젝트 업데이트
    @Test
    @Transactional
    @Rollback(false)
    public void test업뎃() {
        Long memberNo = 2L;

        Member myMember = memberService.myInfo(memberNo);

        Long projectNo = 6L;

        Project project = projectService.surchOne(projectNo);

        if (project.getMember().getNo() == myMember.getNo()) {
            project.setName("projects");
            project.setExp("수정테스트");
        }

    }

    // 프로젝트 검색

}
