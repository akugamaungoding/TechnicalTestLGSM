package data.dao.impl;

import data.dao.ShipmentDao;
import data.model.Shipment;
import data.repository.ShipmentRepository;
import data.service.WeatherService;
import data.service.WeatherService.RiskResult;
import data.vo.ShipmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShipmentDaoImpl implements ShipmentDao {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private WeatherService weatherService;

    private ShipmentVo convertToVo(Shipment item) {
        ShipmentVo vo = new ShipmentVo(item);
        if (item.getDest_lat() != null && item.getDest_lng() != null && item.getDispatch_date() != null) {
            RiskResult riskResult = weatherService.getWeatherRisk(item.getDest_lat(), item.getDest_lng(), item.getDispatch_date(), false);
            
            if (riskResult.getRiskLevel() != null && !riskResult.getRiskLevel().equals(item.getRisk_level())) {
                item.setRisk_level(riskResult.getRiskLevel());
                shipmentRepository.save(item);
            }
            
            vo.setRisk_level(riskResult.getRiskLevel());
            vo.setPrecip_mm(riskResult.getPrecipMm());
            vo.setWeather_code(riskResult.getWeatherCode());
            if (riskResult.getFetchedAt() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setFetched_at(sdf.format(riskResult.getFetchedAt()));
            }
        }
        return vo;
    }

    @Override
    public List<ShipmentVo> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAllActive();
        List<ShipmentVo> shipmentVos = new ArrayList<>();
        for (Shipment item : shipments) {
            shipmentVos.add(convertToVo(item));
        }
        return shipmentVos;
    }

    @Override
    public List<ShipmentVo> getShipmentsByStatus(String status) {
        List<Shipment> shipments = shipmentRepository.findByStatusActive(status);
        List<ShipmentVo> shipmentVos = new ArrayList<>();
        for (Shipment item : shipments) {
            shipmentVos.add(convertToVo(item));
        }
        return shipmentVos;
    }
}
