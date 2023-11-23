package com.omarE505.DroneDelivery.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "medication")
@Data
@NoArgsConstructor
@ToString(exclude = { "drone" })
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.PRIVATE)
    private long id;

    @Column(name = "name")
    @Pattern(regexp = "^[\\w-]+$")
    @NotBlank(message = "Valid name is required")
    private String name;

    @Column(name = "weight")
    @NotNull(message = "Weight is required")
    private int weight;

    @Column(name = "code")
    @Pattern(regexp = "^[A-Z0-9_]{11,15}$")
    @NotBlank(message = "Valid code is required")
    private String code;

    @Column(name = "image", columnDefinition = "BLOB")
    @Lob
    private byte[] image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "drone_id")
    @JsonBackReference
    private Drone drone;

    public Medication(String name, int weight, String code) {
        this.name = name;
        this.weight = weight;
        this.code = code;
    }

}
