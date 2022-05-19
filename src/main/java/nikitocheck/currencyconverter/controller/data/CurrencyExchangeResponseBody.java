package nikitocheck.currencyconverter.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

@JsonSerialize
public record CurrencyExchangeResponseBody(
        @JsonProperty(required = true)
        String source,
        @JsonProperty(required = true)
        String target,
        @JsonProperty(required = true)
        BigDecimal sourceAmount,
        @JsonProperty(required = true)
        BigDecimal resultAmount,
        @JsonProperty(required = true)
        Long millisecondsElapsed
) {
}
