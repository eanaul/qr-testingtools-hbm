package dev.hbm.qris_testingtools.SpringLogic.HeaderConfig;

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
@RequestMapping("api/v1/iso8583/header-config")
@RequiredArgsConstructor
public class HeaderConfigResource extends ResponseResourceEntity<HeaderConfig> {
    protected final HeaderConfigService service;

    @GetMapping("scheme/{id}")
    public ResponseEntity<?> getByScheme(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<HeaderConfig> fetchedData = service.findAllByScheme(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Header Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody HeaderConfig headerConfig) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.add(headerConfig);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Header Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody HeaderConfig headerConfig)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.update(headerConfig);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Header Config");
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
            httpMessage = DATA_DELETED_SUCCESSFULLY("Header Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
