import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import org.json.JSONObject;

import java.net.URL;

class RequestManager {
    static WebRequest prepareLoginRequest(final URL url, final String id) {
        final WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");

        final JSONObject body = new JSONObject();
        body.put("_method", "POST");

        final JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "login");

        final JSONObject bodyRequestData = new JSONObject();
        bodyRequestData.put("login", id);

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    static WebRequest preparePasswordRequest(
            final URL url,
            final String sessionId,
            final String flowId,
            final String password
    ) {
        final WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "PUT");
        request.setAdditionalHeader("X-HTTP-Method", "PUT");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "PUT");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        final JSONObject body = new JSONObject();
        body.put("_method", "PUT");
        body.put("sid", sessionId);

        final JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "password");
        bodyRequest.put("flow_id", flowId);
        bodyRequest.put("first_prelogin", true);

        final JSONObject bodyRequestData = new JSONObject();
        bodyRequestData.put("password", password);

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    static WebRequest prepareLastAuthenticatorRequest(final URL url , final String sessionId, final String flowId){
        final WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "PUT");
        request.setAdditionalHeader("X-HTTP-Method", "PUT");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "PUT");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        final JSONObject body = new JSONObject();
        body.put("_method", "PUT");
        body.put("sid", sessionId);

        final JSONObject bodyRequest = new JSONObject();
        bodyRequest.put("state", "dispatch");
        bodyRequest.put("flow_id", flowId);
        bodyRequest.put("first_prelogin", true);

        final JSONObject bodyRequestData = new JSONObject();

        bodyRequest.put("data", bodyRequestData);
        body.put("request", bodyRequest);

        request.setRequestBody(body.toString());

        return request;
    }

    static WebRequest prepareGetBalanceRequest(final URL url, final String sessionId){
        final WebRequest request = new WebRequest(url, HttpMethod.POST);
        request.setAdditionalHeader("Content-Type", "application/json");
        request.setAdditionalHeader("x-ias-ias_sid", sessionId);
        request.setAdditionalHeader("X-HTTP-Method-Override", "GET");
        request.setAdditionalHeader("X-HTTP-Method", "GET");
        request.setAdditionalHeader("X-METHOD-OVERRIDE", "GET");
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");

        final JSONObject body = new JSONObject();
        body.put("_method", "GET");
        body.put("sid", sessionId);

        request.setRequestBody(body.toString());

        return request;
    }
}
