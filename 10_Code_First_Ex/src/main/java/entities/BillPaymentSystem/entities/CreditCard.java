package entities.BillPaymentSystem.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import java.time.Month;
@Entity
@DiscriminatorValue("card_type")
public class CreditCard extends BillingDetails{


    private String cardType;
    @Column(name = "expiration_month")
    private Month expirationMonth;
    @Column(name = "expiration_year")
    private int expirationYear;

    public CreditCard(){}

    public CreditCard(int number, String owner, String cardType, Month expirationMonth, int expirationYear){
        super(number, owner);
        this.cardType = cardType;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }



    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Month getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Month expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public int getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(int expirationYear) {
        this.expirationYear = expirationYear;
    }
}
