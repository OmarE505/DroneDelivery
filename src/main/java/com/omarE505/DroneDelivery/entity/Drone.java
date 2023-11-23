package com.omarE505.DroneDelivery.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.omarE505.DroneDelivery.utils.State;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "drone")
@Data
@NoArgsConstructor
@ToString(exclude = { "medications" })
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @NotNull
    @OneToOne(mappedBy = "drone", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonManagedReference
    private SerialNumber serialNumber;

    @Column(name = "battery_capacity")
    @Range(min = 0, max = 100)
    private int batteryCapacity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    @NotNull(message = "Valid model type is required")
    @JsonManagedReference
    private Model model;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "drone", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Medication> medications;

    public Drone(SerialNumber serialNumber, Model model) {
        this.serialNumber = serialNumber;
        this.model = model;
    }

}
