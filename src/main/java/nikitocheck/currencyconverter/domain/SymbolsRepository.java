package nikitocheck.currencyconverter.domain;

import java.util.Map;

public interface SymbolsRepository {
    Map<String, String> getSymbols();
}
