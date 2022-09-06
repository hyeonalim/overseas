package sample.shop.domain;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Refund {
    private String refundName;
    private String refundBankName;
    private String refundAccount;

    protected Refund() {
    }

    public Refund(String refundName, String refundBankName, String refundAccount) {
        this.refundName = refundName;
        this.refundBankName = refundBankName;
        this.refundAccount = refundAccount;
    }

}
