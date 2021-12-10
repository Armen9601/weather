package com.example.weather.endpoint;

import com.example.weather.dto.WeatherDto;
import com.example.weather.service.serviceImpl.WeatherServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
public class WeatherEndpoint {

    private final WeatherServiceImpl weatherService;


    @GetMapping()
    public WeatherDto getFromYandex() {

        return weatherService.getByDate(LocalDate.now()) == null ? weatherService.add()
                : weatherService.getByDate(LocalDate.now());

    }

}
