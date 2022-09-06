package sample.shop.service;

import java.util.List;

import sample.shop.controller.data.TrackingForm;
import sample.shop.controller.data.UpdateTrackingRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;
import sample.shop.domain.Tracking;

public interface TrackingService {

    // 생성
    Long save(TrackingForm request, Long memberNo, Long orderNo);

    // 조회
    List<Tracking> findOrder(Order order, Member member);

    // 수정
    Long update(Long no, Member member, UpdateTrackingRequest request);

    Tracking findOne(Long trackingNo);

    void saveByGRAD(List<Order> orders, Project project);
}
