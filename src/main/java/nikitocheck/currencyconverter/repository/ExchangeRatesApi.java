package nikitocheck.currencyconverter.repository;

import nikitocheck.currencyconverter.domain.RatesRepository;
import nikitocheck.currencyconverter.domain.SymbolsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Service
public class ExchangeRatesApi implements RatesRepository, SymbolsRepository {

    private static final String RATES_PATH = "/latest?base={from}&symbols={to}";
    private static final String SYMBOLS_PATH = "/symbols";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    private final String apiToken;
    private final String ratesUrl;
    private final String symbolsUrl;

    public ExchangeRatesApi(
            RestTemplate restTemplate,
            @Value("${exchange.rates.api.url}")
            String apiUrl,
            @Value("${exchange.rates.api.token}")
            String apiToken
    ) {
        this.restTemplate = restTemplate;
        this.ratesUrl = apiUrl + RATES_PATH;
        this.symbolsUrl = apiUrl + SYMBOLS_PATH;
        this.apiToken = apiToken;
    }


    @Override
    public BigDecimal getRate(String source, String target) {

        final var params = Map.of(
                "from", source,
                "to", target
        );

        final var response = doRequest(
                ratesUrl,
                params,
                ExchangeRatesResponseBody.class);
        final var body = Objects.requireNonNull(response.getBody());
        logger.debug("Conversion finished. Response={}", body);
        return body.rates().get(target);
    }

    @Override
    public Map<String, String> getSymbols() {

        final var response = doRequest(
                symbolsUrl,
                Map.of(),
                SymbolsResponseBody.class);
        final var body = Objects.requireNonNull(response.getBody());
        logger.debug("Symbols loaded. Response={}", body);
        return body.symbols();

    }

    private <T> ResponseEntity<T> doRequest(String url, Map<String, String> params, Class<T> clazz) {
        final var headers = new HttpHeaders();
        headers.add("apikey", apiToken);
        logger.debug("Performing request. Headers={}; Params={}; Url={}", headers, params, ratesUrl);
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                clazz,
                params);
    }
}
