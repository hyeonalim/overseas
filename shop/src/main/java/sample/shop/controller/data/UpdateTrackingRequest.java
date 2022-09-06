package sample.shop.controller.data;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpdateTrackingRequest {
    private String status;
    private LocalDateTime date;
    private String local;
    private String memo;
}
