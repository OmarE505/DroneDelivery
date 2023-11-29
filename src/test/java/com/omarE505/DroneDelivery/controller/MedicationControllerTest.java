package com.omarE505.DroneDelivery.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.service.ImageService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class MedicationControllerTest {
    @Mock
    private MedicationService medicationService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private MedicationController medicationController;

    @Test
    public void testGetAllMeds() {
        // Mock data
        List<Medication> medications = Arrays.asList(new Medication(), new Medication());

        // Mock the service behavior
        Mockito.when(medicationService.findAll()).thenReturn(medications);

        // Perform the test
        ResponseEntity<List<Medication>> response = medicationController.getAllMeds();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medications, response.getBody());
    }

    @Test
    public void testRegister() {
        // Mock data
        MedicationDto medicationDto = new MedicationDto();
        Medication medication = new Medication();

        // Mock the service behavior
        Mockito.when(medicationService.save(any(MedicationDto.class))).thenReturn(medication);

        // Perform the test
        ResponseEntity<Medication> response = medicationController.register(medicationDto);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medication, response.getBody());
    }

    @Test
    public void testRegister_InvalidData() {
        // Mock data - Here, intentionally create an invalid MedicationDto
        MedicationDto invalidMedicationDto = new MedicationDto();
        // You might deliberately set invalid data, for example:
        // invalidMedicationDto.setSomeField(null);

        // Mock the service behavior to throw IllegalArgumentException for invalid data
        Mockito.when(medicationService.save(any(MedicationDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        // Perform the test and check for the expected exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            medicationController.register(invalidMedicationDto);
        });

        // Assertions
        assertNotNull(exception);
        assertEquals("Invalid data", exception.getMessage());
    }

    @Test
    public void testImageUpload() throws ResourceNotFoundException, IOException {
        // Mock data
        Long id = 1L;
        byte[] imageData = { /* Some image data */ };
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);

        // Mock the service behavior
        Mockito.when(imageService.processImage(mockFile)).thenReturn(imageData);
        Mockito.when(medicationService.imageUpdate(id, imageData)).thenReturn(new Medication());

        // Perform the test
        ResponseEntity<Medication> response = medicationController.imageUpload(id, Optional.of(mockFile));

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testFindById_CorrectInput() throws ResourceNotFoundException, IOException, DataFormatException {
        // Mock data
        long validId = 1L;
        Medication validMedication = new Medication();
        // Assuming valid data is retrieved from the service
        Mockito.when(medicationService.findById(validId)).thenReturn(validMedication);

        // Perform the test
        ResponseEntity<Medication> response = medicationController.findById(validId);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validMedication, response.getBody());
    }

    @Test
    public void testFindById_IncorrectInput() throws ResourceNotFoundException, IOException, DataFormatException {
        // Mock data
        long invalidId = 999L;
        Mockito.when(medicationService.findById(invalidId))
                .thenThrow(new ResourceNotFoundException("Medication not found"));

        // Perform the test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicationController.findById(invalidId);
        });

        // Assertions
        assertNotNull(exception);
        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    public void testUpdate_CorrectInput() throws ResourceNotFoundException, IllegalArgumentException {
        // Mock data
        long validId = 1L;
        MedicationDto validDto = new MedicationDto();
        Medication updatedMedication = new Medication();
        // Assuming valid data is retrieved from the service
        Mockito.when(medicationService.update(eq(Optional.of(validId)), eq(validDto))).thenReturn(updatedMedication);

        // Perform the test
        ResponseEntity<Medication> response = medicationController.update(Optional.of(validId), validDto);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedMedication, response.getBody());
    }

    @Test
    public void testUpdate_IncorrectInput() throws ResourceNotFoundException, IllegalArgumentException {
        // Mock data
        long invalidId = 999L;
        MedicationDto invalidDto = new MedicationDto();
        Mockito.when(medicationService.update(eq(Optional.of(invalidId)), eq(invalidDto)))
                .thenThrow(new ResourceNotFoundException("Medication not found"));

        // Perform the test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicationController.update(Optional.of(invalidId), invalidDto);
        });

        // Assertions
        assertNotNull(exception);
        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    public void testDelete_CorrectInput() throws ResourceNotFoundException, IllegalArgumentException {
        // Mock data
        long validId = 1L;

        // Perform the test
        ResponseEntity<?> response = medicationController.delete(validId);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("", response.getBody()); // Assuming an empty string as response body for deletion
    }

    @Test
    public void testDelete_IncorrectInput() throws ResourceNotFoundException {
        // Mock data
        long invalidId = 999L;
        Mockito.doThrow(new ResourceNotFoundException("Medication not found")).when(medicationService)
                .delete(invalidId);

        // Perform the test
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            medicationController.delete(invalidId);
        });

        // Assertions
        assertNotNull(exception);
        assertEquals("Medication not found", exception.getMessage());
    }
}
