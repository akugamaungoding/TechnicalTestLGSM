package data.service.impl;

import data.constant.ShipmentConstant;
import data.dao.ShipmentDao;
import data.model.Shipment;
import data.repository.ShipmentRepository;
import data.response.DtoResponse;
import data.service.ShipmentService;
import data.service.WeatherService;
import data.service.WeatherService.RiskResult;
import data.vo.ShipmentVo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentDao shipmentDao;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private WeatherService weatherService;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public DtoResponse getAllShipments() {
        try {
            List<ShipmentVo> list = shipmentDao.getAllShipments();
            if (list != null && !list.isEmpty()) {
                return new DtoResponse(200, list, ShipmentConstant.mGetSuccess);
            }
            return new DtoResponse(200, list, ShipmentConstant.mEmptyData);
        } catch (Exception e) {
            return new DtoResponse(500, null, e.getMessage());
        }
    }

    @Override
    public DtoResponse getShipmentsByStatus(String status) {
        try {
            List<ShipmentVo> list = shipmentDao.getShipmentsByStatus(status);
            return new DtoResponse(200, list, ShipmentConstant.mGetSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, null, e.getMessage());
        }
    }

    @Override
    public DtoResponse getShipmentById(Integer id) {
        try {
            Shipment shipment = shipmentRepository.findById(id).orElse(null);
            if (shipment == null || (shipment.getIs_deleted() != null && shipment.getIs_deleted() == 1)) {
                return new DtoResponse(404, null, ShipmentConstant.mNotFound);
            }

            ShipmentVo vo = new ShipmentVo(shipment);
            RiskResult riskResult = weatherService.getWeatherRisk(shipment.getDest_lat(), shipment.getDest_lng(), shipment.getDispatch_date(), false);
            vo.setRisk_level(riskResult.getRiskLevel());
            vo.setPrecip_mm(riskResult.getPrecipMm());
            vo.setWeather_code(riskResult.getWeatherCode());
            if (riskResult.getFetchedAt() != null) {
                SimpleDateFormat datetimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setFetched_at(datetimeSdf.format(riskResult.getFetchedAt()));
            }

            return new DtoResponse(200, vo, ShipmentConstant.mGetSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, null, e.getMessage());
        }
    }

    @Override
    public DtoResponse addShipment(ShipmentVo vo) {
        try {
            if (vo.getProduct_code() == null || vo.getProduct_code().trim().isEmpty()) {
                return new DtoResponse(400, null, "Product code is required.");
            }
            if (shipmentRepository.countByProductCodeAndNotId(vo.getProduct_code().trim(), null) > 0) {
                return new DtoResponse(400, null, "Product code already exists.");
            }
            if (vo.getQuantity() == null || vo.getQuantity() <= 0) {
                return new DtoResponse(400, null, "Quantity must be greater than 0.");
            }
            if (vo.getDest_city() == null || vo.getDest_city().trim().isEmpty()) {
                return new DtoResponse(400, null, "Destination city is required.");
            }
            if (vo.getDest_lat() == null || vo.getDest_lng() == null) {
                return new DtoResponse(400, null, "Coordinates (lat/lng) are required.");
            }
            if (vo.getDispatch_date() == null || vo.getDispatch_date().trim().isEmpty()) {
                return new DtoResponse(400, null, "Dispatch date is required.");
            }

            Shipment shipment = new Shipment();
            shipment.setProduct_code(vo.getProduct_code().trim());
            shipment.setQuantity(vo.getQuantity());
            shipment.setDest_city(vo.getDest_city());
            shipment.setDest_lat(vo.getDest_lat());
            shipment.setDest_lng(vo.getDest_lng());
            shipment.setIs_deleted(0);

            Date parsedDate = sdf.parse(vo.getDispatch_date());
            shipment.setDispatch_date(parsedDate);
            shipment.setStatus(vo.getStatus() != null ? vo.getStatus() : "PLANNED");

            RiskResult riskResult = weatherService.getWeatherRisk(vo.getDest_lat(), vo.getDest_lng(), parsedDate, false);
            shipment.setRisk_level(riskResult.getRiskLevel());

            Shipment saved = shipmentRepository.save(shipment);
            ShipmentVo responseVo = new ShipmentVo(saved);
            responseVo.setPrecip_mm(riskResult.getPrecipMm());
            responseVo.setWeather_code(riskResult.getWeatherCode());

            return new DtoResponse(200, responseVo, ShipmentConstant.mCreateSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, vo, ShipmentConstant.mCreateFailed + e.getMessage());
        }
    }

    @Override
    public DtoResponse editShipment(ShipmentVo vo) {
        try {
            if (vo.getId() == null) {
                return new DtoResponse(400, null, "Shipment ID is required for editing.");
            }

            Shipment shipment = shipmentRepository.findById(vo.getId()).orElse(null);
            if (shipment == null || (shipment.getIs_deleted() != null && shipment.getIs_deleted() == 1)) {
                return new DtoResponse(404, null, ShipmentConstant.mNotFound);
            }

            if (vo.getProduct_code() != null) {
                String codeTrimmed = vo.getProduct_code().trim();
                if (shipmentRepository.countByProductCodeAndNotId(codeTrimmed, vo.getId()) > 0) {
                    return new DtoResponse(400, null, "Product code already exists.");
                }
                shipment.setProduct_code(codeTrimmed);
            }
            if (vo.getQuantity() != null) shipment.setQuantity(vo.getQuantity());
            if (vo.getDest_city() != null) shipment.setDest_city(vo.getDest_city());
            if (vo.getDest_lat() != null) shipment.setDest_lat(vo.getDest_lat());
            if (vo.getDest_lng() != null) shipment.setDest_lng(vo.getDest_lng());
            if (vo.getDispatch_date() != null) {
                shipment.setDispatch_date(sdf.parse(vo.getDispatch_date()));
            }
            if (vo.getStatus() != null) shipment.setStatus(vo.getStatus());

            RiskResult riskResult = weatherService.getWeatherRisk(shipment.getDest_lat(), shipment.getDest_lng(), shipment.getDispatch_date(), false);
            shipment.setRisk_level(riskResult.getRiskLevel());

            Shipment updated = shipmentRepository.save(shipment);
            ShipmentVo updatedVo = new ShipmentVo(updated);
            updatedVo.setPrecip_mm(riskResult.getPrecipMm());
            updatedVo.setWeather_code(riskResult.getWeatherCode());

            return new DtoResponse(200, updatedVo, ShipmentConstant.mUpdateSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, null, ShipmentConstant.mUpdateFailed + e.getMessage());
        }
    }

    @Override
    public DtoResponse deleteShipment(Integer id) {
        try {
            Shipment shipment = shipmentRepository.findById(id).orElse(null);
            if (shipment == null || (shipment.getIs_deleted() != null && shipment.getIs_deleted() == 1)) {
                return new DtoResponse(404, null, ShipmentConstant.mNotFound);
            }
            shipment.setIs_deleted(1);
            shipmentRepository.save(shipment);
            return new DtoResponse(200, null, ShipmentConstant.mDeleteSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, null, ShipmentConstant.mDeleteFailed + e.getMessage());
        }
    }

    @Override
    public DtoResponse refreshWeather(Integer id) {
        try {
            Shipment shipment = shipmentRepository.findById(id).orElse(null);
            if (shipment == null || (shipment.getIs_deleted() != null && shipment.getIs_deleted() == 1)) {
                return new DtoResponse(404, null, ShipmentConstant.mNotFound);
            }

            RiskResult riskResult = weatherService.getWeatherRisk(shipment.getDest_lat(), shipment.getDest_lng(), shipment.getDispatch_date(), true);
            shipment.setRisk_level(riskResult.getRiskLevel());
            shipmentRepository.save(shipment);

            ShipmentVo vo = new ShipmentVo(shipment);
            vo.setPrecip_mm(riskResult.getPrecipMm());
            vo.setWeather_code(riskResult.getWeatherCode());
            if (riskResult.getFetchedAt() != null) {
                SimpleDateFormat datetimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setFetched_at(datetimeSdf.format(riskResult.getFetchedAt()));
            }

            return new DtoResponse(200, vo, ShipmentConstant.mRefreshSuccess);
        } catch (Exception e) {
            return new DtoResponse(500, null, e.getMessage());
        }
    }
}
