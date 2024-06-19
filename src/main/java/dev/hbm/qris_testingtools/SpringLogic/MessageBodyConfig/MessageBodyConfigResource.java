package dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/message-body")
@RequiredArgsConstructor
public class MessageBodyConfigResource extends ResponseResourceEntity<MessageBodyConfig> {
    protected final MessageBodyConfigService service;

    @GetMapping()
    public ResponseEntity<?> getByMessageType(@RequestParam("typeId") long typeId, @RequestParam("state") MessageStateEnum state) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            Set<MessageBodyConfig> fetchedData = service.findAllNestedChild(typeId, state);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Message Body Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody MessageBodyConfig reqBody) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.add(reqBody);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Message Body Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody MessageBodyConfig reqBody)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            service.update(reqBody);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Message Body Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            e.printStackTrace();
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
            httpMessage = DATA_DELETED_SUCCESSFULLY("Message Body Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
