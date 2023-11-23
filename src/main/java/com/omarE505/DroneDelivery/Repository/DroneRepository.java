package com.omarE505.DroneDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.Drone;

public interface DroneRepository extends JpaRepository<Drone, Long>{
    
}
