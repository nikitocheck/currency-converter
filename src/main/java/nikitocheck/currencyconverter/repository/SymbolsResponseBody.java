package nikitocheck.currencyconverter.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@JsonSerialize
public record SymbolsResponseBody(
        @JsonProperty
        Boolean success,
        @JsonProperty
        Map<String, String> symbols
) {
}
