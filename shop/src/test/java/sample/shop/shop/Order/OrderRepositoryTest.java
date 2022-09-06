package sample.shop.shop.Order;

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
import sample.shop.controller.data.OrderForm;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.OrderStatus;
import sample.shop.service.MemberService;
import sample.shop.service.OrderService;
import sample.shop.service.ProjectService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log4j2
public class OrderRepositoryTest {

    @Autowired
    OrderService orderService;

    @Autowired
    MemberService memberService;

    @Autowired
    ProjectService projectService;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    public void test주문생성() {
        Long memberNo = 2L;
        Long projectNo = 3L;

        OrderForm order = new OrderForm();

        order.setMemo("테스트");

    }

    // 내 프로젝트의 주문서
    @Test
    @Transactional
    @Rollback(true)
    public void test내프로젝트주문서() {
        Long no = 2L;

        Member member = memberService.myInfo(no);

        List<Order> orders = orderService.findMyProject(member);

        for (int cnt = 0; cnt < orders.size(); cnt++) {
            log.info(orders.get(cnt).getNo());
            log.info("***************************");
        }
    }

    // 내가 주문한 주문서
    @Test
    @Transactional
    @Rollback(true)
    public void test내주문서() {
        Long no = 2L;

        Member member = memberService.myInfo(no);

        List<Order> orders = orderService.findMyOrder(member);

        for (int cnt = 0; cnt < orders.size(); cnt++) {
            log.info(orders.get(cnt).getProject().getNo());
            log.info("***************************");
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test내주문서1() {
        Order order = orderService.findOne(162L);

        log.info(order.getProject().getNo());
        log.info("***************************");
    }

    // 삭제
    @Test
    @Transactional
    @Rollback(false)
    public void test주문삭제() {
        Long memberNo = 2L;

        Member member = memberService.myInfo(memberNo);

        Long orderNo = 6L;
        Order order = orderService.findOne(orderNo);

        order.setOrderStatus(OrderStatus.CANCEL);

        if (order.getMember() == member) {
            // orderService.delete(orderNo);
        }

        log.info(OrderStatus.CANCEL);
        log.info("***************************");
        log.info(OrderStatus.ORDER);
        log.info("***************************");
    }
}
