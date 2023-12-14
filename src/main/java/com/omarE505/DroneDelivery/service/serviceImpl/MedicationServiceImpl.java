package com.omarE505.DroneDelivery.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.mifmif.common.regex.Generex;
import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.Repository.MedicationRepository;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final ModelMapper mapper;

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(ModelMapper mapper, MedicationRepository medicationRepository) {
        this.mapper = mapper;
        this.medicationRepository = medicationRepository;
    }

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
        Medication savedMed = medicationRepository.save(newMed);
        return savedMed;
    }

    @Override
    public Medication findById(long id) throws ResourceNotFoundException {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        return medication;
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        try {
            medicationRepository.delete(medication);
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    @Override
    public Medication update(Optional<Long> id, MedicationDto dto) throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = medicationRepository
                .findById(id.orElseThrow(() -> new ResourceNotFoundException("Medication not found. id is null")))
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        mapper.map(dto, medication);
        try {
            medicationRepository.save(medication);
            return medication;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    @Override
    public Medication imageUpdate(Long id, byte[] imageData)
            throws ResourceNotFoundException, IllegalArgumentException {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medication not found"));
        medication.setImage(imageData);
        try {
            medicationRepository.save(medication);
            return medication;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Medication cannot be null");
        }
    }

    private String getCode() {
        Generex generex = new Generex("[A-Z0-9_]{10,15}");
        String random = generex.random();
        return random;
    }
}
