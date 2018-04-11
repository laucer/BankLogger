import org.json.simple.parser.JSONParser;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PkoGetAccountResponseJsonParserTest {
    @Test
    public void shouldParseBalances() throws IOException, org.json.simple.parser.ParseException {
        // given
        PkoGetAccountResponseJsonParser parser = new PkoGetAccountResponseJsonParser();
        JSONParser jsonParser = new JSONParser();
        Object object = jsonParser.parse(new FileReader("src/main/java/test.json"));

        List<BankAccount> expectedAccounts = new ArrayList<>();

        BankAccount account1 = new BankAccount(
                "PKO KONTO DLA M\u0141ODYCH",
                "123456789",
                19.68,
                "PLN"
        );

       BankAccount account2 = new BankAccount(
         "Second test account",
         "987654321",
         1.27,
         "EU"
       );

        expectedAccounts.add(account1);
        expectedAccounts.add(account2);

        // when
        Collection<BankAccount> resultAccounts = parser.getAccounts(object.toString());

        // then
        assertThat(resultAccounts).isEqualTo(expectedAccounts);
    }
 }