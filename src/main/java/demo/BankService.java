package demo;

import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.server.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankService {

    private final Map<Integer, BankAccount> accounts =
            new ConcurrentHashMap<>();

    @Post("/accounts")
    @RequestConverter(BankAccountRequestConverter.class)
    public HttpResponse createAccount(
            BankAccount account) {

        accounts.put(
                account.getAccountNumber(),
                account);

        return HttpResponse.ofJson(account);
    }

    @Get("/accounts/:accountNumber")                    //note we are using the : meaning , the part after that : will be taken as pathvariable
    public HttpResponse getAccount(
            @Param("accountNumber") int accountNumber) {

        BankAccount account =
                accounts.get(accountNumber);

        if (account == null) {
            return HttpResponse.of(HttpStatus.NOT_FOUND);
        }

        return HttpResponse.ofJson(account);
    }

    @Get("/accounts")
    @ProducesJson
    public Iterable<BankAccount> getAccounts() {

        return accounts.values();
    }



    @Get("/accounts/:accountNumber/balance")
    public HttpResponse checkBalance(
            @Param("accountNumber") int accountNumber) {

        BankAccount account =
                accounts.get(accountNumber);

        if (account == null) {
            return HttpResponse.of(404);
        }

        return HttpResponse.ofJson(
                Map.of(
                        "accountNumber", accountNumber,
                        "balance", account.getBalance()
                )
        );
    }


    @Put("/accounts/:accountNumber/deposit")
    @RequestConverter(DepositRequestConverter.class)
    public HttpResponse deposit(@Param("accountNumber") int accountNumber, DepositRequest request) {

        BankAccount account =
                accounts.get(accountNumber);

        if (account == null) {
            return HttpResponse.of(404);
        }

        BankAccount updated =
                new BankAccount(
                        account.getAccountNumber(),
                        account.getAccountHolder(),
                        account.getBalance() + request.getAmount());

        accounts.put(accountNumber, updated);

        return HttpResponse.ofJson(updated);
    }
    @Put("/accounts/:accountNumber/withdraw")
    @RequestConverter(WithdrawRequestConverter.class)
    public HttpResponse withdraw(
            @Param("accountNumber") int accountNumber,
            WithdrawRequest request) {

        BankAccount account =
                accounts.get(accountNumber);

        if (account == null) {
            return HttpResponse.of(404);
        }

        if (account.getBalance() < request.getAmount()) {
            return HttpResponse.of(400);
        }

        BankAccount updated =
                new BankAccount(
                        account.getAccountNumber(),
                        account.getAccountHolder(),
                        account.getBalance() - request.getAmount());

        accounts.put(accountNumber, updated);

        return HttpResponse.ofJson(updated);
    }
    @Delete("/accounts/:accountNumber")
    public HttpResponse deleteAccount(
            @Param("accountNumber") int accountNumber) {

        BankAccount removed = accounts.remove(accountNumber);

        if (removed == null) {
            return HttpResponse.of(404);
        }

        return HttpResponse.of(200);
    }


}