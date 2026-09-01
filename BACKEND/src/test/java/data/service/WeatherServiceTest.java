package data.service;

import data.model.WeatherCache;
import data.repository.WeatherCacheRepository;
import data.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    private WeatherCacheRepository weatherCacheRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void testRiskScoringRules() {
        assertEquals("LOW", weatherService.calculateRiskLevel(0.0));
        assertEquals("MEDIUM", weatherService.calculateRiskLevel(3.5));
        assertEquals("MEDIUM", weatherService.calculateRiskLevel(10.0));
        assertEquals("HIGH", weatherService.calculateRiskLevel(15.2));
        assertEquals("UNKNOWN", weatherService.calculateRiskLevel(null));
    }

    @Test
    public void testFreshCacheHit() {
        Double lat = -6.2088;
        Double lng = 106.8456;
        Date date = new Date();

        WeatherCache cached = new WeatherCache();
        cached.setDest_lat(lat);
        cached.setDest_lng(lng);
        cached.setForecast_date(date);
        cached.setPrecip_mm(0.0);
        cached.setWeather_code(0);
        cached.setFetched_at(new Date());

        when(weatherCacheRepository.findFirstByLocationAndDate(anyDouble(), anyDouble(), any())).thenReturn(Optional.of(cached));

        WeatherService.RiskResult result = weatherService.getWeatherRisk(lat, lng, date, false);

        assertEquals("LOW", result.getRiskLevel());
        assertEquals(0.0, result.getPrecipMm());
        verify(weatherCacheRepository, times(1)).findFirstByLocationAndDate(anyDouble(), anyDouble(), any());
    }

    @Test
    public void testCacheFallbackWhenApiDown() {
        Double lat = -6.2088;
        Double lng = 106.8456;
        Date date = new Date();

        WeatherCache staleCache = new WeatherCache();
        staleCache.setDest_lat(lat);
        staleCache.setDest_lng(lng);
        staleCache.setForecast_date(date);
        staleCache.setPrecip_mm(12.5);
        staleCache.setWeather_code(80);
        staleCache.setFetched_at(new Date(System.currentTimeMillis() - (10 * 60 * 60 * 1000L)));

        when(weatherCacheRepository.findFirstByLocationAndDate(anyDouble(), anyDouble(), any())).thenReturn(Optional.of(staleCache));

        WeatherService.RiskResult result = weatherService.getWeatherRisk(lat, lng, date, true);

        assertEquals("HIGH", result.getRiskLevel());
        assertEquals(12.5, result.getPrecipMm());
    }
}
