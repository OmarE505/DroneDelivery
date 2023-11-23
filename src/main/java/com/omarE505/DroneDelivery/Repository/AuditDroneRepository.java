package com.omarE505.DroneDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.AuditDrone;

public interface AuditDroneRepository extends JpaRepository<AuditDrone, Long>{
    
}
