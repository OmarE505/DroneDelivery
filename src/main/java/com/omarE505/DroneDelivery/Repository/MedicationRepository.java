package com.omarE505.DroneDelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long>{
    
}
