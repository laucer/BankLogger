import java.io.IOException;
import java.util.List;

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
            List<PkoAccount> accounts = pkoWebScraper.getAccounts(sessionId);
            accounts.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Could not get accounts list: " + e.getMessage());
        }
    }
}
