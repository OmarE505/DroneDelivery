package com.omarE505.DroneDelivery.Repository;

import java.util.List;

import com.omarE505.DroneDelivery.entity.Drone;

public interface CustomDroneRepository {

    public List<Drone> findAvailable(int totalWeight);

}
