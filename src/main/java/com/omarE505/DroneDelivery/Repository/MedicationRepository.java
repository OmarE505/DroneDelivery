package com.omarE505.DroneDelivery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.Medication;

public interface MedicationRepository extends JpaRepository<Medication, Long>{
    
}
