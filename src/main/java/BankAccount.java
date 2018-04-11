import java.util.Objects;

public class BankAccount {
    private String accountName;
    private String accountNumber;
    private Double balance;
    private String currency;

    public BankAccount(String accountName, String accountNumber, Double balance, String currency) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "{accountName=\"" + accountName + '\"' +
                ", accountNumber=\"" + accountNumber + '\"' +
                ", balance=\"" + balance + '\"' +
                ", currency=\"" + currency + '\"' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount account = (BankAccount) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, accountNumber, balance, currency);
    }
}
