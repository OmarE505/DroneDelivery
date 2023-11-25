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

    @Scheduled(fixedDelay = 120000)
    public void batteryCheck() {
        List<Drone> drones = droneRepository.findAll();
        drones.stream().forEach(drone -> {
            try {
                int batteryCapacity = drone.getBatteryCapacity();
                State state = drone.getState();

                // Simulate battery drain when the drone is loaded
                if (state == State.LOADED && batteryCapacity <= 25) {
                    drone.setState(State.IDLE); // Change the state to IDLE
                    droneRepository.save(drone); // Save the updated state

                    System.out.println("Drone ID: " + drone.getId() + " stopped due to low battery (25% or below).");
                } else if (state == State.LOADED) {
                    // Simulate battery drain when the drone is loaded
                    int drainedBattery = Math.max(batteryCapacity - 5, 0); // Drain by 5% but not below 0
                    drone.setBatteryCapacity(drainedBattery);
                    droneRepository.save(drone); // Save the updated battery percentage
                } else if (state == State.IDLE && batteryCapacity < 100) {
                    int chargedBattery = Math.min(batteryCapacity + 5, 100);
                    drone.setBatteryCapacity(chargedBattery);
                    droneRepository.save(drone);
                    System.out.println("Drone ID: " + drone.getId() + " gained 5% battery while idle.");
                }

                // Log the battery percentage after the drain
                int batteryPercentageAfter = drone.getBatteryCapacity();
                System.out.println(
                        "Drone ID: " + drone.getId() + ", Battery Percentage: " + batteryPercentageAfter);

                // Save audit information
                AuditDrone aDrone = new AuditDrone(state, batteryCapacity);
                aDrone.setDroneId(drone.getId());
                aDroneRepository.save(aDrone);
            } catch (IllegalArgumentException exc) {
                throw new IllegalArgumentException("Drone not found");
            }
        });
    }

}
