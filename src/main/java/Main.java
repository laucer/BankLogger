import com.gargoylesoftware.htmlunit.WebClient;

import java.io.IOException;

/**
 * This application can be used to automatically logging into your pko bank account and print account balances,
 **/

public class Main {
    public static void main(String[] args) {
        final WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        client.getCookieManager().setCookiesEnabled(true);

        final String id = "INSERT_YOUR_ID_HERE";
        final String password = "INSERT_YOU_PASSWORD_HERE";

        final Authenticator authenticator = new Authenticator(client, id, password);
        try {
            System.out.println(authenticator.getAccounts());
        } catch (IOException e) {
            System.out.println("Could not get accounts list: " + e.getMessage());
        }
    }
}
