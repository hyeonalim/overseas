/**
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
 */
package sample.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Order;
import sample.shop.domain.Tracking;
import sample.shop.service.OrderService;
import sample.shop.service.TrackingService;

@Controller
@RequiredArgsConstructor
public class TrackingController {

    private final OrderService orderService;
    private final TrackingService trackingService;

    // 배송 생성
    @RequestMapping("/trackingSave{orderNo}")
    public String trackingSave(@PathVariable("orderNo") Long orderNo, Model model) {
        Order order = orderService.findOne(orderNo);
        model.addAttribute("order", order);

        return "sample/tracking/tracking-save";
    }

    // 배송 조회
    @RequestMapping("/tracking{orderNo}")
    public String tracking(@PathVariable("orderNo") Long orderNo, Model model) {
        Order order = orderService.findOne(orderNo);
        model.addAttribute("order", order);

        return "sample/tracking/tracking-list";
    }

    // 배송 수정
    @RequestMapping("/trackingUpdate{trackingNo}")
    public String trackingUpdate(@PathVariable("trackingNo") Long trackingNo, Model model) {
        Tracking tracking = trackingService.findOne(trackingNo);
        model.addAttribute("tracking", tracking);
        return "sample/tracking/tracking-update";
    }

}
