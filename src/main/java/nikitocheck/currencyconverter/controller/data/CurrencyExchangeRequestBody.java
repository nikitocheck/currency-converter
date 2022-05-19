package nikitocheck.currencyconverter.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;


@JsonSerialize
public record CurrencyExchangeRequestBody(
        @JsonProperty(required = true)
        String source,
        @JsonProperty(required = true)
        String target,
        @JsonProperty(required = true)
        BigDecimal amount
) {

}
