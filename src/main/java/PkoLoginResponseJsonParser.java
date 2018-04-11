import org.json.JSONObject;

public class PkoLoginResponseJsonParser {
    public String getSessionId(String response) {
        JSONObject serverResponse = new JSONObject(response);
        JSONObject sessionInfo = serverResponse.getJSONObject("session");
        return sessionInfo.getString("sid");
    }

    public String getFlowId(String response) {
        JSONObject serverResponse = new JSONObject(response);
        JSONObject sessionInfo = serverResponse.getJSONObject("response");
        return sessionInfo.getString("flow_id");
    }
}
