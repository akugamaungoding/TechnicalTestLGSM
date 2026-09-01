package data.dao;

import data.vo.ShipmentVo;
import java.util.List;

public interface ShipmentDao {
    List<ShipmentVo> getAllShipments();
    List<ShipmentVo> getShipmentsByStatus(String status);
}
