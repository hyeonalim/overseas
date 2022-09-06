package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import sample.shop.domain.Member;

@Getter
@Setter
public class ProjectForm {
    private Long no;

    @NotEmpty(message = "프로젝트 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "프로젝트 상태는 필수입니다.")
    private String status;

    private int firstPrice;
    private int secondPrice;
    private int thirdPrice;
    private int quantity;

    @NotEmpty(message = "결제 방식은 필수입니다.")
    private String orderStatus;

    private String country;

    @NotEmpty(message = "설명은 필수입니다.")
    private String exp;

    private Member member;
}
