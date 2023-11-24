package com.omarE505.DroneDelivery.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.service.ImageService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/medications")
public class MedicationController {

    @Autowired
    private MedicationService mService;

    @Autowired
    private ImageService imageService;

    public MedicationController(MedicationService mService, ImageService imageService) {
        this.mService = mService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMeds() {
        return ResponseEntity.ok(mService.findAll());
    }

    @PostMapping
    public ResponseEntity<Medication> register(@NotNull @Valid @RequestBody MedicationDto dto)
            throws IllegalArgumentException {
        Medication med = mService.save(dto);
        return ResponseEntity.ok(med);
    }

    @PostMapping(value = "upload/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Medication> imageUpload(@PathVariable Long id,
            @RequestParam("image") Optional<MultipartFile> imageFile)
            throws ResourceNotFoundException, IOException {
        byte[] imageData = imageService.processImage(imageFile.get());
        return ResponseEntity.ok(mService.imageUpdate(id, imageData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> findById(@PathVariable Long id)
            throws ResourceNotFoundException, IOException, DataFormatException {
        Medication med = mService.findById(id);
        if (med.getImage() != null) {
            byte[] imageData = imageService.decompress(med.getImage());
            med.setImage(imageData);
        }
        return ResponseEntity.ok(med);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> update(@PathVariable Optional<Long> id, @NotNull @Valid @RequestBody MedicationDto dto)
            throws ResourceNotFoundException, IllegalArgumentException {
        return ResponseEntity.ok(mService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) throws ResourceNotFoundException, IllegalArgumentException {
        this.mService.delete(id);
        return ResponseEntity.ok("");
    }

}
