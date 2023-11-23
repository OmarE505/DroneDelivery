package com.omarE505.DroneDelivery.service;

import java.io.IOException;
import java.util.zip.DataFormatException;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    public byte[] processImage(MultipartFile imageFile) throws IOException;

    public byte[] decompress(byte[] data) throws IOException, DataFormatException;

}
