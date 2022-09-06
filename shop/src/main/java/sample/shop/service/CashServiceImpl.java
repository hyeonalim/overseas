package sample.shop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.shop.domain.Cash;
import sample.shop.domain.CashList;
import sample.shop.domain.CashStatus;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.OrderStatus;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectStatus;
import sample.shop.domain.ProjectStepStatus;
import sample.shop.repository.CashRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CashServiceImpl implements CashService {

    private final CashRepository cashRepository;
    private final OrderService orderService;

    @Override
    @Transactional
    public Long create(Member member) {
        Cash cash = new Cash();

        cash.setDate(LocalDate.now());
        cash.setMoney(0);

        cashRepository.save(cash);

        return cash.getNo();
    }

    // 주문을 받았을때
    @Override
    public void cashUpdateByOrder(Project project) {

        Cash cash = new Cash();
        int money = 0;

        // HELP인 경우
        if (project.getStatus().equals(ProjectStatus.HELP)) {
            cash = project.getOrders().get(0).getCashList().getCash();
            money += project.getFirstPrice() + project.getSecondPrice() + project.getThirdPrice();
        } else {
            // 주문서가 캐쉬라면 프로젝트 회원의 캐쉬를 올리기
            cash = findByMember(project.getMember());

            List<Order> findOrders = orderService.findMyProject(project.getMember());
            List<Order> findCashes = new ArrayList<Order>();

            int count = 0;

            for (int list = 0; list > findOrders.size(); list++) {
                if (findOrders.get(list).getCashList().getCash() != null) {
                    findCashes.set(count++, findOrders.get(list));
                }
            }

            for (count = 0; count > findCashes.size(); count++) {
                money += findCashes.get(count).getProject().getFirstPrice()
                        + findCashes.get(count).getProject().getSecondPrice()
                        + findCashes.get(count).getProject().getThirdPrice();
            }

            money += cash.getMoney();
        }

        update(cash.getNo(), money, project.getMember());
    }

    // 주문을 한 경우
    @Override
    public void updateProjectBuy(Project project, Member member) {
        Cash cash = member.getCash();
        int money = 0;

        // HELP인 경우
        if (project.getStatus().equals(ProjectStatus.HELP)) {
            money = member.getCash().getMoney() - project.getFirstPrice();
        } else {
            if (project.getStepStatus().equals(ProjectStepStatus.FIRST)) {
                money = member.getCash().getMoney() - project.getFirstPrice();
            } else if (project.getStepStatus().equals(ProjectStepStatus.SECOND)) {
                money = member.getCash().getMoney() - project.getSecondPrice();
            } else {
                money = member.getCash().getMoney() - project.getThirdPrice();
            }
        }
        update(cash.getNo(), money, member);
    }

    @Override
    @Transactional
    public void updateAdd(Long no, int money, Member member) {
        update(no, money, member);

        CashList list = findByNo(no).getCashList();
        list.setDate(LocalDateTime.now());
        list.setStatus(CashStatus.ADD);
    }

    @Override
    @Transactional
    public void updateOut(Long no, int money, Member member) {
        update(no, money, member);

        CashList list = findByNo(no).getCashList();
        list.setDate(LocalDateTime.now());
        list.setStatus(CashStatus.OUT);
    }

    @Override
    @Transactional
    public void update(Long no, int money, Member member) {
        Cash cash = findByNo(no);

        cash.setDate(LocalDate.now());
        cash.setMoney(money);
        cash.setMember(member);
    }

    @Override
    public Cash findByNo(Long no) {
        return cashRepository.findByNo(no);
    }

    @Override
    public Cash findByMember(Member member) {
        return cashRepository.findByMember(member).get(0);
    }

    @Override
    public CashList findByCashListNo(Long no) {
        return cashRepository.findByCashListNo(no);
    }

    @Override
    public List<CashList> findByMemberList(Member member) {
        return cashRepository.findByMemberList(member);
    }

    @Override
    public List<CashList> findByLoading(Cash cash) {
        return cashRepository.findList();
    }

    @Override
    @Transactional
    public Long saveList(int money, Cash cash, Order order) {
        CashList cashList = new CashList();

        cashList.setCash(cash);
        cashList.setDate(LocalDateTime.now());

        if (order == null) {
            cashList.setStatus(CashStatus.Loading);
        } else if (order.getOrderStatus().equals(OrderStatus.ORDER)) {
            cashList.setStatus(CashStatus.OUT);
        } else if (order.getOrderStatus().equals(OrderStatus.CANCEL)
                && order.getOrderStatus().equals(OrderStatus.UPDATE)) {
            cashList.setStatus(CashStatus.ADD);
        }
        cashList.setMoney(money);
        cashList.setOrder(order);

        return cashRepository.save(cashList);
    }

}