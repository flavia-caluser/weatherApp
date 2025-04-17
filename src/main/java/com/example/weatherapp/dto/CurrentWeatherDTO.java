package com.example.weatherapp.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CurrentWeatherDTO {
    private double temperature;

    private double feelsLikeTemperature;

    private double humidity;

    private LocalDateTime date;

    public CurrentWeatherDTO(double temperature, double feelsLikeTemperature, double humidity, LocalDateTime date) {
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.humidity = humidity;
        this.date = date;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(double feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
