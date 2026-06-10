package demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;

import java.lang.reflect.ParameterizedType;

public class AllConvertersInOneFile
        implements RequestConverterFunction {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    @Override
    public Object convertRequest(
            ServiceRequestContext ctx,
            AggregatedHttpRequest request,
            Class<?> expectedResultType,
            ParameterizedType expectedParameterizedResultType)
            throws Exception {

        JsonNode json =
                mapper.readTree(request.contentUtf8());

        if (expectedResultType == BankAccount.class) {

            return new BankAccount(
                    json.get("accountNumber").asInt(),
                    json.get("accountHolder").asText(),
                    json.get("balance").asDouble()
            );
        }

        if (expectedResultType == DepositRequest.class) {

            return new DepositRequest(
                    json.get("amount").asDouble()
            );
        }

        if (expectedResultType == WithdrawRequest.class) {

            return new WithdrawRequest(
                    json.get("amount").asDouble()
            );
        }

        return RequestConverterFunction.fallthrough();
    }
}