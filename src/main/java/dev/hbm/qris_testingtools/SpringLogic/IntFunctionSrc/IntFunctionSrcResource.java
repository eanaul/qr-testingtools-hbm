package dev.hbm.qris_testingtools.SpringLogic.IntFunctionSrc;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.DATA_FETCH_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/int-function-src")
@RequiredArgsConstructor
public class IntFunctionSrcResource extends ResponseResourceEntity<IntFunctionSrc> {
    protected final IntFunctionSrcService service;

    @GetMapping()
    public ResponseEntity<?> getByFunctionType(@RequestParam("type") FunctionTypeEnum type) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            Set<IntFunctionSrc> fetchedData = service.findByType(type);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Function Src");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
