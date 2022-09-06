/*
 ㄴ주문내역
   ㄴ내가 주문한 내역
     ㄴ주문내역 조회
=> 주문 테이블에서 세션 회원정보의 주문 내역 조회

     ㄴ주문내역 수정
=> 세션 회원정보의 주문내역 가져와서 수정
=> 없는 값들은 기존 값들로 수정

ㄴ주문 (아직)
 ㄴ주문 등록
=> 내가 등록한 프로젝트에 대해서는 주문 등록 불가
=> 값 입력 안한 경우, 다음 페이지로 넘어갈 수 없도록
 */
package sample.shop.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectOrderStatus;
import sample.shop.domain.ProjectStatus;
import sample.shop.service.OrderService;
import sample.shop.service.ProjectService;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final ProjectService projectService;
    private final OrderService orderService;

    private final MemberController memberController;

    // 생성
    @RequestMapping("/checkout{projectNo}")
    public String orderSave(@PathVariable("projectNo") Long projectNo, HttpSession httpSession, Model model) {
        Project project = projectService.surchOne(projectNo);
        model.addAttribute("project", project);
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        if (project.getStatus().equals(ProjectStatus.SALE)) {
            if (project.getOrderStatus().equals(ProjectOrderStatus.ACCOUNT)) {
                return "sample/order/checkout-account";
            } else if (project.getOrderStatus().equals(ProjectOrderStatus.CASH)) {
                return "sample/order/checkout-cash";
            } else {
                return "찾는 페이지가 없습니다";
            }
        } else {
            orderService.saveWithoutSale(memberController.modelMember(httpSession), project).toString();

            return "sample/order/confirmation";
        }
    }

    @RequestMapping("/confirmation")
    public String confirmation(HttpSession httpSession, Model model) {
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/order/confirmation";
    }

    // 조회
    @RequestMapping("/orderByMy")
    public String orderMyList(HttpSession httpSession, Model model) {
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/order/order-mylist";
    }

    // 내 프로젝트의 주문서
    @RequestMapping("/orderByMyProject")
    public String orderMyProjectList(HttpSession httpSession, Model model) {
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        return "sample/order/order-myproject";
    }

    // 주문서 수정
    @RequestMapping("/orderUpdate{orderNo}")
    public String orderUpdateAccount(@PathVariable("orderNo") Long orderNo, HttpSession httpSession, Model model) {
        Order order = orderService.findOne(orderNo);
        model.addAttribute("order", order);
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        if (order.getProject().getOrderStatus().equals(ProjectOrderStatus.ACCOUNT)) {
            return "sample/order/checkout-update-account";
        } else if (order.getProject().getOrderStatus().equals(ProjectOrderStatus.CASH)) {
            return "sample/order/checkout-update-cash";
        } else {
            return "찾는 페이지가 없습니다";
        }
    }

    // 삭제
    @GetMapping("/api/order/cancel/{no}")
    public String deleteMember(@SessionAttribute("mSession") Member member,
            @PathVariable("no") Long orderNo, HttpSession httpSession, Model model) {
        model.addAttribute("nowLoginMember", memberController.modelMember(httpSession));

        orderService.delete(orderNo, member);

        return "redirect:/orderByMyProject";

    }
}