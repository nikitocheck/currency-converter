package nikitocheck.currencyconverter.controller;

import nikitocheck.currencyconverter.controller.data.CurrencyExchangeRequestBody;
import nikitocheck.currencyconverter.domain.CurrencyConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;


class CurrencyConverterControllerTest {

    private final CurrencyConverter converterMock = mock(CurrencyConverter.class);
    private final CurrencyConverterController controller = new CurrencyConverterController(converterMock);

    @Test
    public void shouldCallConverterAndReturnResponse() {
        //given
        var expectedResult = new BigDecimal("309");
        var amount = new BigDecimal("10.3");
        var request = new CurrencyExchangeRequestBody("USD", "RUB", amount);
        given(converterMock.convert(any(), any(), any())).willReturn(expectedResult);
        //when
        var response = controller.calculateExchange(request);
        //then
        then(converterMock).should().convert(amount, request.source(), request.target());
        Assertions.assertTrue(response.millisecondsElapsed() >= 0);
        Assertions.assertEquals(response.source(), request.source());
        Assertions.assertEquals(response.target(), request.target());
        Assertions.assertEquals(response.sourceAmount(), request.amount());
        Assertions.assertEquals(response.resultAmount(), expectedResult);

    }

}