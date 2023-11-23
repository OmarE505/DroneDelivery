package com.omarE505.DroneDelivery.Repository.RepositoryImpl;

import java.util.Optional;

import com.omarE505.DroneDelivery.Repository.AppRepository;
import com.omarE505.DroneDelivery.Repository.CustomMedRepository;
import com.omarE505.DroneDelivery.entity.Drone;
import com.omarE505.DroneDelivery.entity.Medication;
import com.omarE505.DroneDelivery.entity.QMedication;
import com.omarE505.DroneDelivery.utils.ResourceNotFoundException;
import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomMedRepositoryImpl implements CustomMedRepository {

    @Autowired
    private AppRepository appRepository;

    @Override
    public Drone checkLoadedMedications(long droneId) throws ResourceNotFoundException {
        QMedication medication = QMedication.medication;
        JPAQuery<Medication> mJpaQuery = appRepository.startJPAQuery(medication);
        Optional<Medication> med = Optional.ofNullable(mJpaQuery.where(medication.drone.id.eq(droneId)).fetchFirst());
        return med.orElseThrow(() -> new ResourceNotFoundException("Drone could not be found")).getDrone();
    }

}
