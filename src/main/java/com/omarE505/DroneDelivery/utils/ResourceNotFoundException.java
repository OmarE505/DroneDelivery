package com.omarE505.DroneDelivery.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
