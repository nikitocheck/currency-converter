package nikitocheck.currencyconverter.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConverter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RatesRepository ratesRepository;
    private final SymbolsRepository symbolsRepository;

    public CurrencyConverter(RatesRepository ratesRepository, SymbolsRepository symbolsRepository) {
        this.ratesRepository = ratesRepository;
        this.symbolsRepository = symbolsRepository;
    }


    public BigDecimal convert(BigDecimal sourceAmount, String sourceCurrency, String targetCurrency) {
        logger.debug("Converting currency amount={}, source={}, target={}", sourceAmount, sourceCurrency, targetCurrency);
        var symbols = symbolsRepository.getSymbols();
        checkCurrencyExists(symbols, sourceCurrency);
        checkCurrencyExists(symbols, targetCurrency);
        var rate = ratesRepository.getRate(sourceCurrency, targetCurrency);
        logger.debug("Rate for {} to {} is {}", sourceCurrency, targetCurrency, rate.toPlainString());
        return rate.multiply(sourceAmount);
    }

    private void checkCurrencyExists(Map<String, String> symbols, String currency) {
        if (!symbols.containsKey(currency)) {
            throw new IllegalArgumentException("Currency %s is not available for conversion".formatted(currency));
        }
    }
}
