package sample.shop.controller.data;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import sample.shop.domain.Order;

@Getter
@Setter
public class TrackingForm {
    private Long no;

    private String status;
    private LocalDateTime date;
    private String local;
    private String memo;

    private Order order;

}