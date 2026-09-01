package data.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "product_code", nullable = false, length = 50)
    private String product_code;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "dest_city", nullable = false, length = 100)
    private String dest_city;

    @Column(name = "dest_lat", nullable = false)
    private Double dest_lat;

    @Column(name = "dest_lng", nullable = false)
    private Double dest_lng;

    @Temporal(TemporalType.DATE)
    @Column(name = "dispatch_date", nullable = false)
    private Date dispatch_date;

    @Column(name = "status", length = 20)
    private String status = "PLANNED";

    @Column(name = "risk_level", length = 10)
    private String risk_level;

    @Column(name = "is_deleted")
    private Integer is_deleted = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date created_at;

    public Shipment() {}

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

    public Date getDispatch_date() {
        return dispatch_date;
    }

    public void setDispatch_date(Date dispatch_date) {
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

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
