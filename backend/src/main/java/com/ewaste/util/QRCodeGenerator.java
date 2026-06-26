package com.ewaste.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {
    
    public static String generateQRCode(String wasteId, String qrText) {
        int width = 350;
        int height = 350;
        String fileName = wasteId + ".png";
        
        // Save to both locations to ensure static server and runtime are synced
        String[] paths = {"../qr/", "./qr/"};
        String savedRelativePath = "/qr/" + fileName;
        
        boolean saved = false;
        for (String baseDir : paths) {
            try {
                File dir = new File(baseDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                
                String filePath = baseDir + fileName;
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, width, height);
                Path path = FileSystems.getDefault().getPath(filePath);
                MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
                System.out.println("QR Code saved at: " + path.toAbsolutePath());
                saved = true;
            } catch (Exception e) {
                System.err.println("Failed to save QR to " + baseDir + ": " + e.getMessage());
            }
        }
        
        return saved ? savedRelativePath : null;
    }
}
