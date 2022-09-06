package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.")
    private String name;

    @NotEmpty(message = "회원 아이디는 필수입니다.")
    private String id;

    @NotEmpty(message = "패스워드는 필수입니다.")
    private String pwd;

    @NotEmpty(message = "패스워드 체크는 필수입니다.")
    private String checkPwd;

    private String phone;
    private String city;
    private String street;
    private String zipcode;
}
