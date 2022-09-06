package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import sample.shop.domain.CashList;

@Getter
@Setter
public class CashForm {
    private int cash;
    private CashList cashList;

    @NotEmpty(message = "입금 날짜는 필수입니다.")
    private String purchaseDate;

    @NotEmpty(message = "입금 은행명은 필수입니다.")
    private String purchaseBankName;

    @NotEmpty(message = "입금 금액은 필수입니다.")
    private String purchaseAccount;

}
