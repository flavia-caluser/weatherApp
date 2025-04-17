package com.example.weatherapp.service;

import com.example.weatherapp.dto.CurrentWeatherDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    //private ObjectMapper objectMapper;
    //construiesc url de baza al request -ului [e care il pun intr o constanta
    private static final String BASE_URL = "https://api.tomorrow.io/v4/weather";


    //imi injectez din app properties intr o variabila key pe care nu vreau sa il
    // dau ca parametru in metoda
    @Value("${weather.api}")
    private String apiKey;


    @Autowired
    public WeatherService(RestTemplate restTemplate, ObjectMapper mapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = mapper;
    }

    public CurrentWeatherDTO getCurrentWeather (double lat, double lon) throws JsonProcessingException {
       // la url de baza adaug locatia lat, lon, apykey dar cheia adaug in apl propert si adaug o constanta
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL+"/realtime")
                .queryParam("location", lat+","+lon)
                .queryParam("apikey", apiKey)
                .toUriString();

        // vom primi ca raspuns un json si sa imi returnez
        String response = restTemplate.getForObject(url,String.class);
        //primesc ca raspuns un JsonNode, practic un arbore care imi returneaza toate datele
        // din site si ma folosesc de o metoda de mapare in care imi returnez printr-un
        // currentweather dto acele atribute care le am definit in service:
        //humidity, temperetureFeelsLike, data,
        JsonNode root = objectMapper.readTree(response);
        return mapFromJsonToCurrentWeatherDTO(root);
    }


    //construiesc un obiect de currentWeatherDto in care imi pun atributele de care am nevois
    // apelend root.path pentru a parcurge arborele
    public CurrentWeatherDTO mapFromJsonToCurrentWeatherDTO(JsonNode root) {
        Double humidity = root.path("data").path("values").path("humidity").asDouble();
        Double temperature = root.path("data").path("values").path("temperature").asDouble();
        Double feelsLikeTemperature = root.path("data").path("values").path("temperatureApparent").asDouble();
        String dateString = root.path("data").path("time").asText();;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime dateTime = LocalDateTime.parse(dateString, dtf);
        return new CurrentWeatherDTO(temperature, feelsLikeTemperature, humidity, dateTime);
    }

    public List<CurrentWeatherDTO> getForecastWeather(double lat, double lon, String timesteps) throws JsonProcessingException {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/forecast")
                .queryParam("location", lat + "," + lon)
                .queryParam("timesteps", timesteps)
                .queryParam("apikey", apiKey)
                .queryParam("untis", "metric")
                .toUriString();
        String response = restTemplate.getForObject(url, String.class);
        JsonNode root = objectMapper.readTree(response);
        return mapFromJsonToForecastWeather(root);
    }
        public List<CurrentWeatherDTO> mapFromJsonToForecastWeather(JsonNode root){
        ArrayNode dailyForecasts = (ArrayNode) root.path("timelines").path("daily");
        List<CurrentWeatherDTO> result = new ArrayList<>();
        for (JsonNode jsonNode: dailyForecasts){
            result.add(mapFromForecastNodeToCurrentWreatherDTO(jsonNode));
        }
        return result;
    }

    public CurrentWeatherDTO mapFromForecastNodeToCurrentWreatherDTO(JsonNode jsonNode){
        Double humidity = jsonNode.path("values").path("humidityAvg").asDouble();
        Double temperature = jsonNode.path("values").path("temperatureAvg").asDouble();
        Double feelsLikeTemperature = jsonNode.path("values").path("temperatureApparentAvg").asDouble();
        String dateTimeString = jsonNode.path("time").asText();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withZone(ZoneOffset.UTC);
        //DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        //                .withZone(ZoneOffset.UTC);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, dtf);
        return new CurrentWeatherDTO(temperature, feelsLikeTemperature, humidity, dateTime);
    }

}
