package dev.hbm.qris_testingtools.SpringLogic.Condition;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/condition")
@RequiredArgsConstructor
public class ConditionResource extends ResponseResourceEntity {
    private final ConditionService conditionService;

    @GetMapping
    public ResponseEntity<?> getConditions() {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<Condition> fetchedData = conditionService.getAll();
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Condition");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getConditionById(@PathVariable("id") Long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            Optional<Condition> fetchedData = conditionService.getById(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Condition");
            return responseWithData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCondition(@RequestBody Condition condition) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            conditionService.save(condition);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Condition");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCondition(@RequestBody Condition condition) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            conditionService.update(condition);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Condition");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCondition(@PathVariable("id") Long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            conditionService.deleteById(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Condition");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
