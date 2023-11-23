package com.omarE505.DroneDelivery.Repository;

import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

public interface CustomMedRepository {

    public Drone checkLoadedMedications(long droneId) throws ResourceNotFoundException;

}
