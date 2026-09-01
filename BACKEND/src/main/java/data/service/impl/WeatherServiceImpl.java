package data.service.impl;

import data.model.WeatherCache;
import data.repository.WeatherCacheRepository;
import data.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherCacheRepository weatherCacheRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast?latitude={lat}&longitude={lng}&daily=precipitation_sum,weather_code&timezone=auto";
    private static final long CACHE_EXPIRATION_MS = 6 * 60 * 60 * 1000L;

    @Override
    public String calculateRiskLevel(Double precipMm) {
        if (precipMm == null) {
            return "UNKNOWN";
        }
        if (precipMm == 0.0) {
            return "LOW";
        } else if (precipMm >= 1.0 && precipMm <= 10.0) {
            return "MEDIUM";
        } else if (precipMm > 10.0) {
            return "HIGH";
        }
        return "LOW";
    }

    @Override
    public RiskResult getWeatherRisk(Double lat, Double lng, Date dispatchDate, boolean forceRefresh) {
        if (lat == null || lng == null || dispatchDate == null) {
            return new RiskResult("UNKNOWN", null, null, null);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetDateStr = sdf.format(dispatchDate);

        Optional<WeatherCache> cachedOpt = weatherCacheRepository.findFirstByLocationAndDate(lat, lng, dispatchDate);

        if (!forceRefresh && cachedOpt.isPresent()) {
            WeatherCache cached = cachedOpt.get();
            long now = System.currentTimeMillis();
            long fetchedTime = cached.getFetched_at() != null ? cached.getFetched_at().getTime() : 0;

            if ((now - fetchedTime) < CACHE_EXPIRATION_MS) {
                String risk = calculateRiskLevel(cached.getPrecip_mm());
                return new RiskResult(risk, cached.getPrecip_mm(), cached.getWeather_code(), cached.getFetched_at());
            }
        }

        try {
            Map<String, Object> uriVariables = new HashMap<>();
            uriVariables.put("lat", lat);
            uriVariables.put("lng", lng);

            Map<?, ?> response = restTemplate.getForObject(OPEN_METEO_URL, Map.class, uriVariables);

            if (response != null && response.containsKey("daily")) {
                Map<?, ?> daily = (Map<?, ?>) response.get("daily");
                List<?> timeList = (List<?>) daily.get("time");
                List<?> precipList = (List<?>) daily.get("precipitation_sum");
                List<?> codeList = (List<?>) daily.get("weather_code");

                Double targetPrecip = null;
                Integer targetCode = null;

                if (timeList != null && precipList != null) {
                    int matchedIndex = -1;
                    for (int i = 0; i < timeList.size(); i++) {
                        if (targetDateStr.equals(String.valueOf(timeList.get(i)))) {
                            matchedIndex = i;
                            break;
                        }
                    }
                    if (matchedIndex == -1 && !precipList.isEmpty()) {
                        matchedIndex = 0;
                    }

                    if (matchedIndex != -1 && matchedIndex < precipList.size()) {
                        Object precipObj = precipList.get(matchedIndex);
                        if (precipObj instanceof Number) {
                            targetPrecip = ((Number) precipObj).doubleValue();
                        }
                    }
                    if (codeList != null && matchedIndex != -1 && matchedIndex < codeList.size()) {
                        Object codeObj = codeList.get(matchedIndex);
                        if (codeObj instanceof Number) {
                            targetCode = ((Number) codeObj).intValue();
                        }
                    }
                }

                Date now = new Date();
                WeatherCache cacheEntry = cachedOpt.orElse(new WeatherCache());
                cacheEntry.setDest_lat(lat);
                cacheEntry.setDest_lng(lng);
                cacheEntry.setForecast_date(dispatchDate);
                cacheEntry.setPrecip_mm(targetPrecip);
                cacheEntry.setWeather_code(targetCode);
                cacheEntry.setFetched_at(now);

                weatherCacheRepository.save(cacheEntry);

                String risk = calculateRiskLevel(targetPrecip);
                return new RiskResult(risk, targetPrecip, targetCode, now);
            }
        } catch (Exception e) {
        }

        if (cachedOpt.isPresent()) {
            WeatherCache cached = cachedOpt.get();
            String risk = calculateRiskLevel(cached.getPrecip_mm());
            return new RiskResult(risk, cached.getPrecip_mm(), cached.getWeather_code(), cached.getFetched_at());
        }

        return new RiskResult("UNKNOWN", null, null, null);
    }

    @Scheduled(fixedRate = 21600000)
    public void refreshAllCachedForecasts() {
        try {
            List<WeatherCache> allCaches = weatherCacheRepository.findAll();
            for (WeatherCache cache : allCaches) {
                getWeatherRisk(cache.getDest_lat(), cache.getDest_lng(), cache.getForecast_date(), true);
            }
        } catch (Exception e) {
        }
    }
}
