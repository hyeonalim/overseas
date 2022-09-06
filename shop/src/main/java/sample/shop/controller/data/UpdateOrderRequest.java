package sample.shop.controller.data;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UpdateOrderRequest {
    private LocalDateTime date;
    private String phone;
    private String memo;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String city;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String street;

    @NotEmpty(message = "배송지는 필수입니다.")
    private String zipcode;

    public UpdateOrderRequest() {
    }

    public UpdateOrderRequest(LocalDateTime date, String phone, String memo, String city, String street,
            String zipcode) {
        this.date = date;
        this.phone = phone;
        this.memo = memo;
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
