package nikitocheck.currencyconverter.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Map;

@JsonSerialize
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRatesResponseBody(
        String base,
        Map<String,BigDecimal> rates
){}