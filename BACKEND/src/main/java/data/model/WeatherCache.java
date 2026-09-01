package data.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weather_cache")
public class WeatherCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dest_lat", nullable = false)
    private Double dest_lat;

    @Column(name = "dest_lng", nullable = false)
    private Double dest_lng;

    @Temporal(TemporalType.DATE)
    @Column(name = "forecast_date", nullable = false)
    private Date forecast_date;

    @Column(name = "precip_mm")
    private Double precip_mm;

    @Column(name = "weather_code")
    private Integer weather_code;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fetched_at")
    private Date fetched_at;

    public WeatherCache() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDest_lat() {
        return dest_lat;
    }

    public void setDest_lat(Double dest_lat) {
        this.dest_lat = dest_lat;
    }

    public Double getDest_lng() {
        return dest_lng;
    }

    public void setDest_lng(Double dest_lng) {
        this.dest_lng = dest_lng;
    }

    public Date getForecast_date() {
        return forecast_date;
    }

    public void setForecast_date(Date forecast_date) {
        this.forecast_date = forecast_date;
    }

    public Double getPrecip_mm() {
        return precip_mm;
    }

    public void setPrecip_mm(Double precip_mm) {
        this.precip_mm = precip_mm;
    }

    public Integer getWeather_code() {
        return weather_code;
    }

    public void setWeather_code(Integer weather_code) {
        this.weather_code = weather_code;
    }

    public Date getFetched_at() {
        return fetched_at;
    }

    public void setFetched_at(Date fetched_at) {
        this.fetched_at = fetched_at;
    }
}
