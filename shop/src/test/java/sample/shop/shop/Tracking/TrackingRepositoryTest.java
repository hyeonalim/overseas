package sample.shop.shop.Tracking;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import sample.shop.controller.data.TrackingForm;
import sample.shop.controller.data.UpdateTrackingRequest;
import sample.shop.domain.TrackingStatus;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Tracking;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;
import sample.shop.service.TrackingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackingRepositoryTest {

    @Autowired
    TrackingService trackingService;

    @Autowired
    MemberService memberService;

    @Autowired
    OrderService orderService;

    @Autowired
    EntityManager em;

    // 배송 생성
    @Test
    @Transactional
    @Rollback(false)
    public void test배송생성() {
        Long no = 2L;
        Long orderNo = 5L;

        TrackingForm delivery = new TrackingForm();

        delivery.setLocal("인천");
        delivery.setDate(LocalDateTime.now());
        delivery.setMemo("memo");
        delivery.setStatus("ORDER");

        trackingService.save(delivery, no, orderNo);
    }

    // 배송 조회
    @Test
    @Transactional
    @Rollback(true)
    public void test배송조회() {
        Member member = memberService.myInfo(2L);
        Order order = orderService.findOne(7L);

        trackingService.findOrder(order, member);
    }

    // 배송 수정
    @Test
    @Transactional
    @Rollback(false)
    public void test배송수정() {
        Long no = 2L;

        Member member = memberService.myInfo(no);
        Order order = orderService.findOne(7L);
        TrackingStatus status = TrackingStatus.ORDER;
        Tracking tracking = trackingService.findOne(197L);

        UpdateTrackingRequest request = new UpdateTrackingRequest();

        request.setMemo("수정");
        request.setStatus("DELIVERY");

        trackingService.update(tracking.getNo(), member, request);
    }

}
