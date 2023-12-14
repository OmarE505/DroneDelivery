package com.omarE505.DroneDelivery.Schedule;

import java.util.List;

import com.omarE505.DroneDelivery.entity.AuditDrone;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.Repository.AuditDroneRepository;
import com.omarE505.DroneDelivery.Repository.DroneRepository;
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
        drones.forEach(this::manageDroneBattery);
    }

    private void manageDroneBattery(Drone drone) {
        try {
            int batteryCapacity = drone.getBatteryCapacity();
            State state = drone.getState();

            if (state == State.LOADED && batteryCapacity <= 25) {
                handleLowBatteryLoadedState(drone);
            } else if (state == State.LOADED) {
                drainBatteryWhenLoaded(drone);
            } else if (state == State.IDLE && batteryCapacity < 100) {
                chargeBatteryWhileIdle(drone);
            }

            saveAuditInformation(drone);
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Drone not found");
        }
    }

    private void handleLowBatteryLoadedState(Drone drone) {
        drone.setState(State.IDLE);
        droneRepository.save(drone);
        System.out.println("Drone ID: " + drone.getId() + " stopped due to low battery (25% or below).");
    }

    private void drainBatteryWhenLoaded(Drone drone) {
        int drainedBattery = Math.max(drone.getBatteryCapacity() - 5, 0);
        drone.setBatteryCapacity(drainedBattery);
        droneRepository.save(drone);
    }

    private void chargeBatteryWhileIdle(Drone drone) {
        int chargedBattery = Math.min(drone.getBatteryCapacity() + 5, 100);
        drone.setBatteryCapacity(chargedBattery);
        droneRepository.save(drone);
        System.out.println("Drone ID: " + drone.getId() + " gained 5% battery while idle.");
    }

    private void saveAuditInformation(Drone drone) {
        int batteryPercentageAfter = drone.getBatteryCapacity();
        System.out.println("Drone ID: " + drone.getId() + ", Battery Percentage: " + batteryPercentageAfter);

        AuditDrone aDrone = new AuditDrone(drone.getState(), batteryPercentageAfter);
        aDrone.setDroneId(drone.getId());
        aDroneRepository.save(aDrone);
    }

}
