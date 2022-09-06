package sample.shop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sample.shop.controller.data.AccountInfoForm;
import sample.shop.controller.data.OrderForm;
import sample.shop.controller.data.UpdateOrderAccountRequest;
import sample.shop.domain.Account;
import sample.shop.domain.Address;
import sample.shop.domain.Cash;
import sample.shop.domain.Member;
import sample.shop.domain.Order;
import sample.shop.domain.OrderStatus;
import sample.shop.domain.Project;
import sample.shop.domain.ProjectOrderStatus;
import sample.shop.domain.ProjectStatus;
import sample.shop.domain.ProjectStepStatus;
import sample.shop.domain.Purchase;
import sample.shop.domain.Refund;
import sample.shop.repository.AccountRepository;
import sample.shop.repository.CashRepository;
import sample.shop.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CashRepository cashRepository;

    private CashService cashService;

    public Order orderInfo(OrderForm orderInfo) {
        Order order = new Order();

        checkPhone(orderInfo.getPhone());

        Address address = new Address(orderInfo.getCity(), orderInfo.getStreet(), orderInfo.getZipcode());

        order.setDate(LocalDateTime.now());
        order.setPhone(orderInfo.getPhone());
        order.setMemo(orderInfo.getMemo());
        order.setAddress(address);
        order.setOrderStatus(OrderStatus.ORDER);

        return order;
    }

    public void checkPhone(String phone) {
        String firstPhone = phone.substring(0, 3);

        if (!(firstPhone.equals("010") || firstPhone.equals("070")) && phone.length() == 11) {
            throw new IllegalStateException("핸드폰 번호가 규칙에 맞지 않습니다");
        }
    }

    public String idMaker() {
        StringBuffer idMaker = new StringBuffer();
        Random rnd = new Random();
        idMaker.append(LocalDate.now());
        idMaker.append("-");

        for (int i = 0; i < 5; i++) {
            if (rnd.nextBoolean()) {
                idMaker.append((char) ((int) (rnd.nextInt(26)) + 97));
            } else {
                idMaker.append((rnd.nextInt(10)));
            }
        }
        return idMaker.toString();
    }

    @Override
    @Transactional
    public Long saveWithoutSale(Member nowLoginMember, Project project) {
        Order order = new Order();

        order.setDate(LocalDateTime.now());
        order.setMemo("아직 판매중이 아닙니다");
        order.setOrderStatus(OrderStatus.ORDER);

        String idMarker = findByOrderIdMarker(nowLoginMember, project);

        if (idMarker == null) {
            order.setId(idMaker());
        } else {
            order.setId(idMarker);
        }

        order.setMember(nowLoginMember);
        order.setProject(project);

        if (project.getMember() != nowLoginMember) {
            return orderRepository.save(order);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Long save(OrderForm orderInfo, AccountInfoForm accountInfo, Member nowLoginMember, Project project) {
        Order order = orderInfo(orderInfo);

        String idMarker = orderRepository.findMyOrder(nowLoginMember).get(0).getId();

        if (idMarker.length() == 0) {
            order.setId(idMaker());
        } else {
            order.setId(idMarker);
        }

        order.setMember(nowLoginMember);
        order.setProject(project);

        if (project.getStepStatus().equals(ProjectStepStatus.FIRST)) {
            order.setProjectStepStatus(ProjectStepStatus.FIRST);
        } else if (project.getStepStatus().equals(ProjectStepStatus.SECOND)) {
            order.setProjectStepStatus(ProjectStepStatus.SECOND);
        } else if (project.getStepStatus().equals(ProjectStepStatus.THIRD)) {
            order.setProjectStepStatus(ProjectStepStatus.THIRD);
        }

        if (project.getMember() != nowLoginMember) {
            if (project.getOrderStatus().equals(ProjectOrderStatus.ACCOUNT)) {
                return saveAccount(project, nowLoginMember, order, accountInfo);
            } else if (project.getOrderStatus().equals(ProjectOrderStatus.CASH)) {
                return saveCash(project, nowLoginMember, order);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Long saveAccount(Project project, Member myMember, Order order, AccountInfoForm accountInfo) {
        Account account = new Account();

        Refund refund = new Refund(accountInfo.getRefundName(), accountInfo.getRefundBankName(),
                accountInfo.getRefundAccount());
        Purchase purchase = new Purchase(accountInfo.getPurchaseBankName(), accountInfo.getPurchaseAccount(),
                accountInfo.getPurchaseDate());

        account.setRefund(refund);
        account.setPurchase(purchase);

        accountRepository.save(account);

        order.setAccount(account);

        return orderRepository.save(order);
    }

    public Long saveCash(Project project, Member myMember, Order order) {
        Cash cash = cashRepository.findByNo(myMember.getCash().getNo());

        if (project.getStatus().equals(ProjectStatus.HELP)) {
            order.getCashList().setCash(cash);

            return orderRepository.save(order);
        } else {
            int cashMoney = 0;

            if (project.getStepStatus().equals(ProjectStepStatus.FIRST)) {
                cashMoney = project.getFirstPrice();
            } else if (project.getStepStatus().equals(ProjectStepStatus.SECOND)) {
                cashMoney = project.getSecondPrice();
            } else if (project.getStepStatus().equals(ProjectStepStatus.THIRD)) {
                cashMoney = project.getThirdPrice();
            }

            int money = cash.getMoney() - cashMoney;

            if (money >= 0) {
                order.getCashList().setCash(cash);

                return orderRepository.save(order);
            } else {
                throw new IllegalStateException("돈이 부족합니다");
            }
        }
    }

    public String findByOrderIdMarker(Member nowLoginMember, Project project) {
        List<Order> orders = orderRepository.findMyOrder(nowLoginMember);

        if (orders.size() > 0) {
            for (int list = 0; list < orders.size(); list++) {
                if (orders.get(list).getProject().equals(project)
                        && !orders.get(list).getOrderStatus().equals(OrderStatus.CANCEL)) {
                    return orders.get(list).getId();
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> findMyProject(Member member) {
        List<Order> orders = orderRepository.findMyProject(member);

        if (orders != null) {
            return orders;
        } else {
            throw new IllegalStateException("주문서가 없습니다.");
        }
    }

    @Override
    @Transactional
    public List<Order> findMyOrder(Member member) {
        try {
            return orderRepository.findMyOrder(member);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Order findOne(Long no) {
        return orderRepository.findOne(no);
    }

    @Override
    @Transactional
    public void delete(Long no, Member nowLoginMember) {
        Order order = findOne(no);

        order.setDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.CANCEL);

        List<Order> orders = new ArrayList<>();
        orders.add(0, order);

        if (order.getMember() == nowLoginMember || order.getProject().getMember() == nowLoginMember) {
            deleteByProject(orders, order.getProject());
        }
    }

    @Override
    @Transactional
    public void update(Long no, UpdateOrderAccountRequest request) {
        Order order = findOne(no);

        Address address = new Address(request.getCity(), request.getStreet(), request.getZipcode());

        order.setDate(request.getDate());
        order.setPhone(request.getPhone());
        order.setMemo(request.getMemo());
        order.setAddress(address);

        if (order.getProject().getOrderStatus().equals(ProjectOrderStatus.ACCOUNT)) {
            updateByAccount(order, request);
        }
    }

    public void updateByAccount(Order order, UpdateOrderAccountRequest request) {
        Account account = order.getAccount();
        Refund refund = new Refund(request.getRefundName(), request.getRefundBankName(),
                request.getRefundAccount());
        Purchase purchase = new Purchase(request.getPurchaseBankName(), request.getPurchaseAccount(),
                request.getPurchaseDate());

        account.setRefund(refund);
        account.setPurchase(purchase);

        order.setAccount(account);
    }

    @Override
    @Transactional
    public List<Order> updateByProject(Project project) {
        List<Order> orders = project.getOrders();
        for (int order = 0; order >= orders.size(); order++) {
            orders.get(order).setOrderStatus(OrderStatus.Tracking);
        }
        return orders;
    }

    @Override
    @Transactional
    public void deleteByProject(List<Order> orders, Project project) {
        if (orders.size() > 0) {
            for (int list = 0; list >= orders.size(); list++) {
                if (project.getOrderStatus().equals(ProjectOrderStatus.CASH)) {
                    deleteByProjectByCash(orders, list);
                } else if (project.getOrderStatus().equals(ProjectOrderStatus.ACCOUNT)) {
                    throw new IllegalStateException("직접 취소 부탁드립니다.");
                }

                orders.get(list).setDate(LocalDateTime.now());
                orders.get(list).setOrderStatus(OrderStatus.CANCEL);
            }
        }
    }

    public void deleteByProjectByCash(List<Order> orders, int list) {
        int money = 0;
        Member member = orders.get(list).getMember();

        if (orders.get(list).getProject().getStepStatus().equals(ProjectStepStatus.FIRST)) {
            money += orders.get(list).getProject().getFirstPrice();
        } else if (orders.get(list).getProject().getStepStatus().equals(ProjectStepStatus.SECOND)) {
            money += orders.get(list).getProject().getSecondPrice();
        } else if (orders.get(list).getProject().getStepStatus().equals(ProjectStepStatus.THIRD)) {
            money += orders.get(list).getProject().getThirdPrice();
        }

        money += member.getCash().getMoney();

        cashService.update(member.getCash().getNo(), money, member);
    }
}