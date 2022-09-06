package sample.shop.controller.data;

import lombok.Data;

@Data
public class UpdateProjectRequest {
    private String name;
    private int firstPrice;
    private int secondPrice;
    private int thirdPrice;
    private int quantity;
    private String exp;
    private String status;
    private String orderStatus;
}
