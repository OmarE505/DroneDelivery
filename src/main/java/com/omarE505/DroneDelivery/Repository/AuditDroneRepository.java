package com.omarE505.DroneDelivery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.AuditDrone;

public interface AuditDroneRepository extends JpaRepository<AuditDrone, Long>{
    
}
