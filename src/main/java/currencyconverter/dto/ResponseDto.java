package currencyconverter.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class ResponseDto {
    Map<String, Double> rates;

}
