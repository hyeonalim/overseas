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
import sample.shop.controller.data.CashForm;
import sample.shop.domain.Cash;
import sample.shop.domain.CashList;
import sample.shop.domain.CashStatus;
import sample.shop.service.CashService;
import sample.shop.service.MemberService;

@RestController
@RequiredArgsConstructor
public class CashApiController {

    private final CashService cashService;
    private final MemberService memberService;

    // 생성
    /*
     * ㄴ캐쉬 충전
     * => 캐쉬 등록 요청
     * => 캐쉬 등록을 위해 돈 보내온 계좌명 받기
     * => 확인되면 캐쉬 올려주기 (관리자 사이트)
     * 
     * ㄴ캐쉬 이체
     * => 캐쉬에 남은 캐쉬 있는지 확인
     * => 캐쉬 이체를 위해 환불할 계좌명/계좌번호
     * => 확인되면 캐쉬 돌려주기 (관리자 사이트)
     */
    @PostMapping("/api/cash/save/create")
    public Long saveCash(@SessionAttribute("mSession") Long nowLoginMemberNo,
            @RequestBody @Valid CashForm request) {
        Long no = cashService.saveList(request.getCash(), memberService.myInfo(nowLoginMemberNo).getCash(), null);

        if (no != null) {
            return no;
        } else {
            throw new IllegalStateException("캐쉬 주문서 발행 중 이상이 생겼습니다.");
        }
    }

    // 내 캐쉬 주문서
    @GetMapping("/api/cash/myList")
    public List<CashDto> cashMyList(@SessionAttribute("mSession") Long nowLoginMemberNo) {
        List<CashList> cashByMy = cashService.findByMemberList(memberService.myInfo(nowLoginMemberNo));
        List<CashDto> collect = cashByMy.stream()
                .map(cl -> new CashDto(cl.getNo(), cl.getDate(), cl.getStatus(), cl.getMoney(),
                        projectName(cl.getNo()), projectNo(cl.getNo())))
                .collect(Collectors.toList());
        return collect;
    }

    // 모든 캐쉬 주문서
    @GetMapping("/api/cash/list")
    public List<CashDto> cashList(Cash cash) {
        List<CashList> cashByLoading = cashService.findByLoading(cash);
        List<CashDto> collect = cashByLoading.stream()
                .map(cl -> new CashDto(cl.getNo(), cl.getDate(), cl.getStatus(), cl.getMoney(),
                        projectName(cl.getNo()), projectNo(cl.getNo())))
                .collect(Collectors.toList());
        return collect;
    }

    public String projectName(Long no) {
        CashList cashList = cashService.findByCashListNo(no);

        if (cashList.getOrder() == null) {
            return null;
        } else {
            return cashList.getOrder().getProject().getName();
        }
    }

    public Long projectNo(Long no) {
        CashList cashList = cashService.findByCashListNo(no);

        if (cashList.getOrder() == null) {
            return null;
        } else {
            return cashList.getOrder().getProject().getNo();
        }
    }

    @Data
    @AllArgsConstructor // return 시, 꼭 필요한 내용
    static class CashDto {
        private Long no;

        private LocalDateTime date;
        private CashStatus status;
        private int money;
        private String projectName;
        private Long projectNo;
    }

    // 충전
    @GetMapping("/api/cash/add/{cashListNo}")
    public void mgtCash(@PathVariable("cashListNo") Long cashNo) {
        try {
            CashList cashList = cashService.findByCashListNo(cashNo);

            if (cashList.getStatus().equals(CashStatus.Loading)) {
                cashService.updateAdd(cashList.getCash().getNo(), cashList.getMoney(), cashList.getCash().getMember());
            }
        } catch (Exception e) {
            throw new IllegalStateException("주문서 발행 중 이상이 생겼습니다.");
        }
    }

    // 이체
    @PutMapping("/api/cash/out/{cashListNo}")
    public void outCash(@PathVariable("cashListNo") Long cashNo) {
        try {
            CashList cashList = cashService.findByNo(cashNo).getCashList();

            if (!cashList.getStatus().equals(CashStatus.Loading)) {
                cashService.updateOut(cashList.getCash().getNo(), cashList.getMoney(), cashList.getCash().getMember());
            }
        } catch (Exception e) {
            throw new IllegalStateException("주문서 발행 중 이상이 생겼습니다.");
        }
    }

    @Data
    @AllArgsConstructor
    static class UpdateOrderResponse {
        private Long no;
    }

}