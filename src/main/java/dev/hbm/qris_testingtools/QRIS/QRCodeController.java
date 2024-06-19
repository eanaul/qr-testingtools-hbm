package dev.hbm.qris_testingtools.QRIS;

import dev.hbm.qris_testingtools.Core.Processor.QRProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/qr")
@Slf4j
public class QRCodeController {

    @Autowired
    private QRService qrCodeService;

    @Autowired
    private QRProcessor qrProcessor;


    @PostMapping("/scan")
    public ResponseEntity<String> scanQRCode(@RequestParam("file") MultipartFile file) {
        try {
            File qrFile = qrCodeService.convertToFile(file.getBytes(), file.getOriginalFilename());
            String decodedText = qrCodeService.decodeQRCode(qrFile);
            if (decodedText == null) {
                log.warn("No QR Code found in this image");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No QR code found in the image");
            } else {
                log.info("Decoded QR Image: {}", decodedText);

//                parsing didieu
                qrProcessor.parseHandler(decodedText);

                return ResponseEntity.ok(decodedText);
            }
        } catch (IOException e) {
            log.error("Cannot decode QR Image", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not decode QR code");
        }
    }
}
