package com.omarE505.DroneDelivery.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omarE505.DroneDelivery.utils.ModelEnum;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "weight_model")
@Data
@NoArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Column(name = "weight_value")
    private int value;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Name cannot be null")
    private ModelEnum name;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Drone> drone;

    public Model(ModelEnum name) {
        this.setValue(name);
    }

    public void setValue(ModelEnum name) {
        switch (name) {
            case LIGHT_WEIGHT:
                this.value = 100;
                this.name = ModelEnum.LIGHT_WEIGHT;
                break;
            case MIDDLE_WEIGHT:
                this.value = 200;
                this.name = ModelEnum.MIDDLE_WEIGHT;
                break;
            case CRUISER_WEIGHT:
                this.value = 300;
                this.name = ModelEnum.CRUISER_WEIGHT;
                break;
            case HEAVY_WEIGHT:
                this.value = 500;
                this.name = ModelEnum.HEAVY_WEIGHT;
                break;
            default:
                this.value = 100;
                this.name = ModelEnum.LIGHT_WEIGHT;
                break;
        }
    }

    public void setValue() {
        if (this.name != null) {
            this.setValue(this.name);
        }
    }

    public void setValue(int value) {
        this.setValue();
    }
}
