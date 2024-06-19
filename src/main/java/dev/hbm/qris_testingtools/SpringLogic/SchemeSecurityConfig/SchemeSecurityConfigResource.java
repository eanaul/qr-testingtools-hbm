package dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import dev.hbm.qris_testingtools.Enum.SecurityTypeEnum;
import dev.hbm.qris_testingtools.SpringLogic.SchemeConfig.SchemeConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/security-config")
@RequiredArgsConstructor
public class SchemeSecurityConfigResource extends ResponseResourceEntity<SchemeSecurityConfig> {
    protected final SchemeSecurityConfigService service;

    @GetMapping("scheme/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<SchemeSecurityConfig> fetchedData = service.findBySchemeId(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Security Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("value") String value,
            @RequestParam("securityType") SecurityTypeEnum securityType,
            @RequestParam("schemeConfig") Long schemeConfig,
            @RequestParam(name = "fileData", required = false) MultipartFile file
    ) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.add(
                    new SchemeSecurityConfig()
                            .builder()
                            .name(name)
                            .value(value)
                            .securityType(securityType)
                            .fileData(file == null ? null : file.getBytes())                            .schemeConfig(new SchemeConfig().builder().id(schemeConfig).build())
                            .build()
            );
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Security Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("value") String value,
            @RequestParam("securityType") SecurityTypeEnum securityType,
            @RequestParam("schemeConfig") Long schemeConfig,
            @RequestParam(name = "fileData", required = false) MultipartFile file
    )  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.add(
                    new SchemeSecurityConfig()
                            .builder()
                            .id(id)
                            .name(name)
                            .value(value)
                            .securityType(securityType)
                            .fileData(file == null ? null : file.getBytes())                            .schemeConfig(new SchemeConfig().builder().id(schemeConfig).build())
                            .build()
            );
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Security Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.delete(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Security Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
