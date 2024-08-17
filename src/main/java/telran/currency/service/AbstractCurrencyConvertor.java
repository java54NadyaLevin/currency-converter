package telran.currency.service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractCurrencyConvertor implements CurrencyConvertor {
	protected Map<String, Double> rates;

	@Override
	public List<String> strongestCurrencies(int amount) {
		return setOfCurrencies(amount, true);
	}

	@Override
	public List<String> weakestCurrencies(int amount) {
		return setOfCurrencies(amount, false);
	}

	private List<String> setOfCurrencies(int amount, boolean inNaturalOrder) {
		return rates.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue(inNaturalOrder ? Comparator.naturalOrder() : Comparator.reverseOrder()))
				.limit(amount)
				.map(Map.Entry::getKey).collect(Collectors.toList());
	}

	@Override
	public double convert(String codeFrom, String codeTo, int amount) {
		double result = (amount*rates.get(codeTo))/rates.get(codeFrom);
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		return Double.parseDouble(decimalFormat.format(result));
	}

	@Override
	public HashSet<String> getAllCodes() {
		return rates.keySet().stream().collect(Collectors.toCollection(HashSet::new));
	}

}
