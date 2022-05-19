package nikitocheck.currencyconverter.controller;

import nikitocheck.currencyconverter.controller.data.CurrencyExchangeErrorResponse;
import nikitocheck.currencyconverter.controller.data.CurrencyExchangeRequestBody;
import nikitocheck.currencyconverter.controller.data.CurrencyExchangeResponseBody;
import nikitocheck.currencyconverter.domain.CurrencyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("converter")
public class CurrencyConverterController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CurrencyConverter converter;

    public CurrencyConverterController(CurrencyConverter converter) {
        this.converter = converter;
    }

    @PostMapping("/exchange/queries")
    public CurrencyExchangeResponseBody calculateExchange(@RequestBody CurrencyExchangeRequestBody requestBody) {
        logger.info("Start processing exchange query");
        var stopWatch = new StopWatch();
        stopWatch.start();

        var targetValue = converter.convert(requestBody.amount(), requestBody.source(), requestBody.target());

        stopWatch.stop();

        return new CurrencyExchangeResponseBody(
                requestBody.source(),
                requestBody.target(),
                requestBody.amount(),
                targetValue,
                stopWatch.getLastTaskTimeMillis()
        );
    }

    @ExceptionHandler
    public ResponseEntity<CurrencyExchangeErrorResponse> handleExceptions(Throwable error) {
        logger.error(error.getMessage(), error);

        var status = HttpStatus.INTERNAL_SERVER_ERROR;
        var message = error.getMessage();

        if (error instanceof IllegalArgumentException || error instanceof HttpMessageNotReadableException) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(new CurrencyExchangeErrorResponse(
                message,
                LocalDateTime.now()
        ));
    }
}
