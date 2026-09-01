package data.rest;

import data.response.DtoResponse;
import data.service.ShipmentService;
import data.vo.ShipmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/shipment")
public class ShipmentRest {

    @Autowired
    private ShipmentService shipmentService;

    public ShipmentRest(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping("/getAllShipments")
    public DtoResponse getAllShipments() {
        return shipmentService.getAllShipments();
    }

    @GetMapping("/getShipmentsByStatus")
    public DtoResponse getShipmentsByStatus(@RequestParam(name = "status", required = false) String status) {
        if (status != null && !status.isEmpty()) {
            return shipmentService.getShipmentsByStatus(status);
        }
        return shipmentService.getAllShipments();
    }

    @GetMapping("/getShipmentById/{id}")
    public DtoResponse getShipmentById(@PathVariable("id") Integer id) {
        return shipmentService.getShipmentById(id);
    }

    @PostMapping("/createShipment")
    public DtoResponse createShipment(@RequestBody ShipmentVo shipmentVo) {
        return shipmentService.addShipment(shipmentVo);
    }

    @PostMapping("/editShipment")
    public DtoResponse editShipment(@RequestBody ShipmentVo shipmentVo) {
        return shipmentService.editShipment(shipmentVo);
    }

    @DeleteMapping("/deleteShipment/{id}")
    public DtoResponse deleteShipment(@PathVariable("id") Integer id) {
        return shipmentService.deleteShipment(id);
    }

    @PostMapping("/refreshWeather/{id}")
    public DtoResponse refreshWeather(@PathVariable("id") Integer id) {
        return shipmentService.refreshWeather(id);
    }
}
