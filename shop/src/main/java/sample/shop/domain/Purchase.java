package sample.shop.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Purchase {
    private String purchaseBankName;
    private String purchaseAccount;
    private String purchaseDate;

    protected Purchase() {
    }

    public Purchase(String purchaseBankName, String purchaseAccount, String purchaseDate) {
        this.purchaseBankName = purchaseBankName;
        this.purchaseAccount = purchaseAccount;
        this.purchaseDate = purchaseDate;
    }

}
