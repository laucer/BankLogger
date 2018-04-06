import com.gargoylesoftware.htmlunit.Page;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Json parser used by {@link PkoWebScraper}.
 */

public class PkoJsonRequestResponseParser {
    public String getSessionId(Page page) {
        String response = page.getWebResponse().getContentAsString();
        JSONObject serverResponse = new JSONObject(response);
        JSONObject sessionInfo = serverResponse.getJSONObject("session");

        return sessionInfo.getString("sid");
    }

    public String getFlowId(Page page) {
        String response = page.getWebResponse().getContentAsString();
        JSONObject serverResponse = new JSONObject(response);
        JSONObject sessionInfo = serverResponse.getJSONObject("response");

        return sessionInfo.getString("flow_id");
    }

    public List<PkoAccount> getAccounts(Page page) {
        String response = page.getWebResponse().getContentAsString();
        JSONObject serverResponse = new JSONObject(response);
        JSONObject accountData = serverResponse.getJSONObject("response");
        JSONArray accountsToParse = accountData.getJSONArray("account_list");
        List<PkoAccount> accounts = parseAccounts(accountsToParse);

        return accounts;
    }

    private List<PkoAccount> parseAccounts(JSONArray products) {
        List<PkoAccount> accounts = new ArrayList<>();
        for (int i = 0; i < products.length(); ++i) {
            JSONObject object = products.getJSONObject(i);
            PkoAccount account = parseAccount(object);
            accounts.add(account);
        }

        return accounts;
    }

    private PkoAccount parseAccount(JSONObject data) {
        String accountName = data.getString("title");
        String accountNumber = data.getJSONObject("number").getString("value");
        JSONObject balanceJson = data.getJSONObject("balance");
        Double balance = balanceJson.getDouble("value");
        String currency = balanceJson.getString("currency");

        return new PkoAccount(accountName, accountNumber, balance, currency);
    }
}
