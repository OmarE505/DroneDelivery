package com.omarE505.DroneDelivery.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
        List<Drone> drones = new ArrayList<>();
        when(droneService.findAll()).thenReturn(drones);

        ResponseEntity<List<Drone>> response = droneController.getAllDrones();

        assertEquals(ResponseEntity.ok(drones), response);
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

        droneDto.getModel().getName();

        assertThrows(IllegalArgumentException.class, () -> droneController.register(droneDto));
    }

    @Test
    public void testFindDroneById() throws ResourceNotFoundException {
        Long id = 1L;
        Drone drone = new Drone();
        when(droneService.findById(id)).thenReturn(drone);

        ResponseEntity<Drone> response = droneController.findById(id);

        assertEquals(ResponseEntity.ok(drone), response);
    }

    @Test
    void testFindByIdWithInvalidId() throws ResourceNotFoundException {
        long invalidDroneId = -1L; // Invalid Drone ID

        // Mock behavior of service to throw ResourceNotFoundException for an invalid ID
        when(droneService.findById(invalidDroneId)).thenThrow(new ResourceNotFoundException("Drone not found"));

        // Invoke findById method of the controller with an invalid ID
        assertThrows(ResourceNotFoundException.class, () -> droneController.findById(invalidDroneId));

        // Verify that the service method was called with the provided invalid ID
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
        long invalidDroneId = -1L; // Invalid Drone ID
        DroneDto updateDto = new DroneDto(); // Create a DTO with updated data

        // Mock behavior of service to throw ResourceNotFoundException for an invalid ID
        when(droneService.update(any(), eq(updateDto)))
                .thenThrow(new ResourceNotFoundException("Drone not found"));

        // Invoke update method of the controller with an invalid ID and DTO
        assertThrows(ResourceNotFoundException.class,
                () -> droneController.update(Optional.of(invalidDroneId), updateDto));

        // Verify that the service method was called with the provided invalid ID and
        // DTO
        verify(droneService, times(1)).update(any(), eq(updateDto));
    }

    @Test
    void testUpdateWithInvalidModel() throws ResourceNotFoundException, IllegalArgumentException {
        Long validDroneId = 1L;
        DroneDto updatedDroneDto = new DroneDto();
        updatedDroneDto.setModel(null); // Setting an invalid model

        // Mock the behavior to throw IllegalArgumentException when the model is null
        when(droneService.update(eq(Optional.of(validDroneId)), eq(updatedDroneDto)))
                .thenThrow(IllegalArgumentException.class);

        // Invoke the update method of the controller with an invalid model and verify
        // the exception
        assertThrows(IllegalArgumentException.class,
                () -> droneController.update(Optional.of(validDroneId), updatedDroneDto));

        // Verify that the service method was called with the provided invalid model for
        // update
        verify(droneService, times(1)).update(eq(Optional.of(validDroneId)), eq(updatedDroneDto));
    }

    @Test
    public void testDeleteDrone() throws ResourceNotFoundException {
        Long id = 1L;

        droneController.delete(id);

        verify(droneService, times(1)).delete(id);
    }

    @Test
    void testDeleteWithInvalidId() throws ResourceNotFoundException {
        long invalidDroneId = -1L; // Invalid Drone ID

        // Mock behavior of service to throw ResourceNotFoundException for an invalid ID
        doThrow(new ResourceNotFoundException("Drone not found")).when(droneService).delete(invalidDroneId);

        // Invoke delete method of the controller with an invalid ID
        assertThrows(ResourceNotFoundException.class, () -> droneController.delete(invalidDroneId));

        // Verify that the service method was called with the provided invalid ID
        verify(droneService, times(1)).delete(invalidDroneId);
    }

    @Test
    void testLoad() throws ResourceNotFoundException, RequirementNotMetException {
        Long validDroneId = 1L;
        List<Long> validMedicationIds = new ArrayList<>(); // Valid medication IDs
        validMedicationIds.add(1L);
        validMedicationIds.add(2L);

        // Mock the load operation in the service layer
        when(droneService.load(anyList(), eq(validDroneId))).thenReturn(new Drone());

        // Invoke the load method of the controller with valid medication IDs and drone
        // ID
        ResponseEntity<Drone> response = droneController.load(validMedicationIds, validDroneId);

        // Verify if the controller returns an OK status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the service method was called with the provided medications and
        // drone ID
        verify(droneService, times(1)).load(anyList(), eq(validDroneId));
    }

    @Test
    void testLoadWithInvalidId() throws ResourceNotFoundException, RequirementNotMetException {
        Long invalidDroneId = 1L; // Invalid Drone ID
        List<Long> invalidMedicationIds = new ArrayList<>(); // Invalid medication IDs
        invalidMedicationIds.add(-1L); // Adding an invalid medication ID

        // Mock the behavior to throw ResourceNotFoundException when loading with
        // invalid data
        when(droneService.load(anyList(), eq(invalidDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        // Invoke the load method of the controller with invalid data
        assertThrows(ResourceNotFoundException.class, () -> droneController.load(invalidMedicationIds, invalidDroneId));

        // Verify that the service method was called with the provided invalid
        // medications and drone ID
        verify(droneService, times(1)).load(anyList(), eq(invalidDroneId));
    }

    @Test
    void testLoadWithWrongMedicationIds() throws ResourceNotFoundException, RequirementNotMetException {
        Long validDroneId = 1L;
        List<Long> wrongMedicationIds = new ArrayList<>(); // Wrong medication IDs
        wrongMedicationIds.add(100L); // Add a non-existing medication ID

        // Mock the behavior to throw ResourceNotFoundException when loading with wrong
        // medication IDs
        when(droneService.load(anyList(), eq(validDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        // Invoke the load method of the controller with wrong medication IDs
        assertThrows(ResourceNotFoundException.class, () -> droneController.load(wrongMedicationIds, validDroneId));

        // Verify that the service method was called with the provided wrong medication
        // IDs and drone ID
        verify(droneService, times(1)).load(anyList(), eq(validDroneId));
    }

    @Test
    void testGetMedications() throws ResourceNotFoundException {
        Long validDroneId = 1L;
        List<Medication> medications = new ArrayList<>(); // Simulated list of medications

        // Mock the service method to return a list of medications for a valid drone ID
        when(droneService.getMedications(eq(validDroneId)))
                .thenReturn(medications);

        // Invoke the getMedications method of the controller for a valid drone ID
        ResponseEntity<List<Medication>> response = droneController.getMedications(validDroneId);

        // Verify that the controller returns medications for the given drone ID
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medications, response.getBody());

        // Verify that the service method was called with the provided valid drone ID
        verify(droneService, times(1)).getMedications(eq(validDroneId));
    }

    @Test
    void testGetMedicationsWithInvalidDroneId() throws ResourceNotFoundException {
        Long invalidDroneId = -1L;

        // Mock the service method to throw ResourceNotFoundException for an invalid
        // drone ID
        when(droneService.getMedications(eq(invalidDroneId)))
                .thenThrow(ResourceNotFoundException.class);

        // Invoke the getMedications method of the controller with an invalid drone ID
        // and catch the exception
        assertThrows(ResourceNotFoundException.class, () -> droneController.getMedications(invalidDroneId));

        // Verify that the service method was called with the provided invalid drone ID
        verify(droneService, times(1)).getMedications(eq(invalidDroneId));
    }

    @Test
    void testFindAvailableDronesWithValidWeight() throws ResourceNotFoundException {
        int validTotalWeight = 150;
        List<Drone> availableDrones = new ArrayList<>(); // Simulated list of available drones
        Drone drone1 = new Drone();
        drone1.setState(State.IDLE);
        drone1.setModel(new Model(ModelEnum.LIGHT_WEIGHT));
        Drone drone2 = new Drone();
        drone2.setState(State.IDLE);
        drone2.setModel(new Model(ModelEnum.HEAVY_WEIGHT));

        availableDrones.add(drone1);
        availableDrones.add(drone2);

        // Mock the service method to return available drones for a valid total weight
        when(droneService.findAll()).thenReturn(availableDrones);

        // Invoke the findAvailable method of the controller for a valid total weight
        ResponseEntity<List<Drone>> response = droneController.findAvailable(validTotalWeight);

        // Verify that the controller returns available drones for the given weight
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Perform a null check before accessing the size of the response body
        List<Drone> responseBody = response.getBody();
        int bodySize = (responseBody != null) ? responseBody.size() : 0;
        assertEquals(1, bodySize);

        // Verify that the service method was called
        verify(droneService, times(1)).findAll();
    }

    @Test
    void testFindAvailableDronesWithInvalidWeight() throws ResourceNotFoundException {
        int invalidTotalWeight = 150;
        List<Drone> availableDrones = new ArrayList<>(); // Simulated list of available drones

        // Mock the service method to return available drones for a valid total weight
        when(droneService.findAll()).thenReturn(availableDrones);

        // Invoke the findAvailable method of the controller for an invalid total weight
        ResponseEntity<List<Drone>> response = droneController.findAvailable(invalidTotalWeight);

        // Verify that the controller returns an empty list for an invalid weight
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Perform a null check before accessing the size of the response body
        List<Drone> responseBody = response.getBody();
        int bodySize = (responseBody != null) ? responseBody.size() : 0;
        assertEquals(0, bodySize);

        // Verify that the service method was called
        verify(droneService, times(1)).findAll();
    }

    @Test
    public void testCheckBatteryLevel() throws ResourceNotFoundException {
        // Arrange
        long droneId = 1L;
        int expectedBatteryLevel = 75; // Define the expected battery level

        // Mock the behavior of the DroneService to return a drone with a specific
        // battery level
        Drone drone = new Drone();
        drone.setBatteryCapacity(expectedBatteryLevel); // Set the expected battery level
        when(droneService.findById(droneId)).thenReturn(drone);

        // Act
        ResponseEntity<Integer> responseEntity = droneController.checkBatteryLevel(droneId);

        // Assert
        assertEquals(expectedBatteryLevel, responseEntity.getBody()); // Check if the returned battery level matches the
                                                                      // expected level
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