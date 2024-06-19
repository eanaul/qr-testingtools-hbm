package dev.hbm.qris_testingtools.SpringLogic.SchemeConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/scheme-config")
public class SchemeConfigResource extends ResponseResourceEntity<SchemeConfig> {
    protected final SchemeConfigService schemeConfigService;

    @Autowired
    public SchemeConfigResource(SchemeConfigService schemeConfigService) {
        this.schemeConfigService = schemeConfigService;
    }

    @GetMapping
    public ResponseEntity<?> getSchemeConfigService() {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<SchemeConfig> fetchedData = schemeConfigService.findAll();
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Scheme Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id")long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            SchemeConfig fetchedData = schemeConfigService.findById(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Scheme Config");
            return responseWithData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addSchemeConfig(@RequestBody SchemeConfig schemeConfig) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            schemeConfigService.add(schemeConfig);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Scheme Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateSchemeConfig(@RequestBody SchemeConfig schemeConfig)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            schemeConfigService.update(schemeConfig);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Scheme Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSchemeConfig(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            schemeConfigService.delete(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Scheme Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
