package dev.hbm.qris_testingtools.SpringLogic.MessageType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageTypeService {
    protected final MessageTypeRepository messageTypeRepository;

    public List<MessageType> findAll() {
        return messageTypeRepository.findAllByOrderByNameAsc();
    }

    public List<MessageType> findAllBySchemeId(long schemeId) {
        return messageTypeRepository.findAllBySchemeConfig_IdOrderByNameAsc(schemeId);
    }

    public MessageType findById(long id) {
        return validateId(id);
    }

    public void add(MessageType reqBody) {
        messageTypeRepository.save(reqBody);
    }

    public void update(MessageType reqBody) {
        messageTypeRepository.save(reqBody);
    }

    public void delete(long id) {
        messageTypeRepository.deleteById(id);
    }

    private MessageType validateId(long id) {
        return this.messageTypeRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Existing data not found, perhaps is already deleted"));
    }

    public MessageType findBySchemeIdAndValue(long schemeId, String value) {
        return messageTypeRepository.findBySchemeConfig_IdAndValue(schemeId, value)
                .orElseThrow(() -> new RuntimeException("Message type not found"));
    }
}
