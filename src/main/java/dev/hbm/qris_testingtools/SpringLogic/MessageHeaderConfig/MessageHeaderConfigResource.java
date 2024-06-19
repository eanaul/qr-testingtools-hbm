package dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
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
@RequestMapping("api/v1/iso8583/message-header")
@RequiredArgsConstructor
public class MessageHeaderConfigResource extends ResponseResourceEntity<MessageHeaderConfig> {
    protected final MessageHeaderConfigService messageHeaderConfigService;

    @GetMapping
    public ResponseEntity<?> getByMessageType(@RequestParam("typeId") long typeId, @RequestParam("state") MessageStateEnum state) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<MessageHeaderConfig> fetchedData = messageHeaderConfigService.findByHeaderConfigs(typeId, state);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("Message Header Config");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMessageHeaderConfig(@RequestBody MessageHeaderConfig messageHeaderConfig) {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            messageHeaderConfigService.add(messageHeaderConfig);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("Message Header Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateMessageHeaderConfig(@RequestBody MessageHeaderConfig messageHeaderConfig)  {
        HttpStatus httpStatus;
        String httpMessage;
        try {
            messageHeaderConfigService.update(messageHeaderConfig);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("Message Header Config");
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
            messageHeaderConfigService.delete(id);
            httpStatus = OK;
            httpMessage = DATA_DELETED_SUCCESSFULLY("Message Header Config");
            return response(httpStatus, httpMessage);
        } catch (Exception e) {
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
