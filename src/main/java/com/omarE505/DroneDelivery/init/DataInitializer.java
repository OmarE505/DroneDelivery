package com.omarE505.DroneDelivery.init;

import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.repository.ModelRepository;
import com.omarE505.DroneDelivery.service.DroneService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ModelEnum;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;

@Component
public class DataInitializer implements ApplicationRunner {
    
    private final DroneService droneService;

    private final MedicationService medicationService;

    private final ModelRepository modelRepository;

    public DataInitializer(DroneService droneService, MedicationService medicationService, ModelRepository modelRepository) {
        this.droneService = droneService;
        this.medicationService = medicationService;
        this.modelRepository = modelRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeModel();
        initializeDrones();
        initializeMedications();
    }

    private void initializeModel() {
        try{
            if (modelRepository.findByName(ModelEnum.LIGHT_WEIGHT).isEmpty()) {
                modelRepository.save(new Model(ModelEnum.LIGHT_WEIGHT));
            }

            if (modelRepository.findByName(ModelEnum.CRUISER_WEIGHT).isEmpty()) {
                modelRepository.save(new Model(ModelEnum.CRUISER_WEIGHT));
            }

            if (modelRepository.findByName(ModelEnum.MIDDLE_WEIGHT).isEmpty()) {
                modelRepository.save(new Model(ModelEnum.MIDDLE_WEIGHT));
            }

            if (modelRepository.findByName(ModelEnum.HEAVY_WEIGHT).isEmpty()) {
                modelRepository.save(new Model(ModelEnum.HEAVY_WEIGHT));
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeDrones() {
        Model model1 = modelRepository.findByName(ModelEnum.LIGHT_WEIGHT)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        Model model2 = modelRepository.findByName(ModelEnum.CRUISER_WEIGHT)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        Model model3 = modelRepository.findByName(ModelEnum.MIDDLE_WEIGHT)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        Model model4 = modelRepository.findByName(ModelEnum.HEAVY_WEIGHT)
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
        

        DroneDto droneDto1 = new DroneDto();
        DroneDto droneDto2 = new DroneDto();
        DroneDto droneDto3 = new DroneDto();
        DroneDto droneDto4 = new DroneDto();

        droneDto1.setModel(model1);
        droneDto2.setModel(model2);
        droneDto3.setModel(model3);
        droneDto4.setModel(model4);
        try {
            droneService.register(droneDto1);
            droneService.register(droneDto2);
            droneService.register(droneDto3);
            droneService.register(droneDto4);
        } catch (IllegalArgumentException | RequirementNotMetException e) {
            e.printStackTrace();
        }
    }
    
    private void initializeMedications() {
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            MedicationDto med = new MedicationDto();
            med.setName("med" + i);
            int randomNumber = random.nextInt(9);
            int result = 20 + randomNumber * 10;
            med.setWeight(result);

            try{
                medicationService.save(med);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

}
