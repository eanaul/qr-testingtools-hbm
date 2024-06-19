package dev.hbm.qris_testingtools.SpringLogic.FieldConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.DATA_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/field-config")
@RequiredArgsConstructor
public class FieldConfigResource extends ResponseResourceEntity<FieldConfig> {
    protected final FieldConfigService fieldConfigService;

    @GetMapping("scheme/{id}")
    public ResponseEntity<?> getByScheme(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<FieldConfig> fetchedData = fieldConfigService.findAllByScheme(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Field Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFieldConfig(@RequestBody FieldConfig fieldConfig) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            fieldConfigService.add(fieldConfig);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Field Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMessageHeaderConfig(@RequestBody FieldConfig fieldConfig)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            fieldConfigService.update(fieldConfig);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Field Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMessageHeaderConfig(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            fieldConfigService.delete(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Field Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("/schemeField/{schemeId}")
    public List<FieldConfig> getFieldConfigsBySchemeId(@PathVariable int fieldId, Long schemeId) {
        return fieldConfigService.findByFieldIdAndSchemeId(fieldId, schemeId);
    }
}
