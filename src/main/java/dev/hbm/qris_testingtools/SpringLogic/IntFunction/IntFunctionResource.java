package dev.hbm.qris_testingtools.SpringLogic.IntFunction;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.DATA_FETCH_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/int-function")
@RequiredArgsConstructor
public class IntFunctionResource extends ResponseResourceEntity<IntFunction> {
    protected final IntFunctionService service;

    @GetMapping("list")
    public ResponseEntity<?> list() {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<IntFunction> fetchedData = service.findAll();
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Int Function");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
