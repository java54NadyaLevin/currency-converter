package telran.currency;


import java.util.*;

import telran.currency.service.FixerApiPerDay;
import telran.view.*;

public class CurrencyConvertorAppl {

	public static void main(String[] args) throws Exception {
		FixerApiPerDay currencyConvertor = new FixerApiPerDay();
		List<Item> items = CurrencyItems.getItems(currencyConvertor,
				currencyConvertor.getAllCodes());
		items.add(Item.of("Exit & connection close", io -> {}, true));
		Menu menu = new Menu("Currencies convertor", items.toArray(Item[]::new));
		menu.perform(new SystemInputOutput());
	}

}
