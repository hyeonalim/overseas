package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfoForm {
    @NotEmpty(message = "환불 계좌는 필수입니다.")
    private String refundName;

    @NotEmpty(message = "환불 은행명은 필수입니다.")
    private String refundBankName;

    @NotEmpty(message = "환불 금액은 필수입니다.")
    private String refundAccount;

    @NotEmpty(message = "입금 날짜는 필수입니다.")
    private String purchaseDate;

    @NotEmpty(message = "입금 은행명은 필수입니다.")
    private String purchaseBankName;

    @NotEmpty(message = "입금 금액은 필수입니다.")
    private String purchaseAccount;

    public AccountInfoForm() {
    }

    public AccountInfoForm(String refundName, String refundBankName, String refundAccount, String purchaseDate,
            String purchaseBankName, String purchaseAccount) {
        this.refundName = refundName;
        this.refundBankName = refundBankName;
        this.refundAccount = refundAccount;
        this.purchaseDate = purchaseDate;
        this.purchaseBankName = purchaseBankName;
        this.purchaseAccount = purchaseAccount;
    }

}
