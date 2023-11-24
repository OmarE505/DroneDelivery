package com.omarE505.DroneDelivery.service;

import java.util.List;
import java.util.Optional;

import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

public interface DroneService {

    public List<Drone> findAll();

    public Drone register(DroneDto drone) throws IllegalArgumentException, RequirementNotMetException;

    public Drone load(List<Medication> medications, Long droneId)
            throws ResourceNotFoundException, IllegalArgumentException, RequirementNotMetException;

    public Drone findById(long id) throws ResourceNotFoundException;

    public void delete(long id) throws ResourceNotFoundException, IllegalArgumentException;

    public Drone update(Optional<Long> id, DroneDto drone) throws ResourceNotFoundException, IllegalArgumentException;

    public List<Medication> getMedications(long droneId) throws ResourceNotFoundException;
}
