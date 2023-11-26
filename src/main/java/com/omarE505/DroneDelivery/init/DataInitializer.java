package com.omarE505.DroneDelivery.init;

import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.dto.MedicationDto;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.service.DroneService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.ModelEnum;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;

@Component
public class DataInitializer implements ApplicationRunner {
    
    private final DroneService droneService;

    private final MedicationService medicationService;

    public DataInitializer(DroneService droneService, MedicationService medicationService) {
        this.droneService = droneService;
        this.medicationService = medicationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeDrones();
        initializeMedications();
    }
    
    private void initializeDrones() {
        DroneDto droneDto1 = new DroneDto();
        DroneDto droneDto2 = new DroneDto();
        DroneDto droneDto3 = new DroneDto();
        DroneDto droneDto4 = new DroneDto();

        droneDto1.setModel(new Model(ModelEnum.LIGHT_WEIGHT));
        droneDto2.setModel(new Model(ModelEnum.CRUISER_WEIGHT));
        droneDto3.setModel(new Model(ModelEnum.MIDDLE_WEIGHT));
        droneDto4.setModel(new Model(ModelEnum.HEAVY_WEIGHT));
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
