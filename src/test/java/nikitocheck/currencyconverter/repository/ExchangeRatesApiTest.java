package nikitocheck.currencyconverter.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class ExchangeRatesApiTest {

    private final RestTemplate mockedTemplate = mock(RestTemplate.class);
    private final String apiUrl = "api.com/symbols";
    private final String token = "token-sample";

    private final ExchangeRatesApi exchangeRatesApi = new ExchangeRatesApi(mockedTemplate, apiUrl, token);

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDoGetRatesRequest() {
        //given
        var from = "USD";
        var to = "EUR";
        var rates = new BigDecimal("18.3");
        var response = new ExchangeRatesResponseBody(from, Map.of(to, rates));

        given(mockedTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(ExchangeRatesResponseBody.class), any(Map.class)))
                .willReturn(ResponseEntity.of(Optional.of(response)));

        //when
        var actual = exchangeRatesApi.getRate(from, to);
        //then
        then(mockedTemplate).should().exchange(
                eq(apiUrl + "/latest?base={from}&symbols={to}"),
                eq(HttpMethod.GET),
                argThat(arg -> Objects.requireNonNull(arg.getHeaders().get("apikey")).get(0).equals(token)),
                eq(ExchangeRatesResponseBody.class),
                (Map<String, ?>) argThat(params ->
                        params instanceof Map<?, ?>
                                && ((Map<?, ?>) params).get("from").equals(from)
                                && ((Map<?, ?>) params).get("to").equals(to))
        );

        Assertions.assertEquals(rates, actual);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldDoGetSymbolsRequest() {
        //given
        final var symbolsResponse = new SymbolsResponseBody(true, Map.of(
                "USD", "United states dollar",
                "EUR", "Euro"
        ));

        given(mockedTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(SymbolsResponseBody.class), any(Map.class)))
                .willReturn(ResponseEntity.of(Optional.of(symbolsResponse)));

        //when
        var actual = exchangeRatesApi.getSymbols();
        //then
        then(mockedTemplate).should().exchange(
                eq(apiUrl + "/symbols"),
                eq(HttpMethod.GET),
                argThat(arg -> Objects.requireNonNull(arg.getHeaders().get("apikey")).get(0).equals(token)),
                eq(SymbolsResponseBody.class),
                eq(Map.of())
        );
        Assertions.assertEquals(symbolsResponse.symbols(), actual);
    }
}