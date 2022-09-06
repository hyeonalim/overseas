package sample.shop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.TrackingForm;
import sample.shop.controller.data.UpdateTrackingRequest;
import sample.shop.domain.TrackingStatus;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;
import sample.shop.domain.Tracking;
import sample.shop.repository.TrackingRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrackingServiceImpl implements TrackingService {

    @Autowired
    private TrackingRepository trackingRepository;

    private final OrderService orderService;
    private final MemberService memberService;

    @Override
    @Transactional
    public Long save(TrackingForm request, Long memberNo, Long orderNo) {
        Tracking tracking = new Tracking();
        Order order = orderService.findOne(orderNo);

        tracking.setDate(request.getDate());
        tracking.setLocal(request.getLocal());
        tracking.setMemo(request.getMemo());
        tracking.setOrder(order);

        if (request.getStatus().equals("ORDER")) {
            tracking.setStatus(TrackingStatus.ORDER);
        } else if (request.getStatus().equals("RELEASE")) {
            tracking.setStatus(TrackingStatus.RELEASE);
        } else if (request.getStatus().equals("SHIPPINGAGENT")) {
            tracking.setStatus(TrackingStatus.SHIPPINGAGENT);
        } else if (request.getStatus().equals("DOMESTIC")) {
            tracking.setStatus(TrackingStatus.DOMESTIC);
        } else if (request.getStatus().equals("DELIVERY")) {
            tracking.setStatus(TrackingStatus.DELIVERY);
        }

        Member member = memberService.myInfo(memberNo);

        if (order.getProject().getMember() == member) {
            return trackingRepository.save(tracking);
        } else {
            throw new IllegalStateException("프로젝트를 만든 회원이 아닙니다.");
        }
    }

    @Override
    public List<Tracking> findOrder(Order order, Member member) {
        return trackingRepository.findOrder(order);
    }

    @Override
    @Transactional
    public Long update(Long no, Member member, UpdateTrackingRequest request) {
        Tracking tracking = trackingRepository.findOne(no);

        tracking.setDate(LocalDateTime.now());
        tracking.setLocal(request.getLocal());
        tracking.setMemo(request.getMemo());

        if (request.getStatus().equals("ORDER")) {
            tracking.setStatus(TrackingStatus.ORDER);
        } else if (request.getStatus().equals("RELEASE")) {
            tracking.setStatus(TrackingStatus.RELEASE);
        } else if (request.getStatus().equals("SHIPPINGAGENT")) {
            tracking.setStatus(TrackingStatus.SHIPPINGAGENT);
        } else if (request.getStatus().equals("DOMESTIC")) {
            tracking.setStatus(TrackingStatus.DOMESTIC);
        } else if (request.getStatus().equals("DELIVERY")) {
            tracking.setStatus(TrackingStatus.DELIVERY);
        }

        if (member == tracking.getOrder().getProject().getMember()) {
            return trackingRepository.save(tracking);
        } else {
            throw new IllegalStateException("프로젝트를 만든 회원이 아닙니다.");
        }
    }

    @Override
    public Tracking findOne(Long trackingNo) {
        return trackingRepository.findOne(trackingNo);
        /*
         * if (member == order.getProject().getMember()) {
         * List<Delivery> deliveries = findOrder(order, member);
         * Delivery delivery = null;
         * 
         * for (int set = 0; set < deliveries.size(); set++) {
         * if (status == deliveries.get(set).getStatus()) {
         * delivery = deliveries.get(set);
         * }
         * }
         * 
         * return trackingRepository.findOne(delivery.getNo());
         * } else {
         * throw new IllegalStateException("프로젝트를 만든 회원이 아닙니다.");
         * }
         */
    }

    @Override
    public void saveByGRAD(List<Order> orders, Project project) {
        Tracking tracking = new Tracking();

        for (int list = 0; list >= orders.size(); list++) {
            Order order = orderService.findOne(orders.get(list).getNo());

            tracking.setDate(LocalDateTime.now());
            tracking.setLocal("준비중");
            tracking.setMemo("배송 시작을 알립니다.");
            tracking.setOrder(order);

            tracking.setStatus(TrackingStatus.ORDER);

            Member member = orders.get(list).getMember();

            if (tracking.getOrder().getProject().getMember() == member) {
                trackingRepository.save(tracking);
            } else {
                throw new IllegalStateException("프로젝트를 만든 회원이 아닙니다.");
            }
        }
    }

}
