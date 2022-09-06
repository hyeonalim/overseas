package sample.shop.controller.data;

import lombok.Data;

@Data
public class UpdateMemberRequest {
    private String name;
    private String id;
    private String pwd;
    private String checkPwd;
    private String phone;
    private String photoUrl;
    private String city;
    private String street;
    private String zipcode;
}
