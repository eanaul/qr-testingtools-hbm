package dev.hbm.qris_testingtools.QRIS;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class TLVToQR {

    public static void main(String[] args) {
        String text = "0002110918ReyhanAuliaTreeana0107PPLGXI20208Cicurug6";

        try {
            generateQRCodeImage(text, 350, 350, "D:/QR/" + text + ".png");
            System.out.println("QR Code berhasil dibuat.");
        } catch (WriterException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.MARGIN, 0);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(new String(text), BarcodeFormat.QR_CODE, width, height, hintMap);
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
