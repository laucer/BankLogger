import com.gargoylesoftware.htmlunit.Page;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Json parser used by {@link PkoWebScraper}.
 */

class PkoJsonParser {
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

    public Map<String, Double> parseBalances(Page page) {
        String response = page.getWebResponse().getContentAsString();
        JSONObject serverResponse = new JSONObject(response);
        JSONObject accountData = serverResponse.getJSONObject("response");

        Map<String, Double> liabilities = getLiabilities(accountData);
        Map<String, Double> savings = getSavings(accountData);

        Map<String, Double> allAccounts = new HashMap<String, Double>();
        allAccounts.putAll(liabilities);
        allAccounts.putAll(savings);

        return allAccounts;
    }

    public static Map<String, Double> getLiabilities(JSONObject data) {
        JSONObject liabilities = data.getJSONObject("liabilities");
        JSONArray products = liabilities.getJSONArray("products");
        Map<String, Double> accounts = getAccounts(products);

        return accounts;
    }

    public static Map<String, Double> getSavings(JSONObject data) {
        JSONObject savings = data.getJSONObject("savings");
        JSONArray products = savings.getJSONArray("products");
        Map<String, Double> accounts = getAccounts(products);

        return accounts;
    }

    public static Map<String, Double> getAccounts(JSONArray products) {
        Map<String, Double> accounts = new HashMap<>();
        for (int i = 0; i < products.length(); ++i) {
            JSONObject object = products.getJSONObject(i);
            String name = object.getString("name");
            Double balance = object.getJSONObject("balance").getDouble("value");
            accounts.put(name, balance);
        }

        return accounts;
    }
}
