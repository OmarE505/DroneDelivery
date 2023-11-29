package com.omarE505.DroneDelivery.controller;

import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.repository.ModelRepository;
import com.omarE505.DroneDelivery.service.DroneService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ModelEnum;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;
import com.omarE505.DroneDelivery.utils.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DroneControllerTest {

    @Mock
    private DroneService droneService;

    @Mock
    private MedicationService medicationService;

    @Mock
    private ModelRepository modelRepository;

    @InjectMocks
    private DroneController droneController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDrones() {
        // Mock the behavior of the DroneService
        List<Drone> drones = new ArrayList<>();
        drones.add(new Drone());
        drones.add(new Drone());

        when(droneService.findAll()).thenReturn(drones);

        // Create a new instance of DroneController with the mocked DroneService
        DroneController droneController = new DroneController(droneService, medicationService, modelRepository);

        // Call the method being tested
        ResponseEntity<List<Drone>> response = droneController.getAllDrones();

        // Assert the behavior of the method
        assertEquals(ResponseEntity.ok(drones), response);

        // Verify that the DroneService method was called exactly once
        verify(droneService, times(1)).findAll();
    }

    @Test
    void testRegister() throws IllegalArgumentException, RequirementNotMetException {
        // Mocking a DTO object
        DroneDto droneDto = new DroneDto();
        Model model = new Model();
        droneDto.setModel(model);

        when(modelRepository.findById(droneDto.getModel().getId())).thenReturn(Optional.of(model));
        when(droneService.register(droneDto)).thenReturn(new Drone());

        ResponseEntity<Drone> response = droneController.register(droneDto);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegisterDroneWithInvalidModel() {
        DroneDto droneDto = new DroneDto();
        Model model = new Model();
        droneDto.setModel(model);

        assertThrows(IllegalArgumentException.class, () -> droneController.register(droneDto));
    }

    @Test
    public void testFindDroneById() throws ResourceNotFoundException {
        long id = 1L;
        Drone drone = new Drone();
        when(droneService.findById(id)).thenReturn(drone);

        ResponseEntity<Drone> response = droneController.findById(id);

        assertEquals(ResponseEntity.ok(drone), response);
    }

    @Test
    void testFindByIdWithInvalidId() throws ResourceNotFoundException {
        long invalidDroneId = -1L;

        when(droneService.findById(invalidDroneId)).thenThrow(new ResourceNotFoundException("Drone not found"));

        assertThrows(ResourceNotFoundException.class, () -> droneController.findById(invalidDroneId));

        verify(droneService, times(1)).findById(invalidDroneId);
    }

    @Test
    public void testUpdateDrone() throws ResourceNotFoundException {
        Long id = 1L;
        DroneDto droneDto = new DroneDto();
        Model model = new Model(ModelEnum.LIGHT_WEIGHT);
        droneDto.setModel(model);

        Drone drone = new Drone();
        when(droneService.update(Optional.of(id), droneDto)).thenReturn(drone);

        ResponseEntity<Drone> response = droneController.update(Optional.of(id), droneDto);

        assertEquals(ResponseEntity.ok(drone), response);
    }

    @Test
    void testUpdateWithInvalidId() throws ResourceNotFoundException {
        long invalidDroneId = -1L;
        DroneDto updateDto = new DroneDto();

        when(droneService.update(any(), eq(updateDto)))
                .thenThrow(new ResourceNotFoundException("Drone not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> droneController.update(Optional.of(invalidDroneId), updateDto));

        verify(droneService, times(1)).update(any(), eq(updateDto));
    }

    @Test
    void testUpdateWithInvalidModel() throws ResourceNotFoundException, IllegalArgumentException {
        Long validDroneId = 1L;
        DroneDto updatedDroneDto = new DroneDto();
        updatedDroneDto.setModel(null);

        when(droneService.update(eq(Optional.of(validDroneId)), eq(updatedDroneDto)))
                .thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class,
                () -> droneController.update(Optional.of(validDroneId), updatedDroneDto));

        verify(droneService, times(1)).update(eq(Optional.of(validDroneId)), eq(updatedDroneDto));
    }

    @Test
    public void testDeleteDrone() throws ResourceNotFoundException {
        long id = 1L;

        droneController.delete(id);

        verify(droneService, times(1)).delete(id);
    }

    @Test
    void testDeleteWithInvalidId() throws ResourceNotFoundException {
        long invalidDroneId = -1L;

        doThrow(new ResourceNotFoundException("Drone not found")).when(droneService).delete(invalidDroneId);

        assertThrows(ResourceNotFoundException.class, () -> droneController.delete(invalidDroneId));

        verify(droneService, times(1)).delete(invalidDroneId);
    }

    @Test
    void testLoad() throws ResourceNotFoundException, RequirementNotMetException {
        Long validDroneId = 1L;
        List<Long> validMedicationIds = new ArrayList<>();
        validMedicationIds.add(1L);
        validMedicationIds.add(2L);

        when(droneService.load(anyList(), eq(validDroneId))).thenReturn(new Drone());

        ResponseEntity<Drone> response = droneController.load(validMedicationIds, validDroneId);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(droneService, times(1)).load(anyList(), eq(validDroneId));
    }

    @Test
    void testLoadWithInvalidId() throws ResourceNotFoundException, RequirementNotMetException {
        Long invalidDroneId = -1L;
        List<Long> invalidMedicationIds = new ArrayList<>();
        invalidMedicationIds.add(1L);

        when(droneService.load(anyList(), eq(invalidDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> droneController.load(invalidMedicationIds, invalidDroneId));

        verify(droneService, times(1)).load(anyList(), eq(invalidDroneId));
    }

    @Test
    void testLoadWithWrongMedicationIds() throws ResourceNotFoundException, RequirementNotMetException {
        Long validDroneId = 1L;
        List<Long> wrongMedicationIds = new ArrayList<>();
        wrongMedicationIds.add(100L);

        when(droneService.load(anyList(), eq(validDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        Exception thrownException = assertThrows(ResourceNotFoundException.class,
                () -> droneController.load(wrongMedicationIds, validDroneId));

        verify(droneService, times(1)).load(anyList(), eq(validDroneId));

        assertNotNull(thrownException);

        assertTrue(thrownException instanceof ResourceNotFoundException);
    }

    @Test
    void testGetMedications() throws ResourceNotFoundException {
        Long validDroneId = 1L;
        List<Medication> medications = new ArrayList<>();

        when(droneService.getMedications(eq(validDroneId)))
                .thenReturn(medications);

        ResponseEntity<List<Medication>> response = droneController.getMedications(validDroneId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medications, response.getBody());

        verify(droneService, times(1)).getMedications(eq(validDroneId));
    }

    @Test
    void testGetMedicationsWithInvalidDroneId() throws ResourceNotFoundException {
        Long invalidDroneId = -1L;

        when(droneService.getMedications(eq(invalidDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> droneController.getMedications(invalidDroneId));

        verify(droneService, times(1)).getMedications(eq(invalidDroneId));
    }

    @Test
    void testFindAvailableDronesWithValidWeight() throws ResourceNotFoundException {
        int validTotalWeight = 150;

        List<Drone> availableDrones = new ArrayList<>();
        Drone drone1 = new Drone();
        drone1.setState(State.IDLE);
        drone1.setModel(new Model(ModelEnum.LIGHT_WEIGHT));
        Drone drone2 = new Drone();
        drone2.setState(State.IDLE);
        drone2.setModel(new Model(ModelEnum.HEAVY_WEIGHT));
        availableDrones.add(drone1);
        availableDrones.add(drone2);

        when(droneService.findAll()).thenReturn(availableDrones);

        DroneController droneController = new DroneController(droneService, medicationService, modelRepository);

        ResponseEntity<List<Drone>> response = droneController.findAvailable(validTotalWeight);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Drone> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1, responseBody.size());
        verify(droneService, times(1)).findAll();
    }

    @Test
    void testFindAvailableDronesWithInvalidWeight() throws ResourceNotFoundException {
        int invalidTotalWeight = 150;
        List<Drone> availableDrones = new ArrayList<>();

        when(droneService.findAll()).thenReturn(availableDrones);

        ResponseEntity<List<Drone>> response = droneController.findAvailable(invalidTotalWeight);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Drone> responseBody = response.getBody();
        int bodySize = (responseBody != null) ? responseBody.size() : 0;
        assertEquals(0, bodySize);

        verify(droneService, times(1)).findAll();
    }

    @Test
    public void testCheckBatteryLevel() throws ResourceNotFoundException {
        long droneId = 1L;
        int expectedBatteryLevel = 75;

        Drone drone = new Drone();
        drone.setBatteryCapacity(expectedBatteryLevel);
        when(droneService.findById(droneId)).thenReturn(drone);

        ResponseEntity<Integer> responseEntity = droneController.checkBatteryLevel(droneId);

        // Assert
        assertEquals(expectedBatteryLevel, responseEntity.getBody());
    }

    @Test
    public void testCheckBatteryLevelWithInvalidId() throws ResourceNotFoundException {
        // Arrange
        long invalidDroneId = 100L; // Assuming this ID doesn't exist
        when(droneService.findById(invalidDroneId)).thenThrow(ResourceNotFoundException.class);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            droneController.checkBatteryLevel(invalidDroneId);
        });
    }
}