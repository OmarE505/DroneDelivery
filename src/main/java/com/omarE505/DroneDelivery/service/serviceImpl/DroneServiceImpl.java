package com.omarE505.DroneDelivery.service.serviceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.github.olili2017.O2019.SerialNumberGenerator;
import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.entity.SerialNumber;
import com.omarE505.DroneDelivery.repository.DroneRepository;
import com.omarE505.DroneDelivery.repository.MedicationRepository;
import com.omarE505.DroneDelivery.repository.ModelRepository;
import com.omarE505.DroneDelivery.repository.SerialNumberRepository;
import com.omarE505.DroneDelivery.service.DroneService;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;
import com.omarE505.DroneDelivery.utils.State;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    private final SerialNumberRepository sNumberRepository;

    private final ModelMapper mapper;

    private final ModelRepository modelRepository;

    public DroneServiceImpl(DroneRepository droneRepository, MedicationRepository medicationRepository,
            SerialNumberRepository serialNumberRepository, ModelMapper mapper, ModelRepository modelRepository) {
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        sNumberRepository = serialNumberRepository;
        this.mapper = mapper;
        this.modelRepository = modelRepository;
    }

    @Override
    public List<Drone> findAll() {
        return droneRepository.findAll();
    }

    @Override
    public Drone register(DroneDto drone) throws RequirementNotMetException, IllegalArgumentException {
        Optional<Long> optDroneCount = Optional.ofNullable(droneRepository.count());
        if (optDroneCount.isPresent() && optDroneCount.get() < 10) {
            try {
                String serialNumber = generateSerialNumber();
                boolean exists = sNumberRepository.findByValue(serialNumber) != null;
                while (exists) {
                    serialNumber = generateSerialNumber();
                    exists = sNumberRepository.findByValue(serialNumber) != null;
                }
                SerialNumber number = new SerialNumber(serialNumber);

                // Fetch existing model from repository based on ModelEnum
                Model model = modelRepository.findById(drone.getModel().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Model not found"));

                Drone newDrone = new Drone(number, model);
                System.out.println("Drone created ...");
                newDrone.setBatteryCapacity(100);
                newDrone.setState(State.IDLE);
                number.setDrone(newDrone);
                return droneRepository.save(newDrone);
            } catch (IllegalArgumentException exception) {
                throw new IllegalArgumentException("Drone cannot be null");
            }
        } else {
            throw new RequirementNotMetException("Maximumm number of drones that can be created is 10");
        }
    }

    @Override
    public Drone load(List<Medication> medications, Long droneId)
            throws ResourceNotFoundException, IllegalArgumentException, RequirementNotMetException {
        try {
            Drone drone = droneRepository.findById(droneId)
                    .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));

            int totalMedicationWeight = medications.stream().mapToInt(Medication::getWeight).reduce(0, Integer::sum);
            Model droneModel = drone.getModel();
            if (droneModel != null && droneModel.getValue() >= totalMedicationWeight
                    && drone.getBatteryCapacity() >= 25) {
                drone.setState(State.LOADING);
                System.out.println("Drone is currently ... : " + drone.getState());
                droneRepository.save(drone);
                Thread.sleep(2000);
                drone.setMedications(medications);
                drone.setState(State.LOADED);
                System.out.println("Drone is currently ... : " + drone.getState());
                Drone savedDrone = droneRepository.save(drone);
                for (Medication med : medications) {
                    med.setDrone(drone);
                    medicationRepository.save(med);
                }
                return savedDrone;
            } else {
                drone.setState(State.IDLE);
                droneRepository.save(drone);
                String errorMessage = (droneModel == null || droneModel.getValue() < totalMedicationWeight)
                        ? "Total medication weight exceeds drone model specification"
                        : "Drone battery capacity is below 25%";
                throw new RequirementNotMetException(errorMessage);
            }
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Drone cannot be null");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RequirementNotMetException("Interruption occured while loading");
        }
    }

    @Override
    public Drone findById(long id) throws ResourceNotFoundException {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));
        return drone;
    }

    @Override
    public Drone update(Optional<Long> id, DroneDto drone) throws ResourceNotFoundException, IllegalArgumentException {
        Drone existingDrone = droneRepository
                .findById(id.orElseThrow(() -> new ResourceNotFoundException("Drone not found. id is null")))
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));
        SerialNumber oldSerialNumber = existingDrone.getSerialNumber();
        mapper.map(drone, existingDrone);
        if (drone.getModel() != null) {
            Model model = modelRepository.findById(drone.getModel().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Model not found"));
            model.setValue();
            existingDrone.setModel(model);
        }
        existingDrone.setSerialNumber(oldSerialNumber); // serial number should not be updated
        try {
            droneRepository.save(existingDrone);
            return existingDrone;
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Drone cannot be null");
        }
    }

    @Override
    public List<Medication> getMedications(long droneId) throws ResourceNotFoundException {
        Drone drone = droneRepository.findById(droneId)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));
        Optional<List<Medication>> optMeds = Optional.of(drone.getMedications());
        return optMeds.orElse(Collections.emptyList());
    }

    @Override
    public void delete(long id) throws ResourceNotFoundException, IllegalArgumentException {
        Drone drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone not found"));
        try {
            droneRepository.delete(drone);
        } catch (IllegalArgumentException exc) {
            throw new IllegalArgumentException("Drone cannot be null");
        }
    }

    private String generateSerialNumber() {
        return new SerialNumberGenerator(30).split(6, '-').generate();
    }
}
