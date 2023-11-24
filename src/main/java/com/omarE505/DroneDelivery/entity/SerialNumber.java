package com.omarE505.DroneDelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "serial_number")
@Data
@NoArgsConstructor
public class SerialNumber {

    @Id
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @NotBlank(message = "Valid serial number is required")
    @Size(max = 100)
    private String serialValue;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private Drone drone;

    public SerialNumber(String value) {
        this.serialValue = value;
    }

}
