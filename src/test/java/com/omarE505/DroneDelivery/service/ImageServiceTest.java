package com.omarE505.DroneDelivery.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.omarE505.DroneDelivery.service.serviceImpl.ImageServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {
    @Mock
    private MultipartFile mockImageFile;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void testProcessImage() throws IOException {
        // Mock image file
        byte[] imageData = "Test Image Data".getBytes();
        Mockito.when(mockImageFile.getBytes()).thenReturn(imageData);

        // Perform the test
        byte[] compressedImage = imageService.processImage(mockImageFile);

        // Assertions
        assertNotNull(compressedImage);
    }

    @Test
    public void testDecompress() throws IOException, DataFormatException {
        byte[] compressedImageData = getCompressedData();

        // Perform the test
        byte[] decompressedImage = imageService.decompress(compressedImageData);

        // Assertions
        assertNotNull(decompressedImage);
    }

    @Test
    public void testDecompress_InvalidData() {
        byte[] invalidCompressedData = new byte[10]; // Invalid or incomplete compressed data

        // Perform the test and check for DataFormatException
        assertThrows(DataFormatException.class, () -> {
            imageService.decompress(invalidCompressedData);
        });
    }

    @Test
    public void testCompressBytes() throws IOException {
        // Mock image data
        byte[] imageData = "Test Image Data".getBytes();

        // Perform the test
        byte[] compressedImageData = imageService.compressBytes(imageData);

        // Assertions
        assertNotNull(compressedImageData);
    }

    private byte[] getCompressedData() throws IOException {
        // Simulate compressed image data
        byte[] imageData = "Test Image Data".getBytes();
        Deflater deflater = new Deflater();
        deflater.setInput(imageData);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageData.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }
}
