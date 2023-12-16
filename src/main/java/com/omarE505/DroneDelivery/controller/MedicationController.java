package com.omarE505.DroneDelivery.controller;

import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.service.ImageService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("api/medications")
public class MedicationController {

    private final MedicationService mService;

    private final ImageService imageService;

    public MedicationController(MedicationService mService, ImageService imageService) {
        this.mService = mService;
        this.imageService = imageService;
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medications retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "No medications found", responseCode = "404"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping({"", "/"})
    public ResponseEntity<List<Medication>> getAllMeds() {
        List<Medication> medications = mService.findAll();
        if (medications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medication registered successfully", responseCode = "201"),
            @ApiResponse(description = "Invalid input", responseCode = "400")
    })
    @PostMapping({"", "/"})
    public ResponseEntity<Medication> register(@NotNull @Valid @RequestBody MedicationDto dto) {
        try {
            Medication med = mService.save(dto);
            return new ResponseEntity<>(med, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Image uploaded and medication updated successfully", responseCode = "201"),
            @ApiResponse(description = "Medication not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input or image processing error", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PostMapping(value = "upload/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<Medication> imageUpload(@PathVariable Long id,
                                                  @RequestParam("image") Optional<MultipartFile> imageFile) {
        try {
            byte[] imageData = imageService.processImage(imageFile.get());
            Medication updatedMedication = mService.imageUpdate(id, imageData);
            return new ResponseEntity<>(updatedMedication, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medication found successfully", responseCode = "200"),
            @ApiResponse(description = "Medication not found", responseCode = "404"),
            @ApiResponse(description = "Error in data format or processing", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Medication> findById(@PathVariable Long id) {
        try {
            Medication med = mService.findById(id);
            if (med.getImage() != null) {
                byte[] imageData = imageService.decompress(med.getImage());
                med.setImage(imageData);
            }
            return new ResponseEntity<>(med, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (DataFormatException | IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medication updated successfully", responseCode = "200"),
            @ApiResponse(description = "Medication not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Medication> update(@PathVariable Optional<Long> id,
                                             @NotNull @Valid @RequestBody MedicationDto dto) {
        try {
            Medication updatedMedication = mService.update(id, dto);
            return new ResponseEntity<>(updatedMedication, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medication deleted successfully", responseCode = "204"),
            @ApiResponse(description = "Medication not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            this.mService.delete(id);
            return new ResponseEntity<>("Deleted...", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
