package sample.shop.service;

import java.util.List;

import sample.shop.domain.Cash;
import sample.shop.domain.CashList;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.Project;

public interface CashService {

    Long create(Member member);

    void cashUpdateByOrder(Project project);

    void updateProjectBuy(Project project, Member member);

    void update(Long no, int money, Member member);

    void updateAdd(Long no, int money, Member member);

    void updateOut(Long no, int money, Member member);

    Cash findByNo(Long no);

    CashList findByCashListNo(Long no);

    Cash findByMember(Member member);

    List<CashList> findByMemberList(Member member);

    List<CashList> findByLoading(Cash cash);

    Long saveList(int money, Cash cash, Order order);

}
