package sample.shop.controller.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.MemberForm;
import sample.shop.controller.data.MemberFormLogin;
import sample.shop.controller.data.UpdateMemberRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.ProjectStatus;
import sample.shop.service.FileService;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final FileService fileService;

    // 회원가입
    @PostMapping("/api/member/join")
    public Long saveMember(@RequestBody @Valid MemberForm request) {
        Long memberNo = memberService.join(request, fileService.fileUrl);

        return memberNo;
    }

    // 로그인
    @PostMapping("/api/member/login")
    public void login(@RequestBody @Valid MemberFormLogin request, HttpServletResponse response,
            @SessionAttribute(name = "mSession", required = false) Long nowLoginMemberNo, HttpServletRequest req,
            RedirectAttributes rttr) {
        HttpSession session = req.getSession();

        if (session.getAttribute("mSession") == null) {
            Long mSession = memberService.login(request, response);

            if (mSession == null) {
                session.setAttribute("mSession", null);
                rttr.addFlashAttribute("msg", false);
            } else {
                session.setAttribute("mSession", mSession);
                session.setMaxInactiveInterval(1800);
            }
        }
    }

    // 회원 정보 보여주기
    @GetMapping("/api/member/infos")
    public List<MemberInfoDto> getMemberInfo() {
        List<Member> findMembers = memberService.infos();
        List<MemberInfoDto> collect = findMembers.stream()
                .map(m -> new MemberInfoDto(m.getNo(), m.getName(), m.getId(), m.getPhotoUrl(),
                        m.getAddress().getCity(),
                        m.getAddress().getStreet(), m.getAddress().getZipcode()))
                .collect(Collectors.toList());
        return collect;
    }

    // 선택한 회원 정보 보여주기
    @PostMapping("/api/member/info")
    public List<MemberInfoDto> getMemberInfo(@RequestBody @Valid MemberInfo request) {
        List<Member> findMembers = memberService.myInfoList(request.getNo());
        List<MemberInfoDto> collect = findMembers.stream()
                .map(m -> new MemberInfoDto(m.getNo(), m.getName(), m.getId(), m.getPhotoUrl(),
                        m.getAddress().getCity(),
                        m.getAddress().getStreet(), m.getAddress().getZipcode()))
                .collect(Collectors.toList());
        return collect;
    }

    // 내 회원 정보 보여주기
    @GetMapping("/api/member/myInfo")
    public List<MyMemberInfoDto> getMyMemberInfo(
            @SessionAttribute(name = "mSession", required = false) Long nowLoginMemberNo) {
        List<Member> myMemberInfo = memberService.myInfoList(nowLoginMemberNo);
        List<MyMemberInfoDto> myMember = myMemberInfo.stream()
                .map(m -> new MyMemberInfoDto(m.getNo(), m.getName(), m.getId(), m.getPhotoUrl(), m.getBudget(),
                        budgetOrder(m),
                        budgetOrderPer(m.getBudget(), budgetOrder(m)), m.getAddress().getCity(),
                        m.getAddress().getStreet(), m.getAddress().getZipcode()))
                .collect(Collectors.toList());
        return myMember;
    }

    @Data
    static class MemberInfo {
        private Long no;
    }

    public int budgetOrder(Member member) {
        List<Order> orders = orderService.findMyOrder(member);
        int budgetOrder = 0;

        if (orders.size() > 0) {
            for (int order = 0; order >= orders.size(); order++) {
                if (orders.get(order).getDate().getMonth() == LocalDateTime.now().getMonth()) {
                    if (!orders.get(order).getProject().getStatus().equals(ProjectStatus.HELP)) {
                        int price = orders.get(order).getProject().getFirstPrice()
                                + orders.get(order).getProject().getSecondPrice()
                                + orders.get(order).getProject().getThirdPrice();
                        budgetOrder += price;
                    }
                }
            }
        }

        return budgetOrder;
    }

    public double budgetOrderPer(int budget, int budgetOrder) {
        double budgetOrderPer = 0;

        if (budget != 0 && budgetOrder != 0) {
            budgetOrderPer = (budgetOrder / budget) * 100;
        }

        return budgetOrderPer;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class MemberInfoDto {
        private Long no;
        private String name;
        private String id;
        private String photoUrl;

        private String city;
        private String street;
        private String zipcode;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class MyMemberInfoDto {
        private Long no;
        private String name;
        private String id;
        private String photoUrl;
        private int budget;
        private int budgetOrder;
        private double budgetOrderPer;

        private String city;
        private String street;
        private String zipcode;
    }

    // 프로필 수정
    @PutMapping("/api/member/edit")
    public UpdateMemberResponse updateMember(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @RequestBody @Valid UpdateMemberRequest request) {
        Member myMember = memberService.myInfo(nowLoginMemberNo);
        memberService.update(myMember, request, fileService.fileUrl);
        return new UpdateMemberResponse(myMember.getNo(), request.getName(), request.getId());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long no;
        private String name;
        private String id;
    }

    // 회원 탈퇴
    @DeleteMapping("/api/member/remove")
    public void deleteMember(@SessionAttribute(name = "mSession") Long nowLoginMemberNo) {
        memberService.delete(memberService.myInfo(nowLoginMemberNo).getNo());
    }

}
