package com.example.weather.service;

import com.example.weather.dto.WeatherDto;

import java.time.LocalDate;

public interface WeatherService {

    WeatherDto getByDate(LocalDate localDate);

    WeatherDto add();

}
