package data.service;

import data.model.WeatherCache;
import java.util.Date;

public interface WeatherService {
    
    class RiskResult {
        private String riskLevel;
        private Double precipMm;
        private Integer weatherCode;
        private Date fetchedAt;

        public RiskResult(String riskLevel, Double precipMm, Integer weatherCode, Date fetchedAt) {
            this.riskLevel = riskLevel;
            this.precipMm = precipMm;
            this.weatherCode = weatherCode;
            this.fetchedAt = fetchedAt;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public Double getPrecipMm() {
            return precipMm;
        }

        public Integer getWeatherCode() {
            return weatherCode;
        }

        public Date getFetchedAt() {
            return fetchedAt;
        }
    }

    RiskResult getWeatherRisk(Double lat, Double lng, Date dispatchDate, boolean forceRefresh);
    
    String calculateRiskLevel(Double precipMm);
}
