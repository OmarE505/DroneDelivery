package com.omarE505.DroneDelivery.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.entity.SerialNumber;
import com.omarE505.DroneDelivery.utils.State;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneDto {

    @NotNull()
    private Model model;

    private Long id;

    private SerialNumber serialNumber;

    private int batteryCapacity;

    private State state;

    private List<Medication> medications;

}
