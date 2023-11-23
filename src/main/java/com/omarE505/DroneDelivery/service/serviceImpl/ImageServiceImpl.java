package com.omarE505.DroneDelivery.service.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.omarE505.DroneDelivery.service.ImageService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public byte[] processImage(MultipartFile imageFile) throws IOException {
        return this.compressBytes(imageFile.getBytes());
    }

    @Override
    public byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException exc) {
            throw new IOException("Could not decompress image");
        } catch (DataFormatException exc) {
            throw new DataFormatException("Data format error. Could not decompress image.");
        }
        return outputStream.toByteArray();
    }

    // compress image bytes for optimized space in the database
    private byte[] compressBytes(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException exc) {
            throw new IOException("Could not compress image");
        }
        return outputStream.toByteArray();
    }
}
