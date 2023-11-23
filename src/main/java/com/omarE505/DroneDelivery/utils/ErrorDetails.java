package com.omarE505.DroneDelivery.utils;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private Date timestamp;
    private String message;
    private String details;
}
