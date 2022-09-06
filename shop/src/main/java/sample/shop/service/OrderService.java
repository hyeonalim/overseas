package sample.shop.service;

import java.util.List;

import sample.shop.controller.data.AccountInfoForm;
import sample.shop.controller.data.OrderForm;
import sample.shop.controller.data.UpdateOrderAccountRequest;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;

public interface OrderService {

    // sale이 아닌 값 생성
    Long saveWithoutSale(Member nowLoginMember, Project project);

    // 생성
    Long save(OrderForm orderInfo, AccountInfoForm accountInfo, Member nowLoginMember, Project project);

    // 내 프로젝트의 주문서
    List<Order> findMyProject(Member member);

    // 주문한 프로젝트의 주문서들
    List<Order> findMyOrder(Member member);

    // 주문서 하나만 노출
    Order findOne(Long no);

    // 주문서 업데이트
    void update(Long no, UpdateOrderAccountRequest request);

    // 주문서 삭제
    void delete(Long no, Member nowLoginMember);

    List<Order> updateByProject(Project project);

    void deleteByProject(List<Order> orders, Project project);

}
