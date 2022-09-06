package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderAccountForm {
    private String phone;
    private String memo;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String city;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String street;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String zipcode;

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

}
