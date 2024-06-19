package dev.hbm.qris_testingtools.SpringLogic.MessageType;

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
@RequestMapping("api/v1/iso8583/message-type")
@RequiredArgsConstructor
public class MessageTypeResource extends ResponseResourceEntity<MessageType> {
    protected final MessageTypeService messageTypeService;

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            MessageType fetchedData = messageTypeService.findById(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Message Type");
            return responseWithData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("scheme/{id}")
    public ResponseEntity<?> getByMessageType(@PathVariable("id") long id) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<MessageType> fetchedData = messageTypeService.findAllBySchemeId(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Message Type");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody MessageType reqBody) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            messageTypeService.add(reqBody);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Message Type");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody MessageType reqBody)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            messageTypeService.update(reqBody);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Message Type");
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
            messageTypeService.delete(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Message Type");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
