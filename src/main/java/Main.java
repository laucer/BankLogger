import java.io.IOException;

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
            System.out.println(pkoWebScraper.getAccounts(sessionId));
        } catch (IOException e) {
            System.out.println("Could not get accounts list: " + e.getMessage());
        }
    }
}
