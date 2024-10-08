package com.omarE505.DroneDelivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.omarE505.DroneDelivery.utils.State;

import org.hibernate.validator.constraints.Range;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_drone")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class AuditDrone extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "battery_capacity")
    @Range(min = 0, max = 100)
    private int batteryCapacity;

    @Column(name = "drone_id")
    private long droneId;

    public AuditDrone(State state, int batteryCapacity) {
        super();
        this.batteryCapacity = batteryCapacity;
        this.state = state;
    }
}
