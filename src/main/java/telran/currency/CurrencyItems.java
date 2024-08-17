package telran.currency;

import java.util.*;

import telran.currency.service.CurrencyConvertor;

import telran.view.*;


public class CurrencyItems {
private static CurrencyConvertor currencyConvertor;
private static HashSet<String> currencies;

public static List<Item> getItems(CurrencyConvertor currencyConvertor, HashSet<String> currencies) {
	CurrencyItems.currencyConvertor = currencyConvertor;
	CurrencyItems.currencies = currencies;
	
	
	Item[] items = {
			Item.of("Show 10 strongest currencies", CurrencyItems::getStrongestCurrencies),
			Item.of("Show 10 weakest currencies", CurrencyItems::getWeakestCurrencies),
			Item.of("Convert currencies", CurrencyItems::convert),
	};
			
	return new ArrayList<>(List.of(items));
}
	
	private static void getStrongestCurrencies(InputOutput io) {
		getCurrencies(io, true);
	}
	
	private static void getWeakestCurrencies(InputOutput io) {
		getCurrencies(io, false);
	}

	private static void getCurrencies(InputOutput io, boolean getStrongest) {
		List<String> currencies = getStrongest ? currencyConvertor.strongestCurrencies(10) : currencyConvertor.weakestCurrencies(10);
		for(String currency:currencies) {
		io.writeLine(currency);
		}
	}
	
	private static void convert(InputOutput io) {
		String from = io.readStringOptions("From currency ", "Wrong currency", currencies);
		String to = io.readStringOptions("To currency ", "Wrong currency", currencies);
		int amount = io.readNumberRange("Amount to be converted", "Wrong amount", 1, Integer.MAX_VALUE).intValue();
		io.writeLine(currencyConvertor.convert(from, to, amount));
	}
}
