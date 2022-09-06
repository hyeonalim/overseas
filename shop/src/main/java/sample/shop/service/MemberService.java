package sample.shop.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sample.shop.controller.data.MemberForm;
import sample.shop.controller.data.MemberFormLogin;
import sample.shop.controller.data.UpdateMemberRequest;
import sample.shop.domain.Member;

public interface MemberService {

    Long join(MemberForm request, String fileUrl);

    Long login(MemberFormLogin request, HttpServletResponse response);

    Long nowLoginInfo(HttpSession httpSession);

    void logout(HttpServletResponse response, HttpSession session);

    List<Member> infos();

    Member myInfo(Long no);

    void update(Member member, UpdateMemberRequest request, String fileUrl);

    void delete(Long no);

    List<Member> myInfoTest(Long memberNo);
}
