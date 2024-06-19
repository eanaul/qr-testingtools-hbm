package dev.hbm.qris_testingtools.SpringLogic.NetworkConfig;

import dev.hbm.qris_testingtools.AppConfiguration.HttpTemplate.ResponseResourceEntity;
import dev.hbm.qris_testingtools.Enum.NetworkStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.hbm.qris_testingtools.AppConfiguration.Constant.CRUDTemplateMessage.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v1/iso8583/network-config")
public class NetworkConfigResource extends ResponseResourceEntity<NetworkConfig> {
    protected final NetworkConfigService networkConfigService;

    @Autowired
    public NetworkConfigResource(NetworkConfigService networkConfigService) {
        this.networkConfigService = networkConfigService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> findById(@PathVariable long id){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            NetworkConfig fetchedData = networkConfigService.findById(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("NetworkConfig");
            return responseWithData(httpStatus, httpMessage, fetchedData);
        }catch (Exception e){
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping("scheme/{schemeId}")
    public ResponseEntity<?> fetchByScheme(@PathVariable long schemeId){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<NetworkConfig> fetchedData = networkConfigService.fetchAllByScheme(schemeId);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("NetworkConfig");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        }catch (Exception e){
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @GetMapping
    public ResponseEntity<?> fetchBySchemeAndState(@RequestParam("schemeId") long schemeId, @RequestParam("state") NetworkStateEnum state){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            List<NetworkConfig> fetchedData = networkConfigService.fetchAllBySchemeAndState(schemeId, state);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("NetworkConfig");
            return responseWithListData(httpStatus, httpMessage, fetchedData);
        }catch (Exception e){
            e.printStackTrace();
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> setConnectedNetwork(@RequestBody NetworkConfig networkConfig){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            networkConfigService.add(networkConfig);
            httpStatus = OK;
            httpMessage = DATA_ADDED_SUCCESSFULLY("NetworkConfig");
            return response(httpStatus, httpMessage);
        }catch (Exception e){
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateNetwork(@RequestBody NetworkConfig networkConfig){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            networkConfigService.update(networkConfig);
            httpStatus = OK;
            httpMessage = DATA_UPDATED_SUCCESSFULLY("NetworkConfig");
            return response(httpStatus, httpMessage);
        }catch (Exception e){
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteNetwork(@PathVariable long id){
        HttpStatus httpStatus;
        String httpMessage;
        try {
            networkConfigService.deleteById(id);
            httpStatus = OK;
            httpMessage = DATA_FETCH_SUCCESSFULLY("NetworkConfig");
            return response(httpStatus, httpMessage);
        }catch (Exception e){
            httpStatus = INTERNAL_SERVER_ERROR;
            httpMessage = e.getMessage();
            return response(httpStatus, httpMessage);
        }
    }
}
