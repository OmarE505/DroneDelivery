package com.omarE505.DroneDelivery.Repository.RepositoryImpl;

import java.util.List;

import com.omarE505.DroneDelivery.Repository.AppRepository;
import com.omarE505.DroneDelivery.Repository.CustomDroneRepository;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.QDrone;
import com.omarE505.DroneDelivery.utils.State;
import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomDroneRepositoryImpl implements CustomDroneRepository {

    @Autowired
    private AppRepository appRepository;

    @Override
    public List<Drone> findAvailable(int totalWeight) {
        QDrone drone = QDrone.drone;
        JPAQuery<Drone> droneJpaQuery = appRepository.startJPAQuery(drone);
        droneJpaQuery.where(drone.state.eq(State.IDLE)
                .and(drone.model.value.goe(totalWeight)));
        return droneJpaQuery.fetch();
    }

}
