package com.ing.tech.bank.model.entities;

import com.ing.tech.bank.exceptions.ExchangeRateException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ExchangeRate {

    public static double getRate(String baseCurrency, String receiverCurrency) {
        if (baseCurrency.equals(receiverCurrency)) return 1d;

        try {
            String requestUrl = "https://v6.exchangerate-api.com/v6/";
            String apiKey = "e629f99e831a2061c8dba256";
            URL url = new URL(requestUrl + apiKey + "/latest/" + baseCurrency);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            return parseResponse(connection, receiverCurrency);
        } catch (Exception ex) {
            throw new ExchangeRateException(ex.getMessage());
        }
    }

    private static double parseResponse(HttpsURLConnection connection, String currency) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject response = (JSONObject) parser.parse(new InputStreamReader((InputStream) connection.getContent()));
        JSONObject allRates = (JSONObject) response.get("conversion_rates");

        return (double) allRates.get(currency);
    }
}
