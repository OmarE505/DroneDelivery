package com.omarE505.DroneDelivery.service;

import java.util.List;

import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

public interface MedicationService {

    public List<Medication> findAll();

    public Medication save(MedicationDto medication) throws IllegalArgumentException;

    public Medication findById(long id) throws ResourceNotFoundException;

    public void delete(long id) throws ResourceNotFoundException, IllegalArgumentException;

    public Medication update(MedicationDto dto) throws ResourceNotFoundException, IllegalArgumentException;

    public Medication imageUpdate(Long id, byte[] imageData) throws ResourceNotFoundException, IllegalArgumentException;

    public Drone checkLoadedMedications(long droneId) throws ResourceNotFoundException;

}
