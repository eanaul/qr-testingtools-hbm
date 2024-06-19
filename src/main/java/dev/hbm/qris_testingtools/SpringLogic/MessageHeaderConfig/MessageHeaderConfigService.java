package dev.hbm.qris_testingtools.SpringLogic.MessageHeaderConfig;

import dev.hbm.qris_testingtools.Enum.MessageStateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageHeaderConfigService {
    protected final MessageHeaderConfigRepository repository;

//    public Set<MessageHeaderConfig> findAllNestedChild(long typeId, MessageStateEnum state) {
//        return repository.findAllByMessageType_IdAndStateOrderBySequenceAsc(typeId, state);
//    }

    public List<MessageHeaderConfig> findAllByMessageTypeAndState(long typeId, MessageStateEnum state) {
        return repository.findAllByMessageType_IdAndState(typeId, state);
    }

    public List<String> findAllMandatoryFieldByTypeAndState(long typeId, MessageStateEnum state ) {
        return repository.findAllMandatoryFieldByTypeAndState(typeId, state);
    }

    public List<MessageHeaderConfig> findAll() {
        return repository.findAllByOrderByIdAsc();
    }

    public List<MessageHeaderConfig> findByHeaderConfigs(long id, MessageStateEnum state) {
        return repository.findAllByMessageType_IdAndStateOrderBySequenceAsc(id, state);
    }

    public void add(MessageHeaderConfig messageHeaderConfig) {
        validateUniqueConstraint(messageHeaderConfig);
        repository.save(messageHeaderConfig);
    }

    public void update(MessageHeaderConfig messageHeaderConfig){
        this.validateShcemeConfigId(messageHeaderConfig.getId());
        validateUniqueConstraint(messageHeaderConfig);
        repository.save(messageHeaderConfig);
    }

    public void delete(Long id){
        repository.deleteById(id);
    }

    protected void validateShcemeConfigId(long id) {
        this.repository.findById(id).orElseThrow(()
                -> new RuntimeException("Existing data not found, perhaps is already deleted"));
    }

    protected void validateUniqueConstraint(MessageHeaderConfig messageHeaderConfig) {
        // Retrieve configurations with the same level, messageType, state, and parentId
//        List<MessageHeaderConfig> duplicateConfigs = repository.findByLevelAndMessageTypeAndStateAndParentId(
//                messageHeaderConfig.getLevel(),
//                messageHeaderConfig.getMessageType(),
//                messageHeaderConfig.getState(),
//                messageHeaderConfig.getParentId());
//
//        // Check each retrieved configuration
//        for (MessageHeaderConfig config : duplicateConfigs) {
//            // Check if all attributes are identical
//            if (config.getSequence().equals(messageHeaderConfig.getSequence()) &&
//                    config.getDerType().equals(messageHeaderConfig.getDerType()) &&
//                    config.getDefaultValue().equals(messageHeaderConfig.getDefaultValue()) &&
//                    config.isUseFunction() == messageHeaderConfig.isUseFunction() &&
//                    config.getArgsValue().equals(messageHeaderConfig.getArgsValue())) {
//                throw new RuntimeException("Duplicate configuration found.");
//            }
//        }
    }
}
