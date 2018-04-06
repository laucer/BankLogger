import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.JSONParser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

public class PkoJsonParserTest {

    @Mock
    private Page page;

    @Mock
    private WebResponse response;

    private PkoJsonParser parser = new PkoJsonParser();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        given(page.getWebResponse()).willReturn(response);
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

        given(response.getContentAsString()).willReturn(json.toString());

        // when
        String resultSessionId = parser.getSessionId(page);

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

        given(response.getContentAsString()).willReturn(json.toString());

        // when
        Throwable thrown = catchThrowable(() -> parser.getSessionId(page));

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

        given(response.getContentAsString()).willReturn(json.toString());

        // when
        String resultFlowId = parser.getFlowId(page);

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

        given(response.getContentAsString()).willReturn(json.toString());

        // when
        Throwable thrown = catchThrowable(() -> parser.getFlowId(page));

        // then
        assertThat(thrown)
                .isInstanceOf(JSONException.class)
                .hasMessage("JSONObject[\"flow_id\"] not found.");
    }

    @Test
    public void shouldParseBalances() throws ParseException, IOException {
        // given
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader("src/main/java/test.json"));

        given(response.getContentAsString()).willReturn(object.toString());

        Map<String, Double> expectedAccounts = new HashMap<>();
        expectedAccounts.put("credit1", -200.53);
        expectedAccounts.put("account1", 1.68);
        expectedAccounts.put("account2", 0.0);

        // when
        Map<String, Double> resultAccounts = parser.parseBalances(page);

        // then
        assertThat(resultAccounts).isEqualTo(expectedAccounts);
    }
}