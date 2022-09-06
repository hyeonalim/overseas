package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCashForm {

    private String phone;
    private String memo;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String city;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String street;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String zipcode;

    public OrderCashForm() {
    }

    public OrderCashForm(String phone, String memo, String city, String street, String zipcode) {
        this.phone = phone;
        this.memo = memo;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}