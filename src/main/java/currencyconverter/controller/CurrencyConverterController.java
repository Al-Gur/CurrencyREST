package currencyconverter.controller;

import currencyconverter.dto.AmountDto;
import currencyconverter.dto.AmountTargetCurrencyDto;
import currencyconverter.dto.ResponseDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
public class CurrencyConverterController {
    RestTemplate restTemplate = new RestTemplate();
    final String basicUrl = "http://data.fixer.io/api/";
    final String API_Access_Key = "59429518bef1b95f7dbe2fa2ad74b5ba";
    final String accessUrl = "access_key=" + API_Access_Key;

    private Double getRate(String currency) throws URISyntaxException {
        String url = basicUrl + "latest?" + accessUrl + "&symbols=" + currency;
        //System.out.println(url);
        RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, new URI(url));
        ResponseEntity<ResponseDto> response = restTemplate.exchange(request, ResponseDto.class);
        //System.out.println(response);
        ResponseDto responseDto = response.getBody();
        if (responseDto != null) {
            Map<String, Double> rates = responseDto.getRates();
            //System.out.println(rates);
            return rates.get(currency);
        } else {
            return null;
        }
    }

    private String conversion(Double amount, String fromCurrency, String toCurrency) throws URISyntaxException {
        if (amount == null || fromCurrency == null || toCurrency == null) {
            return "Error!";
        }
        Double fromCurrencyRate = getRate(fromCurrency);
        Double toCurrencyRate = getRate(toCurrency);
        if (fromCurrencyRate == null || fromCurrencyRate == 0 || toCurrencyRate == null) {
            return "Error!!";
        }
        return Double.valueOf(amount * toCurrencyRate / fromCurrencyRate).toString();
    }

    @PostMapping("/convert-usd-ils")
    public String convertUsdIls(@RequestBody AmountDto amount) throws URISyntaxException {
        if (amount == null) {
            return "Error!!";
        }
        return conversion(amount.getAmount(), "USD", "ILS");
    }

    @PostMapping("/convert")
    public String convert(@RequestBody AmountTargetCurrencyDto requestData) throws URISyntaxException {
        if (requestData == null) {
            return "Error!!!";
        }
        return conversion(requestData.getAmount(), "USD", requestData.getCurrency());
    }
}

