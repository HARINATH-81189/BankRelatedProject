package demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;
import com.linecorp.armeria.server.annotation.FallthroughException;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;

public class BankAccountRequestConverter
        implements RequestConverterFunction {

    private static final ObjectMapper mapper =    new ObjectMapper();

    @Override
    public @Nullable Object convertRequest(
            ServiceRequestContext ctx,
            AggregatedHttpRequest request,
            Class<?> expectedResultType,
            @Nullable ParameterizedType expectedParameterizedResultType)
            throws Exception {

        if (expectedResultType != BankAccount.class) {
            return RequestConverterFunction.fallthrough();
        }

        JsonNode json = mapper.readTree(request.contentUtf8());

        return new BankAccount(
                json.get("accountNumber").asInt(),
                json.get("accountHolder").asText(),
                json.get("balance").asDouble()
        );
    }
}