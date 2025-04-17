package com.example.weatherapp.controller;

import com.example.weatherapp.dto.CurrentWeatherDTO;
import com.example.weatherapp.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private WeatherService weatherService;


@Autowired
    public WeatherController(WeatherService weatherService) {
    this.weatherService = weatherService;
    }
@GetMapping("/current")
    public ResponseEntity<CurrentWeatherDTO> getCurrentWeather(@RequestParam double lat, @RequestParam  double lon ) {
    try {
        return ResponseEntity.ok(weatherService.getCurrentWeather(lat,lon));
    } catch (JsonProcessingException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
}

    @GetMapping("/forecast")
    public ResponseEntity<List<CurrentWeatherDTO>> getWeatherForecast (@RequestParam  double lat, @RequestParam double lon, @RequestParam String timesteps){
        try {
            return ResponseEntity.ok(weatherService.getForecastWeather(lat,lon,timesteps));
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
