package com.omarE505.DroneDelivery.controller;

import com.omarE505.DroneDelivery.Repository.ModelRepository;
import com.omarE505.DroneDelivery.dto.DroneDto;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.service.DroneService;
import com.omarE505.DroneDelivery.service.MedicationService;
import com.omarE505.DroneDelivery.utils.RequirementNotMetException;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;
import com.omarE505.DroneDelivery.utils.State;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/drones")
public class DroneController {

    private final DroneService droneService;

    private final MedicationService medicationService;

    private final ModelRepository modelRepository;

    public DroneController(DroneService droneService, MedicationService medicationService,
                           ModelRepository modelRepository) {
        this.droneService = droneService;
        this.medicationService = medicationService;
        this.modelRepository = modelRepository;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found All Drones"),
            @ApiResponse(responseCode = "404", description = "Didn't find drones")
    })
    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        List<Drone> drones = droneService.findAll();

        if (drones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(drones, HttpStatus.OK);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Drone registered successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Model not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Drone> register(@NotNull @Valid @RequestBody DroneDto dto) {
        try {
            Model model = modelRepository.findById(dto.getModel().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Model not found"));

            dto.setModel(model);

            Drone registeredDrone = droneService.register(dto);
            return new ResponseEntity<>(registeredDrone, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RequirementNotMetException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Drone found", responseCode = "200"),
            @ApiResponse(description = "Drone not found", responseCode = "404"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Drone> findById(@PathVariable Long id) {
        try {
            Drone foundDrone = droneService.findById(id);
            return new ResponseEntity<>(foundDrone, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Drone updated successfully", responseCode = "200"),
            @ApiResponse(description = "Drone not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Drone> update(@PathVariable Optional<Long> id,
                                        @NotNull @Valid @RequestBody DroneDto dto) {
        try {
            if (id.isPresent()) {
                Drone updatedDrone = droneService.update(id, dto);
                return new ResponseEntity<>(updatedDrone, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Drone deleted successfully", responseCode = "204"),
            @ApiResponse(description = "Drone not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input", responseCode = "400"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            droneService.delete(id);
            return new ResponseEntity<>("Deleted...", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medications loaded into drone successfully", responseCode = "200"),
            @ApiResponse(description = "Medication(s) not found", responseCode = "404"),
            @ApiResponse(description = "Invalid input", responseCode = "400"),
            @ApiResponse(description = "Requirement not met", responseCode = "422"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/load")
    public ResponseEntity<Drone> load(@RequestParam("medicationIds") List<Long> medicationIds,
                                      @RequestParam("droneId") Long droneId) {
        try {
            List<Medication> meds = new ArrayList<>();
            for (Long medicationId : medicationIds) {
                Medication med = medicationService.findById(medicationId);
                meds.add(med);
            }
            return new ResponseEntity<>(droneService.load(meds, droneId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException | RequirementNotMetException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Medications retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "Drone not found", responseCode = "404"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/medications/{droneId}")
    public ResponseEntity<List<Medication>> getMedications(@PathVariable Long droneId) {
        try {
            return new ResponseEntity<>(droneService.getMedications(droneId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Available drones found successfully", responseCode = "200"),
            @ApiResponse(description = "No available drones found", responseCode = "404"),
            @ApiResponse(description = "Internal server error", responseCode = "500")
    })
    @GetMapping("/available/{totalMedicationWeight}")
    public ResponseEntity<List<Drone>> findAvailable(@PathVariable("totalMedicationWeight") int totalWeight) {
        try {
            List<Drone> drones = droneService.findAll();

            List<Drone> filteredDrones = drones.stream()
                    .filter(drone -> drone.getState().equals(State.IDLE))
                    .filter(drone -> drone.getModel().getValue() >= totalWeight)
                    .collect(Collectors.toList());

            if (filteredDrones.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(filteredDrones, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Battery level retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "Drone not found", responseCode = "404")
    })
    @GetMapping("/batteryLevel/{droneId}")
    public ResponseEntity<Integer> checkBatteryLevel(@PathVariable Long droneId) {
        try {
            Drone drone = droneService.findById(droneId);
            return new ResponseEntity<>(drone.getBatteryCapacity(), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
