import java.util.Objects;

public class PkoAccount {
    private String accountName;
    private String accountNumber;
    private Double balance;
    private String currency;

    public PkoAccount(String accountName, String accountNumber, Double balance, String currency) {
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
        PkoAccount account = (PkoAccount) o;
        return Objects.equals(accountName, account.accountName) &&
                Objects.equals(accountNumber, account.accountNumber) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(currency, account.currency);
    }
}
