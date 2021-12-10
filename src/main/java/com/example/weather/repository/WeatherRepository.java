package com.example.weather.repository;

import com.example.weather.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WeatherRepository extends JpaRepository<Weather, Integer> {

    Weather findByWeatherDate(LocalDate localDate);

}
