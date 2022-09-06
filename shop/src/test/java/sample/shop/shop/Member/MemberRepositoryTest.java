package sample.shop.shop.Member;

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
import sample.shop.controller.data.MemberForm;
import sample.shop.controller.data.UpdateMemberRequest;
import sample.shop.domain.Member;
import sample.shop.service.MemberService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class MemberRepositoryTest {

    @Autowired
    MemberService memberService;

    @Autowired
    EntityManager em;

    // 생성
    @Test
    @Transactional
    @Rollback(false)
    public void test회원생성() {
        String id = "test2";
        String pwd = "Qwer123!@#";
        String checkPwd = "Qwer123!@#";
        String name = "테스트2";

        Member member = new Member();
        MemberForm request = new MemberForm();

        member.setId(id);
        member.setPwd(pwd);
        member.setName(name);

        request.setId(id);
        request.setPwd(pwd);
        request.setCheckPwd(checkPwd);

        // Long no = memberService.join(request, file);

        // log.info(no);
    }

    // 로그인
    /*
     * @Test
     * 
     * @Transactional
     * 
     * @Rollback(true)
     * public void test로그인(HttpServletResponse response) {
     * String id = "test";
     * String pwd = "Qwer123!@#";
     * 
     * memberServiceImpl.login(id, pwd, response);
     * }
     */

    @Test
    @Transactional
    @Rollback(false)
    public void test회원정보() {
        List<Member> members = memberService.infos();

        for (int cnt = 0; cnt < members.size(); cnt++) {
            log.info(members.get(0).getId());
            System.out.println("***************************");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test회원업뎃() {
        Long no = 1L;
        UpdateMemberRequest request = new UpdateMemberRequest();

        request.setName("테스터3");
        request.setPwd("Qwert123!@#");
        request.setCheckPwd("Qwert123!@#");

        // memberService.update(no, request);
        Member member = memberService.myInfo(no);

        log.info(member.getName(), member.getPwd());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test회원삭제() {
        Long no = 4L;

        memberService.delete(no);
    }
}
