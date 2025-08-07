package org.alura.challenges;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

class ApiClient {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String API_KEY = dotenv.get("API_KEY");
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static double convertCurrency(String fromCurrency, String toCurrency, double value) throws IOException {
        // 1. Construir URL dinámica con fromCurrency
        String apiUrl = API_BASE_URL + API_KEY + "/latest/" + fromCurrency;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // 2. Leer respuesta JSON
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // 3. Parsear JSON y obtener tasa de cambio
        Gson gson = new Gson();
        ExchangeRateResponse apiResponse = gson.fromJson(response.toString(), ExchangeRateResponse.class);
        double rate = apiResponse.getConversionRate(toCurrency);

        // 4. Calcular valor convertido
        return value * rate;
    }

    // Clase interna para mapear la respuesta JSON
    private static class ExchangeRateResponse {
        private String base_code;
        private java.util.Map<String, Double> conversion_rates;

        public double getConversionRate(String currency) {
            if (conversion_rates == null || !conversion_rates.containsKey(currency)) {
                throw new IllegalArgumentException("Divisa no soportada: " + currency);
            }
            return conversion_rates.get(currency);
        }
    }
}