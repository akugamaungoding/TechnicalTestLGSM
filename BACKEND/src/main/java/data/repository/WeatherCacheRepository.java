package data.repository;

import data.model.WeatherCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface WeatherCacheRepository extends JpaRepository<WeatherCache, Integer> {

    @Query("SELECT w FROM WeatherCache w WHERE ABS(w.dest_lat - :lat) < 0.01 AND ABS(w.dest_lng - :lng) < 0.01 AND w.forecast_date = :forecastDate ORDER BY w.fetched_at DESC")
    Optional<WeatherCache> findFirstByLocationAndDate(@Param("lat") Double lat, @Param("lng") Double lng, @Param("forecastDate") Date forecastDate);
}
