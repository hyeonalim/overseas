package sample.shop.controller.data;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateOrderAccountRequest {
    private LocalDateTime date;
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

    public UpdateOrderAccountRequest() {
    }

    public UpdateOrderAccountRequest(LocalDateTime date, String phone, String memo, String city, String street,
            String zipcode, String refundName, String refundBankName, String refundAccount, String purchaseDate,
            String purchaseBankName, String purchaseAccount) {
        this.date = date;
        this.phone = phone;
        this.memo = memo;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
        this.refundName = refundName;
        this.refundBankName = refundBankName;
        this.refundAccount = refundAccount;
        this.purchaseDate = purchaseDate;
        this.purchaseBankName = purchaseBankName;
        this.purchaseAccount = purchaseAccount;
    }

}
