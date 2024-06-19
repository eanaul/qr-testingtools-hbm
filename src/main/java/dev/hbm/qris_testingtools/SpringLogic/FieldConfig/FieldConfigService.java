package dev.hbm.qris_testingtools.SpringLogic.FieldConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldConfigService {
    protected final FieldConfigRepository fieldConfigRepository;

    public List<FieldConfig> findAll() {
        return fieldConfigRepository.findAllByOrderByIdAsc();
    }

    public List<FieldConfig> findAllByScheme(long id) {
        return fieldConfigRepository.findAllBySchemeConfig_IdOrderByIdAsc(id);
    }

    public List<FieldConfig> findByFieldId(int id) {
        return fieldConfigRepository.findByFieldId(id);
    }

    public void add(FieldConfig fieldConfig) {
        validateFieldNameAndSchemeConfig(0L, fieldConfig.getSchemeConfig().getId(), fieldConfig.getFieldName());
        fieldConfigRepository.save(fieldConfig);
    }

    public void update(FieldConfig fieldConfig) {
        this.validateShcemeConfigId(fieldConfig.getId());
        validateFieldNameAndSchemeConfig(fieldConfig.getId(), fieldConfig.getSchemeConfig().getId(), fieldConfig.getFieldName());
        fieldConfigRepository.save(fieldConfig);
    }

    public void delete(Long id) {
        fieldConfigRepository.deleteById(id);
    }

    public FieldConfig validateId(long id) {
        return fieldConfigRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Existing data not found, perhaps is already deleted"));
    }

    protected void validateShcemeConfigId(long id) {
        this.fieldConfigRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Existing data not found, perhaps is already deleted"));
    }

    public void validateFieldNameAndSchemeConfig(long id, long schemeId, String fName) {
        fieldConfigRepository.findByFieldNameAndSchemeConfig_Id(fName, schemeId)
                .ifPresent(v1 -> {
                    if (v1.getId() != id)
                        throw new RuntimeException("Data already exist");
                });
    }

    public List<FieldConfig> findDuplicateFieldConfigs() {
        return fieldConfigRepository.findDuplicates();
    }

    public List<FieldConfig> findByFieldIdAndSchemeId(int fieldId, Long schemeId) {
        return fieldConfigRepository.findByFieldIdAndSchemeId(fieldId, schemeId);
    }

    public List<FieldConfig> findDuplicatesInSchemeId(long schemeId){
        return fieldConfigRepository.findDuplicatesBySchemeId(schemeId);
    }
}
