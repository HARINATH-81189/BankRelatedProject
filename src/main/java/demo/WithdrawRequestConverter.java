package demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.annotation.Nullable;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.RequestConverter;
import com.linecorp.armeria.server.annotation.RequestConverterFunction;

import java.lang.reflect.ParameterizedType;

public class WithdrawRequestConverter implements RequestConverterFunction {
    private  static final ObjectMapper mapper=new ObjectMapper();
    @Override
    public @Nullable Object convertRequest(ServiceRequestContext ctx,
                                           AggregatedHttpRequest request,
                                           Class<?> expectedResultType,
                                           @Nullable ParameterizedType expectedParameterizedResultType)
            throws Exception {
        if(expectedResultType!=WithdrawRequest.class){
            return RequestConverterFunction.fallthrough();
        }
        JsonNode json=mapper.readTree(request.contentUtf8());
        return new WithdrawRequest(json.get("amount").asDouble()); //remember here we are getting the amount from the json tree that we are sending from the postman/client
    }
}
