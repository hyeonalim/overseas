package sample.shop.controller.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.AccountInfoForm;
import sample.shop.controller.data.OrderAccountForm;
import sample.shop.controller.data.OrderForm;
import sample.shop.controller.data.UpdateOrderAccountRequest;
import sample.shop.controller.data.UpdateOrderRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.OrderStatus;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectOrderStatus;
import sample.shop.domain.ProjectStepStatus;
import sample.shop.service.CashService;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;
import sample.shop.service.ProjectService;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ProjectService projectService;
    private final CashService cashService;

    // 생성
    @PostMapping("/api/order/save/{projectId}")
    public Long save(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("projectId") Long projectNo, @RequestBody @Valid OrderAccountForm request) {
        Member member = memberService.myInfo(nowLoginMemberNo);
        Project project = projectService.surchOne(projectNo);

        OrderForm orderInfo = new OrderForm(request.getPhone(), request.getMemo(), request.getCity(),
                request.getStreet(), request.getZipcode());
        AccountInfoForm accountInfo = new AccountInfoForm(request.getRefundName(), request.getRefundBankName(),
                request.getRefundAccount(), request.getPurchaseDate(),
                request.getPurchaseBankName(), request.getPurchaseAccount());

        Long no = orderService.save(orderInfo, accountInfo, member, project);

        if (project.getOrderStatus().equals(ProjectOrderStatus.CASH)) {
            if (project.getStepStatus().equals(ProjectStepStatus.FIRST)) {
                cashService.saveList(project.getFirstPrice(), member.getCash(), orderService.findOne(no));
            } else if (project.getStepStatus().equals(ProjectStepStatus.SECOND)) {
                cashService.saveList(project.getSecondPrice(), member.getCash(), orderService.findOne(no));
            } else {
                cashService.saveList(project.getThirdPrice(), member.getCash(), orderService.findOne(no));
            }

            cashService.updateProjectBuy(project, member);
        }

        if (no != null) {
            return no;
        } else {
            throw new IllegalStateException("주문서 발행 중 이상이 생겼습니다.");
        }
    }

    // 조회
    @GetMapping("/api/order/myorder")
    public List<OrderByMyDto> orderByMy(@SessionAttribute("mSession") Long nowLoginMemberNo) {
        List<Order> orderByMy = orderService.findMyOrder(memberService.myInfo(nowLoginMemberNo));
        List<OrderByMyDto> collect = orderByMy.stream()
                .map(o -> new OrderByMyDto(o.getNo(), o.getOrderStatus(), o.getDate(), o.getPhotoUrl(),
                        o.getProject().getNo()))
                .collect(Collectors.toList());
        return collect;
    }

    // 내 프로젝트의 주문서
    @GetMapping("/api/order/myproject")
    public List<OrderByMyDto> orderByProject(@SessionAttribute("mSession") Long nowLoginMemberNo) {
        List<Order> orderByMy = orderService.findMyProject(memberService.myInfo(nowLoginMemberNo));
        List<OrderByMyDto> collect = orderByMy.stream()
                .map(o -> new OrderByMyDto(o.getNo(), o.getOrderStatus(), o.getDate(), o.getPhotoUrl(),
                        o.getProject().getNo()))
                .collect(Collectors.toList());
        return collect;
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class OrderByMyDto {
        private Long no;

        private OrderStatus orderStatus;
        private LocalDateTime date;
        private String photoUrl;
        private Long projectNo;
    }

    // 수정
    @PutMapping("/api/order/update/{no}")
    public UpdateOrderResponse updateOrderAccount(@PathVariable("no") Long orderNo,
            @RequestBody @Valid UpdateOrderAccountRequest request) {
        orderService.update(orderNo, request);

        return new UpdateOrderResponse(orderNo);
    }

    @Data
    @AllArgsConstructor
    static class UpdateOrderResponse {
        private Long no;
    }

    // 삭제
    @GetMapping("/api/order/edit/{no}")
    public void updateMemberV2(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @PathVariable("no") Long orderNo, @RequestBody @Valid UpdateOrderRequest request) {
        Member nowLoginMember = memberService.myInfo(nowLoginMemberNo);

        orderService.delete(orderNo, nowLoginMember);
    }

}