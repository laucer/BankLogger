import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import org.json.JSONObject;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

/**
 * This class is used to log into pko bank and download account info.
 **/

public class PkoWebScraper {
    private static String BASE_URL = "https://www.ipko.pl/";
    private WebClient client = new WebClient();
    private PkoLoginResponseJsonParser loginResponseJsonParser = new PkoLoginResponseJsonParser();
    private PkoGetAccountResponseJsonParser getAccountResponseJsonParser = new PkoGetAccountResponseJsonParser();

    private String id;
    private String password;

    public PkoWebScraper(String id, String password) {
        this.id = id;
        this.password = password;

        this.client.getOptions().setCssEnabled(false);
        this.client.getOptions().setJavaScriptEnabled(false);
        this.client.getCookieManager().setCookiesEnabled(true);
    }

    public String signIn() throws IOException, FailedLoginException {
        Page passwordPage = sendLogin();
        String content = passwordPage.getWebResponse().getContentAsString();
        String sessionId = loginResponseJsonParser.getSessionId(content);
        String flowId = loginResponseJsonParser.getFlowId(content);

        sendPassword(sessionId, flowId);
        continueAuthentication(sessionId, flowId);

        return sessionId;
    }

    private Page sendLogin() throws IOException, FailedLoginException {
        URL url = new URL(BASE_URL + "/secure/ikd3/api/login");
        WebRequest request = prepareLoginRequest(url);
        Page page = client.getPage(request);

        if (page.getWebResponse().getContentAsString().contains("Niepoprawny format numeru klienta lub loginu")) {
            throw new FailedLoginException("Wrong client id");
        }

        return page;
    }

    private WebRequest prepareLoginRequest(URL url) {
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");

        JSONObject body = new JSONObject();
        body.put("_method", "POST");

        JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "login");

        JSONObject bodyRequestData = new JSONObject();
        bodyRequestData.put("login", id);

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    private void sendPassword(String sessionId, String flowId) throws IOException, FailedLoginException {
        URL url = new URL(BASE_URL + "/secure/ikd3/api/login");
        WebRequest request = preparePasswordRequest(url, sessionId, flowId);
        Page page = client.getPage(request);
        if (page.getWebResponse().getContentAsString().contains("Niepoprawne logowanie")) {
            throw new FailedLoginException("Please check your password");
        }
    }

    private WebRequest preparePasswordRequest(URL url, String sessionId, String flowId) {
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "PUT");
        request.setAdditionalHeader("X-HTTP-Method", "PUT");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "PUT");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        JSONObject body = new JSONObject();
        body.put("_method", "PUT");
        body.put("sid", sessionId);

        JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "password");
        bodyRequest.put("flow_id", flowId);
        bodyRequest.put("first_prelogin", true);

        JSONObject bodyRequestData = new JSONObject();
        bodyRequestData.put("password", password);

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    private void continueAuthentication(String sessionId, String flowId) throws IOException {
        URL url = new URL(BASE_URL + "/secure/ikd3/api/login");
        WebRequest request = prepareLastAuthenticationRequest(url, sessionId, flowId);
        client.getPage(request);
    }

    private WebRequest prepareLastAuthenticationRequest(URL url, String sessionId, String flowId) {
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "PUT");
        request.setAdditionalHeader("X-HTTP-Method", "PUT");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "PUT");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        JSONObject body = new JSONObject();
        body.put("_method", "PUT");
        body.put("sid", sessionId);

        JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "dispatch");
        bodyRequest.put("flow_id", flowId);
        bodyRequest.put("first_prelogin", true);

        JSONObject bodyRequestData = new JSONObject();

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    public Collection<BankAccount> getAccounts(String sessionId) throws IOException {
        Page accountsPage = getAccountsPage(sessionId);
        String content = accountsPage.getWebResponse().getContentAsString();
        return getAccountResponseJsonParser.getAccounts(content);
    }

    private Page getAccountsPage(String sessionId) throws IOException {
        URL url = new URL(BASE_URL + "/secure/ikd3/api/accounts/init");
        WebRequest request = prepareGetAccountsRequest(url, sessionId);
        return client.getPage(request);
    }

    private WebRequest prepareGetAccountsRequest(URL url, String sessionId) {
        WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "GET");
        request.setAdditionalHeader("X-HTTP-Method", "GET");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "GET");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        JSONObject body = new JSONObject();
        body.put("_method", "GET");
        body.put("sid", sessionId);

        request.setRequestBody(body.toString());
        return request;
    }
}