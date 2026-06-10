package demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;

import java.lang.reflect.ParameterizedType;

public class DepositRequestConverter
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

        if (expectedResultType != DepositRequest.class) {
            return RequestConverterFunction.fallthrough();
        }

        JsonNode json =
                mapper.readTree(request.contentUtf8());

        return new DepositRequest(
                json.get("amount").asDouble()
        );
    }
}