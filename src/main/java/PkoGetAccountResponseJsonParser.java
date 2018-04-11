import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class PkoGetAccountResponseJsonParser {
    public Collection<BankAccount> getAccounts(String response) {
        JSONObject serverResponse = new JSONObject(response);
        JSONObject accountData = serverResponse.getJSONObject("response");
        JSONArray accountsToParse = accountData.getJSONArray("account_list");
        return parseAccounts(accountsToParse);
    }

    private Collection<BankAccount> parseAccounts(JSONArray products) {
        Collection<BankAccount> accounts = new ArrayList<>();
        for (int i = 0; i < products.length(); ++i) {
            JSONObject object = products.getJSONObject(i);
            BankAccount account = parseAccount(object);
            accounts.add(account);
        }
        return accounts;
    }

    private BankAccount parseAccount(JSONObject data) {
        String accountName = data.getString("title");
        String accountNumber = data.getJSONObject("number").getString("value");
        JSONObject balanceJson = data.getJSONObject("balance");
        Double balance = balanceJson.getDouble("value");
        String currency = balanceJson.getString("currency");
        return new BankAccount(accountName, accountNumber, balance, currency);
    }
}
