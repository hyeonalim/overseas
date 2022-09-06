package sample.shop.domain;

import java.time.LocalDate;

import javax.persistence.Column;
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
public class Cash {

    @Id
    @GeneratedValue
    @Column(name = "cash_no")
    private Long no;

    private int money;
    private LocalDate date;

    @OneToOne(mappedBy = "cash", fetch = FetchType.LAZY)
    private CashList cashList;

    @OneToOne(mappedBy = "cash", fetch = FetchType.LAZY)
    private Member member;

}