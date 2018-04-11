import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class PkoLoginResponseJsonParserTest {
    private PkoLoginResponseJsonParser parser;

    @Before
    public void setUp(){
        parser = new PkoLoginResponseJsonParser();
    }

    @Test
    public void shouldGetSessionId() {
        // given
        String sessionId = "101";

        JSONObject sid = new JSONObject();
        sid.put("sid", sessionId);

        JSONObject json = new JSONObject();
        json.put("id", "test");
        json.put("session", sid);

        // when
        String resultSessionId = parser.getSessionId(json.toString());

        // then
        assertThat(resultSessionId).isEqualTo(sessionId);
    }

    @Test
    public void shouldNotGetSessionId() {
        // given
        JSONObject sid = new JSONObject();

        JSONObject json = new JSONObject();
        json.put("id", "test");
        json.put("session", sid);

        // when
        Throwable thrown = catchThrowable(() -> parser.getSessionId(json.toString()));

        // then
        assertThat(thrown)
                .isInstanceOf(JSONException.class)
                .hasMessage("JSONObject[\"sid\"] not found.");
    }

    @Test
    public void shouldGetFlowId() {
        // given
        String flowId = "101";

        JSONObject sid = new JSONObject();
        sid.put("flow_id", flowId);

        JSONObject json = new JSONObject();
        json.put("id", "test");
        json.put("response", sid);

        // when
        String resultFlowId = parser.getFlowId(json.toString());

        // then
        assertThat(resultFlowId).isEqualTo(flowId);
    }

    @Test
    public void shouldNotGetFlowId() {
        // given
        JSONObject serverResponse = new JSONObject();

        JSONObject json = new JSONObject();
        json.put("id", "test");
        json.put("response", serverResponse);

        // when
        Throwable thrown = catchThrowable(() -> parser.getFlowId(json.toString()));

        // then
        assertThat(thrown)
                .isInstanceOf(JSONException.class)
                .hasMessage("JSONObject[\"flow_id\"] not found.");
    }
}