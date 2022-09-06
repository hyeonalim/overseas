package sample.shop.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_no")
    private Long no;

    @Embedded
    private Refund refund; // 환불 계좌

    @Embedded
    private Purchase purchase; // 구매 계좌

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    private Order order;

}