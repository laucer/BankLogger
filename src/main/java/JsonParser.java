import com.gargoylesoftware.htmlunit.Page;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class JsonParser {
    static String getSessionId(final Page page) {
        final String response = page.getWebResponse().getContentAsString();
        final JSONObject serverResponse = new JSONObject(response);
        final JSONObject sessionInfo = serverResponse.getJSONObject("session");

        return sessionInfo.getString("sid");
    }

    static String getFlowId(final Page page) {
        final String response = page.getWebResponse().getContentAsString();
        final JSONObject serverResponse = new JSONObject(response);
        final JSONObject sessionInfo = serverResponse.getJSONObject("response");

        return sessionInfo.getString("flow_id");
    }

    static Map<String, Double> parseBalances(final Page page){
        final String response = page.getWebResponse().getContentAsString();
        final JSONObject serverResponse = new JSONObject(response);
        final JSONObject accountData = serverResponse.getJSONObject("response");

        final Map<String,Double> liabilities = getLiabilities(accountData);
        final Map<String, Double> savings = getSavings(accountData);

        final Map<String, Double> allAccounts = new HashMap<String, Double>();
        allAccounts.putAll(liabilities);
        allAccounts.putAll(savings);

        return allAccounts;
    }

    private static Map<String, Double> getLiabilities(final JSONObject data){
        final JSONObject liabilities = data.getJSONObject("liabilities");
        final JSONArray products = liabilities.getJSONArray("products");
        final Map<String, Double> accounts = getAccounts(products);

        return accounts;
    }

    private static Map<String, Double> getSavings(final JSONObject data){
        final JSONObject savings = data.getJSONObject("savings");
        final JSONArray products = savings.getJSONArray("products");
        final Map<String, Double> accounts = getAccounts(products);

        return accounts;
    }

    private static Map<String,Double> getAccounts(final JSONArray products){
        final Map<String, Double> accounts = new HashMap<String, Double>();
        for(int i = 0; i < products.length(); ++i ){
            final JSONObject object = products.getJSONObject(i);
            final String name = object.getString("name");
            final Double balance = object.getJSONObject("balance").getDouble("value");
            accounts.put(name, balance);
        }

        return accounts;
    }
}
