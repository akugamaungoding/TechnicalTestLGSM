package data.repository;

import data.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    @Query("SELECT s FROM Shipment s WHERE (s.is_deleted = 0 OR s.is_deleted IS NULL)")
    List<Shipment> findAllActive();

    @Query("SELECT s FROM Shipment s WHERE s.status = :status AND (s.is_deleted = 0 OR s.is_deleted IS NULL)")
    List<Shipment> findByStatusActive(@Param("status") String status);

    @Query("SELECT COUNT(s) FROM Shipment s WHERE UPPER(s.product_code) = UPPER(:productCode) AND (:id IS NULL OR s.id <> :id) AND (s.is_deleted = 0 OR s.is_deleted IS NULL)")
    long countByProductCodeAndNotId(@Param("productCode") String productCode, @Param("id") Integer id);
}
