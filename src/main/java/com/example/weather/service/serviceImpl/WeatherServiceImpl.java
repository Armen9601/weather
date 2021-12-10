package com.example.weather.service.serviceImpl;


import com.example.weather.dto.WeatherDto;
import com.example.weather.entity.Weather;
import com.example.weather.repository.WeatherRepository;
import com.example.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final ModelMapper mapper;
    private final WeatherRepository weatherRepository;

    private WeatherDto requestWeatherStates() {
        String apiKey = "4696cadd-788a-4f5c-90ff-966511c86af3";

        try (CloseableHttpClient client = HttpClients.createDefault();) {
            String baseUrl = "https://api.weather.yandex.ru/v1/forecast";
            String lat = "59.93909836";
            String lon = "30.31587601";
            String dayLimit = "1";
            String url = baseUrl + "?lat=" + lat + "&lon=" + lon + "&limit=" + dayLimit;

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("X-Yandex-API-Key", apiKey);
            HttpEntity httpEntity;


            try (CloseableHttpResponse response = client.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    httpEntity = response.getEntity();
                    String responseString = EntityUtils.toString(httpEntity, "UTF-8");
                    JSONObject jsonObject = new JSONObject(responseString);
                    String weatherDate = jsonObject.getString("now_dt");
                    String weatherValue = jsonObject.getJSONObject("yesterday").getString("temp");
                    LocalDate localDate = ZonedDateTime.parse(weatherDate).toLocalDate();
                    return new WeatherDto(localDate, weatherValue);
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return new WeatherDto();
    }

    @Override
    public WeatherDto getByDate(LocalDate localDate) {

        Weather byWeatherDate = weatherRepository.findByWeatherDate(localDate);
        if (byWeatherDate != null) {
            return mapper.map(byWeatherDate, WeatherDto.class);
        }
        return null;

    }

    @Override
    public WeatherDto add() {
        Weather weather = mapper.map(requestWeatherStates(), Weather.class);
        weatherRepository.save(weather);
        return requestWeatherStates();
    }
}
