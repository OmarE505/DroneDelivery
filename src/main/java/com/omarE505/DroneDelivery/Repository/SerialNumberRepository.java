package com.omarE505.DroneDelivery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.omarE505.DroneDelivery.entity.SerialNumber;

public interface SerialNumberRepository extends JpaRepository<SerialNumber, Long> {
    
    @Query("SELECT s FROM SerialNumber s where s.serialValue = ?1")
    public SerialNumber findByValue(String serialValue);
    
}
