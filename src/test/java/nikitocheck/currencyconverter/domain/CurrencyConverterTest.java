package nikitocheck.currencyconverter.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.BDDMockito.*;

class CurrencyConverterTest {

    BigDecimal amount = new BigDecimal("10.3");
    BigDecimal rate = new BigDecimal("11.3");
    String sourceCurrency = "EUR";
    String targetCurrency = "USD";

    RatesRepository mockedRatesRepository = mock(RatesRepository.class);
    SymbolsRepository mockedSymbolsRepository = mock(SymbolsRepository.class);
    CurrencyConverter converter = new CurrencyConverter(mockedRatesRepository, mockedSymbolsRepository);

    @Test
    public void givenSourceCurrencyNotExistThenShouldFail() {
        given(mockedSymbolsRepository.getSymbols()).willReturn(Map.of());

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> converter.convert(amount, sourceCurrency, targetCurrency)
        );
    }

    @Test
    public void givenTargetCurrencyNotExistThenShouldFail() {
        given(mockedSymbolsRepository.getSymbols()).willReturn(Map.of(sourceCurrency, "Euro"));

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> converter.convert(amount, sourceCurrency, targetCurrency)
        );
    }

    @Test
    public void givenCurrenciesExistThenShouldCallRates() {
        //given
        given(mockedSymbolsRepository.getSymbols()).willReturn(Map.of(
                sourceCurrency, "Euro",
                targetCurrency, "United states dollar"
        ));
        given(mockedRatesRepository.getRate(any(), any())).willReturn(rate);
        //when
        var actualResult = converter.convert(amount, sourceCurrency, targetCurrency);
        //then
        then(mockedRatesRepository).should().getRate(sourceCurrency, targetCurrency);
        Assertions.assertEquals(amount.multiply(rate), actualResult);
    }


}