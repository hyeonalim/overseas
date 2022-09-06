package sample.shop.controller.data;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberFormLogin {
    @NotEmpty(message = "회원 아이디는 필수입니다.")
    private String id;

    @NotEmpty(message = "패스워드는 필수입니다.")
    private String pwd;

}
