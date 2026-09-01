package data.service;

import data.response.DtoResponse;
import data.vo.ShipmentVo;

public interface ShipmentService {
    DtoResponse getAllShipments();
    DtoResponse getShipmentsByStatus(String status);
    DtoResponse getShipmentById(Integer id);
    DtoResponse addShipment(ShipmentVo shipmentVo);
    DtoResponse editShipment(ShipmentVo shipmentVo);
    DtoResponse deleteShipment(Integer id);
    DtoResponse refreshWeather(Integer id);
}
