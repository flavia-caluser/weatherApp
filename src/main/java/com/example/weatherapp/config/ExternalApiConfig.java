package com.example.weatherapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@Configuration
public class ExternalApiConfig {
    @Bean
    //construieste un obict de tip rest template builder cu anumite configurari de baza
            //pentru ca noi sa facem call spre api extern
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
    //un obiect care il construieac din libraria de jakspon atunci cand
    //atributele din json bat cu atributele de care am eu nevoie
    //temeperatureFeelsLike, temperature, humidity, data
}
