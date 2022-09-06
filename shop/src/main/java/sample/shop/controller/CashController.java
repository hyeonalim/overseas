/*
 *회원
 ㄴ캐쉬
   ㄴ캐쉬 사용 내역
=> 캐쉬 사용 내역으로 따로 테이블 생성
=> 캐쉬 등록/이체/프로젝트 구매 내역 노출

   ㄴ캐쉬 충전
=> 캐쉬 등록 요청
=> 캐쉬 등록을 위해 돈 보내온 계좌명 받기
=> 확인되면 캐쉬 올려주기 (관리자 사이트)

   ㄴ캐쉬 이체
=> 캐쉬에 남은 캐쉬 있는지 확인
=> 캐쉬 이체를 위해 환불할 계좌명/계좌번호
=> 확인되면 캐쉬 돌려주기 (관리자 사이트)
 */

package sample.shop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import sample.shop.service.MemberService;

@Controller
@RequiredArgsConstructor
public class CashController {

    private final MemberService memberService;

    // 캐쉬 사용 내역
    @RequestMapping("/cashMyList")
    public String myList(HttpSession httpSession, Model model) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        model.addAttribute("nowLoginMember", memberService.myInfo(nowLoginMemberNo));
        return "sample/cash/my-list";
    }

    @RequestMapping("/cashList")
    public String list(HttpSession httpSession, Model model) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        model.addAttribute("nowLoginMember", memberService.myInfo(nowLoginMemberNo));
        return "sample/cash/all-list";
    }

    @RequestMapping("/cashOrder")
    public String cashOrder(HttpSession httpSession, Model model) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        model.addAttribute("nowLoginMember", memberService.myInfo(nowLoginMemberNo));
        return "sample/cash/cash-order";
    }

}