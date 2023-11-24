package com.omarE505.DroneDelivery.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.mifmif.common.regex.Generex;
import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.repository.MedicationRepository;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final ModelMapper mapper;

    private final MedicationRepository medicationRepository;

    @Override
    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    @Override
    public Medication save(MedicationDto medication) throws IllegalArgumentException {
        String code = this.getCode();
        medication.setCode(code);
        Medication newMed = new Medication();
        mapper.map(medication, newMed);
        Medication savedMed = this.medicationRepository.save(newMed);
        return savedMed;
    }

    @Override
    public Medication findById(long id) throws ResourceNotFoundException {
        Medication medication = this.medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        return medication;
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = this.medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        try {
            this.medicationRepository.delete(medication);
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    @Override
    public Medication update(Optional<Long> id, MedicationDto dto) throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = this.medicationRepository
                .findById(id.orElseThrow(() -> new ResourceNotFoundException("Medication not found. id is null")))
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        mapper.map(dto, medication);
        try {
            this.medicationRepository.save(medication);
            return medication;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    @Override
    public Medication imageUpdate(Long id, byte[] imageData)
            throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = this.medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        medication.setImage(imageData);
        try {
            this.medicationRepository.save(medication);
            return medication;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    private String getCode() {
        Generex generex = new Generex("[A-Z0-9]{11,15}_*");
        return generex.random();
    }
}
