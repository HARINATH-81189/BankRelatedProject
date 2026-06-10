package demo;

import com.linecorp.armeria.client.WebClient;
import com.linecorp.armeria.common.AggregatedHttpResponse;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.testing.junit5.server.ServerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankServiceTest{
    @RegisterExtension
    static final ServerExtension server=new ServerExtension(){
        @Override
        protected void configure(ServerBuilder sb) throws Exception {
            sb.annotatedService(new BankService());
        }
    };

    private HttpRequest createAccountRequest() throws Exception {

        String json =
                """
                {
                  "accountNumber":1001,
                  "accountHolder":"Hari",
                  "balance":5000
                }
                """;

        return HttpRequest.builder()
                .post("/accounts")
                .content(MediaType.JSON, json)
                .build();
    }

    @Test
    void createAccount() throws Exception {

        WebClient client = WebClient.of(server.httpUri());

        AggregatedHttpResponse res = client.execute(createAccountRequest())//here we are sending the request to the server
                        .aggregate()                    //collects all the small chunks  from the server and creates complete response
                        .join();                            //it tells to wait for the complete response to arrive

        assertEquals(
                HttpStatus.OK,
                res.status());
    }

    @Test
    void getAccount() throws Exception {

        WebClient client = WebClient.of(server.httpUri());

        client.execute(createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res = client.get("/accounts/1001")
                        .aggregate()
                        .join();

        assertEquals(HttpStatus.OK,res.status());
    }

    @Test
    void accountNotFound() {

        WebClient client =WebClient.of(server.httpUri());

        AggregatedHttpResponse res = client.get("/accounts/999")
                        .aggregate()
                        .join();

        assertEquals(HttpStatus.NOT_FOUND, res.status());
    }

    @Test
    void getAccounts() throws Exception {

        WebClient client = WebClient.of(server.httpUri());

        client.execute(createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res = client.get("/accounts")
                        .aggregate()
                        .join();

        assertEquals( HttpStatus.OK,res.status());
    }

    @Test
    void checkBalance() throws Exception {

       WebClient client =   WebClient.of(server.httpUri());

        client.execute(createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res =client.get("/accounts/1001/balance")
                        .aggregate()
                        .join();

        assertEquals(
                HttpStatus.OK,
                res.status());
    }

    private HttpRequest depositRequest() throws Exception {

        String json =
                """
                {
                  "amount":1000
                }
                """;

        return HttpRequest.builder()
                .put("/accounts/1001/deposit")
                .content(MediaType.JSON,json)
                .build();
    }

    @Test
    void depositMoney() throws Exception {

        WebClient client = WebClient.of(server.httpUri());

        client.execute(createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res = client.execute(depositRequest())
                        .aggregate()
                        .join();

        assertEquals(HttpStatus.OK,res.status());
    }

    private HttpRequest withdrawRequest() throws Exception {

        String json =
                """
                {
                  "amount":500
                }
                """;

        return HttpRequest.builder()
                .put("/accounts/1001/withdraw")
                .content(MediaType.JSON,json)
                .build();
    }

    @Test
    void withdrawMoney() throws Exception {

        WebClient client =WebClient.of(server.httpUri());

        client.execute(createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res = client.execute(  withdrawRequest())
                        .aggregate()
                        .join();

        assertEquals(HttpStatus.OK, res.status());
    }

    @Test
    void insufficientBalance() throws Exception {

        WebClient client = WebClient.of(server.httpUri());

        client.execute( createAccountRequest())
                .aggregate()
                .join();

        String json =
                """
                {
                  "amount":100000
                }
                """;

        HttpRequest request = HttpRequest.builder()
                        .put("/accounts/1001/withdraw")
                        .content( MediaType.JSON,json)
                        .build();

        AggregatedHttpResponse res = client.execute(request)
                        .aggregate()
                        .join();

        assertEquals(HttpStatus.BAD_REQUEST,res.status());
    }

    @Test
    void deleteAccount() throws Exception {

        WebClient client =  WebClient.of(server.httpUri());

        client.execute(  createAccountRequest())
                .aggregate()
                .join();

        AggregatedHttpResponse res = client.delete("/accounts/1001")            //before deleting it is creating an account , thats why n number of times we deleted
                        .aggregate()                                                 // it is again created and again deleted
                        .join();

        assertEquals( HttpStatus.OK,  res.status());
    }

}