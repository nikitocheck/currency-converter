package nikitocheck.currencyconverter.domain;

import java.math.BigDecimal;

public interface RatesRepository {
    BigDecimal getRate(String source, String target);
}
