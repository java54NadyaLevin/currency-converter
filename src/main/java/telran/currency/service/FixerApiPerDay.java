package telran.currency.service;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class FixerApiPerDay extends AbstractCurrencyConvertor {
	protected String uriString = "http://data.fixer.io/api/latest?access_key=c6221a4dafe03eb64d7064ff651d65bd";
	HttpClient httpClient;
	HttpRequest request;
	HashMap<String, Double> map;
	LocalDateTime time;

	public FixerApiPerDay() throws Exception {
		this.httpClient = HttpClient.newHttpClient();
		this.request = HttpRequest.newBuilder(new URI(uriString)).build();
		rates = getRates();
	}

	protected HashMap<String, Double> getRates() throws Exception {
		HttpResponse<String> response =
				httpClient.send(request, BodyHandlers.ofString());
		JSONObject jsonObject = new JSONObject(response.body());
		JSONObject jsonRates = jsonObject.getJSONObject("rates");
		
		return jsonRates.keySet().stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> jsonRates.getDouble(key),
                        (existing, replacement) -> existing, 
                        HashMap::new 
                ));
	}

	@Override
	public List<String> strongestCurrencies(int amount) {
		refresh();
		return super.strongestCurrencies(amount);
	}

	@Override
	public List<String> weakestCurrencies(int amount) {
		refresh();
		return super.weakestCurrencies(amount);
	}

	@Override
	public double convert(String codeFrom, String codeTo, int amount) {
		refresh();
		return super.convert(codeFrom, codeTo, amount);
	}

	private void refresh() {
		if(time == null || time.isBefore(LocalDateTime.now().minusHours(24))) {
		try {
			map = getRates();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		time = LocalDateTime.now();
		}
	}

}
