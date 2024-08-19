package telran.currency.service;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.json.JSONObject;

public class FixerApiPerDay extends AbstractCurrencyConvertor {
	protected String uriString = "http://data.fixer.io/api/latest?access_key=c6221a4dafe03eb64d7064ff651d65bd";
	HttpClient httpClient;
	HttpRequest request;
	HashMap<String, Double> map;
	Instant time;
	JSONObject jsonObject;

	public FixerApiPerDay() throws Exception {
		getJSON();
		rates = getRates();
		time = getTime();
	}

	private Instant getTime() {
		return Instant.ofEpochMilli(jsonObject.getInt("timestamp"));
	}

	protected HashMap<String, Double> getRates() throws Exception {
		JSONObject jsonRates = jsonObject.getJSONObject("rates");
		return jsonRates.keySet().stream().collect(Collectors.toMap(key -> key, key -> jsonRates.getDouble(key),
				(existing, replacement) -> existing, HashMap::new));
	}

	private void getJSON() throws Exception {
		this.httpClient = HttpClient.newHttpClient();
		this.request = HttpRequest.newBuilder(new URI(uriString)).build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		jsonObject = new JSONObject(response.body());
		
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
		if (time == null || time.isBefore(Instant.now().minus(Duration.ofHours(24)))) {
			try {
				map = getRates();
				time = getTime();
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}

		}
	}

}
