package dev.hbm.qris_testingtools.SpringLogic.MessageBodyConfig;

import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import dev.hbm.qris_testingtools.SpringLogic.FieldConfig.FieldConfig;
import dev.hbm.qris_testingtools.SpringLogic.FieldConfig.FieldConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageBodyConfigService {
    protected final MessageBodyConfigRepository messageBodyConfigRepository;
    protected final FieldConfigService fieldConfigService;

    public Set<MessageBodyConfig> findAllNestedChild(long typeId, MessageStateEnum state) {
        return messageBodyConfigRepository.findNestedChildByMessageTypeAndState(typeId, state);
    }

    public List<MessageBodyConfig> findAllByMessageTypeAndState(long typeId, MessageStateEnum state) {
        return messageBodyConfigRepository.findAllByMessageType_IdAndState(typeId, state);
    }

    public List<MessageBodyConfig> findAllMandatoryFieldByTypeAndState(long typeId, MessageStateEnum state ) {
        return messageBodyConfigRepository.findAllMandatoryFieldByTypeAndState(typeId, state);
    }

    public void add(MessageBodyConfig reqBody) {
        FieldConfig fieldConfig = fieldConfigService.validateId(reqBody.getFieldConfig().getId());

        reqBody.setPath(getParentPath(reqBody, fieldConfig.getFieldName()));
        messageBodyConfigRepository.save(reqBody);
    }

    public void update(MessageBodyConfig reqBody) {
        FieldConfig fieldConfig = fieldConfigService.validateId(reqBody.getFieldConfig().getId());

        reqBody.setPath(getParentPath(reqBody, fieldConfig.getFieldName()));
        messageBodyConfigRepository.save(reqBody);
    }

    public void delete(long id) {
        messageBodyConfigRepository.deleteById(id);
        messageBodyConfigRepository.deleteAllByParentId(id);
    }

    private String getParentPath(MessageBodyConfig config, String path) {
        if (config.getParentId() != 0) {
            MessageBodyConfig parentConfig = validateId(config.getParentId());
            return getParentPath(parentConfig, parentConfig.getFieldConfig().getFieldName() + "." + path);
        }

        return path;
    }

    private MessageBodyConfig validateId(long id) {
        return messageBodyConfigRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found"));
    }
}
