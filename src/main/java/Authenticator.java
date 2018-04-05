import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * This class is used to log into pko bank and download account info.
 **/

class Authenticator {
    private static final String MAIN_WEBSITE = "https://www.ipko.pl/";
    private final WebClient client;
    private final String id;
    private final String password;

    Authenticator(final WebClient client, final String id, final String password) {
        this.client = client;
        this.id = id;
        this.password = password;
    }

    Map<String, Double> getAccounts() throws IOException {
        final Page passwordPage = sendLogin();

        final String sessionId = JsonParser.getSessionId(passwordPage);
        final String flowId = JsonParser.getFlowId(passwordPage);

        sendPassword(sessionId, flowId);
        continueAuthorization(sessionId, flowId);

        final Page balancePage = getBalance(sessionId);

        return JsonParser.parseBalances(balancePage);
    }


    Page sendLogin() throws IOException {
        final URL url = new URL(MAIN_WEBSITE + "/secure/ikd3/api/login");
        final WebRequest request = RequestManager.prepareLoginRequest(url, id);

        return client.getPage(request);
    }

    void sendPassword(final String sessionId, final String flowId) throws IOException {
        final URL url = new URL(MAIN_WEBSITE + "/secure/ikd3/api/login");
        final WebRequest request = RequestManager.preparePasswordRequest(url, sessionId, flowId, password);

        client.getPage(request);
    }

    void continueAuthorization(final String sessionId, final String flowId) throws IOException {
        final URL url = new URL(MAIN_WEBSITE + "/secure/ikd3/api/login");
        final WebRequest request = RequestManager.prepareLastAuthenticatorRequest(url, sessionId, flowId);

        client.getPage(request);
    }

    Page getBalance(final String sessionId) throws IOException {
        final URL url = new URL(MAIN_WEBSITE + "/secure/ikd3/api/home/balance");
        final WebRequest request = RequestManager.prepareGetBalanceRequest(url, sessionId);

        return client.getPage(request);
    }
}
