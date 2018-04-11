import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.util.Collection;

/**
 * This application can be used to automatically logging into your pko bank account and print account balances,
 **/

public class Main {
    public static void main(String[] args) {
        String id = "INSERT_YOUR_ID_HERE";
        String password = "INSERT_YOU_PASSWORD_HERE";

        PkoWebScraper pkoWebScraper = new PkoWebScraper(id, password);
        try {
            String sessionId = pkoWebScraper.signIn();
            Collection<BankAccount> accounts = pkoWebScraper.getAccounts(sessionId);
            accounts.forEach(System.out::println);
        } catch (FailedLoginException e) {
            System.out.println("Could not login into account: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("An IOException was caught" + e.getMessage());
        }
    }
}
