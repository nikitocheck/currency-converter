package nikitocheck.currencyconverter.controller.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

@JsonSerialize
public record CurrencyExchangeErrorResponse(
        @JsonProperty(required = true)
        String errorMessage,
        @JsonProperty(required = true)
        LocalDateTime timeStamp
) {
}