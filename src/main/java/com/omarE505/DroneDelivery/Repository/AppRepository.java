package com.omarE505.DroneDelivery.Repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;

public interface AppRepository {

    <E> JPAQuery<E> startJPAQuery(EntityPath<E> entityPath);

}
