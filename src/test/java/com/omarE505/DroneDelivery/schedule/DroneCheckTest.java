package com.omarE505.DroneDelivery.schedule;

import com.omarE505.DroneDelivery.entity.AuditDrone;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.repository.AuditDroneRepository;
import com.omarE505.DroneDelivery.repository.DroneRepository;
import com.omarE505.DroneDelivery.utils.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneCheckTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private AuditDroneRepository aDroneRepository;

    @Mock
    private ThreadPoolTaskScheduler scheduler;

    @InjectMocks
    private DroneCheck droneCheck;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBatteryCheck() {
        Drone drone1 = new Drone();
        drone1.setBatteryCapacity(25);
        drone1.setState(State.LOADED);

        Drone drone2 = new Drone();
        drone2.setBatteryCapacity(90);
        drone2.setState(State.IDLE);

        List<Drone> drones = new ArrayList<>();
        drones.add(drone1);
        drones.add(drone2);

        when(droneRepository.findAll()).thenReturn(drones);

        droneCheck.batteryCheck();

        verify(droneRepository, times(1)).findAll();
        verify(aDroneRepository, times(2)).save(any(AuditDrone.class));
        verify(droneRepository, times(2)).save(any(Drone.class));

        assertEquals(State.IDLE, drone1.getState());
        assertEquals(25, drone1.getBatteryCapacity());

        assertEquals(State.IDLE, drone2.getState());
        assertEquals(95, drone2.getBatteryCapacity());
    }
}
