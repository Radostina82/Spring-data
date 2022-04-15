package entities.BillPaymentSystem.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("bank_account")
public class BankAccount extends BillingDetails{

    @Column(name = "bank_name")
    private String bankName;
    @Column(name = "swift_code")
    private String swiftCode;

    public BankAccount() {

    }

    public BankAccount(int number, String owner, String bankName, String swiftCode) {
        super(number, owner);
        this.bankName = bankName;
        this.swiftCode = swiftCode;
    }


    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
