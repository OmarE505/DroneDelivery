package com.omarE505.DroneDelivery.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omarE505.DroneDelivery.entity.Model;
import com.omarE505.DroneDelivery.utils.ModelEnum;

public interface ModelRepository extends JpaRepository<Model, Long>{
    Optional<Model> findByName(ModelEnum name);
}
