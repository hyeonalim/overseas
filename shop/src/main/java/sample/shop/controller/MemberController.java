/*
 *회원
ㄴ가입
=> 회원 아이디가 테이블에 없는지 검색
=> 비밀번호 와 비밀번호 확인 동일 확인
=> 전화번호 맞는지 검색

ㄴ로그인
=> 회원테이블에 해당 회원 정보가 있는지 확인
=> 세션으로 값 저장

ㄴ탈퇴
=> 회원 테이블에 해당 회원 정보 확인하여 삭제

ㄴ내정보
 ㄴ회원정보
=> 입력하지 않은 정보는 기존 입력 정보로 바꾸기
=> 비밀번호 동일 확인
=> 이 다음 회원 정보 변경되어야 함

 ㄴ주소
=> 회원 테이블에 저장한 주소 정보 가져오기
=> 메인 주소 등록 가능하도록
 */
package sample.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;
import sample.shop.service.MemberService;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    public Member modelMember(HttpSession httpSession) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        Member member = null;

        if (nowLoginMemberNo != 0L) {
            member = memberService.myInfo(nowLoginMemberNo);
        }

        return member;
    }

    @RequestMapping("/login")
    public String login() {
        return "sample/member/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "redirect:/";
    }

    @RequestMapping("/signin")
    public String signIn() {
        return "sample/member/signin";
    }

    @RequestMapping("/profile")
    public String profile(HttpSession httpSession, Model model) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        model.addAttribute("nowLoginMember", memberService.myInfo(nowLoginMemberNo));

        return "sample/member/profile-details";
    }

    @RequestMapping("/profile-edit")
    public String edit() {
        return "sample/member/edit";
    }

}