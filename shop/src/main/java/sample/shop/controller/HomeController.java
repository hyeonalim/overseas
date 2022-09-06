/*
 * 홈
회원
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

 ㄴ등록한 상품 조회
=> 세션으로 회원 정보를 통해 등록한 상품 조회

 ㄴ주문내역
   ㄴ내가 주문한 내역
     ㄴ주문내역 조회
=> 주문 테이블에서 세션 회원정보의 주문 내역 조회

     ㄴ주문내역 수정
=> 세션 회원정보의 주문내역 가져와서 수정
=> 없는 값들은 기존 값들로 수정

     ㄴ"배송"
      ㄴ배송 조회
=> 배송 테이블에서 주문내역의 배송 정보 조회

   ㄴ내 프로젝트에 주문한 내역
     ㄴ주문내역 조회
     ㄴ"배송"
      ㄴ배송 등록
=> 내가 등록한 프로젝트의 배송 등록하기
=> 한 고객만/모든 고객만 선택 가능하도록 (json 형태로 blooen 값 보내주기)

      ㄴ배송 수정/삭제
=> 내가 등록한 프로젝트의 배송 수정하기
=> 한 고객만/모든 고객만 선택 가능하도록 (json 형태로)
=> 삭제는 가운데 줄 그어서 노출시키기 (=> 실질적인 삭제 X)

     ㄴ공지
      ㄴ공지등록
=> 내가 등록한 프로젝트의 공지 등록하기 (모든 고객에게)

      ㄴ공지 수정/삭제
=> 내가 등록한 프로젝트의 공지 수정
=> 빈 값은 기존 값으로 저장할 것

프로젝트
ㄴ등록
 ㄴ판매자 등록
=> 값 선택 다양하게 가능하도록

 ㄴ구매자 등록
=> HELP 만 가능하도록

ㄴ조회
 ㄴ일반 조회
 ㄴ상세 조회
 ㄴ구매방식 조회
 ㄴ태그 조회
 ㄴ회원 조회
=> 드롭박스로 조회할 것 선택 후 검색어 입력

ㄴ수정/삭제
=> 판매자 <=> 구매자 서로 변경 불가
=> 삭제 시, 모든 캐쉬 취소나 환불 확인한 뒤 삭제 가능하도록 (잠시 값은 close로 변경됨)
=> 없는 값은 기존 값으로 변경

ㄴ주문
 ㄴ주문 등록
=> 내가 등록한 프로젝트에 대해서는 주문 등록 불가
=> 값 입력 안한 경우, 다음 페이지로 넘어갈 수 없도록
 */
package sample.shop.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Grade;
import sample.shop.domain.Member;
import sample.shop.service.MemberService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String home(HttpSession httpSession, Model model) {
        Long nowLoginMemberNo = memberService.nowLoginInfo(httpSession);
        Member nowLoginMember = null;

        if (nowLoginMemberNo != 0L) {
            nowLoginMember = memberService.myInfo(nowLoginMemberNo);
        }

        model.addAttribute("nowLoginMember", nowLoginMember);

        if (nowLoginMember != null) {
            if (nowLoginMember.getGrade().equals(Grade.MANAGER)) {
                return "indexMGT";
            }
        }
        return "index";
    }

    @RequestMapping("/apiTest")
    public String apiTest(@SessionAttribute("mSession") Member member, Model model,
            HttpServletResponse response) {

        Member myMember = null;

        if (member != null) {
            try {
                myMember = memberService.myInfo(member.getNo());
            } catch (Exception e) {
                member = null;
            }
        }

        model.addAttribute("member", myMember);
        return "home";
    }

}