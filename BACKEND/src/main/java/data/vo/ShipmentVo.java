package data.vo;

import data.model.Shipment;
import java.text.SimpleDateFormat;

public class ShipmentVo {

    private Integer id;
    private String product_code;
    private Integer quantity;
    private String dest_city;
    private Double dest_lat;
    private Double dest_lng;
    private String dispatch_date;
    private String status;
    private String risk_level;
    private Integer is_deleted;
    private Double precip_mm;
    private Integer weather_code;
    private String fetched_at;

    public ShipmentVo() {}

    public ShipmentVo(Shipment shipment) {
        this.id = shipment.getId();
        this.product_code = shipment.getProduct_code();
        this.quantity = shipment.getQuantity();
        this.dest_city = shipment.getDest_city();
        this.dest_lat = shipment.getDest_lat();
        this.dest_lng = shipment.getDest_lng();
        if (shipment.getDispatch_date() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.dispatch_date = sdf.format(shipment.getDispatch_date());
        }
        this.status = shipment.getStatus();
        this.risk_level = shipment.getRisk_level();
        this.is_deleted = shipment.getIs_deleted();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getDest_city() {
        return dest_city;
    }

    public void setDest_city(String dest_city) {
        this.dest_city = dest_city;
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

    public String getDispatch_date() {
        return dispatch_date;
    }

    public void setDispatch_date(String dispatch_date) {
        this.dispatch_date = dispatch_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRisk_level() {
        return risk_level;
    }

    public void setRisk_level(String risk_level) {
        this.risk_level = risk_level;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
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

    public String getFetched_at() {
        return fetched_at;
    }

    public void setFetched_at(String fetched_at) {
        this.fetched_at = fetched_at;
    }
}
