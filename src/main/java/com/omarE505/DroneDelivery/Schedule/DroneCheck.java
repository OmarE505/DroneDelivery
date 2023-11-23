package com.omarE505.DroneDelivery.schedule;

import java.util.List;

import com.omarE505.DroneDelivery.entity.AuditDrone;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.repository.AuditDroneRepository;
import com.omarE505.DroneDelivery.repository.DroneRepository;
import com.omarE505.DroneDelivery.utils.State;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DroneCheck {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private AuditDroneRepository aDroneRepository;

    @Scheduled(fixedDelay = 4 * 50 * 1000)
    public void batteryCheck() {
        List<Drone> drones = droneRepository.findAll();
        drones.stream().forEach(drone -> {
            try {
                int batteryCapacity = drone.getBatteryCapacity();
                State state = drone.getState();
                AuditDrone aDrone = new AuditDrone(state, batteryCapacity);
                aDrone.setDroneId(drone.getId());
                aDroneRepository.save(aDrone);
            } catch (IllegalArgumentException exc) {
                throw new IllegalArgumentException("Drone not found");
            }
        });
    }

}
