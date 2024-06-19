package dev.hbm.qris_testingtools.SpringLogic.SchemeSecurityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchemeSecurityConfigService {
    protected final SchemeSecurityConfigRepository repository;

    public SchemeSecurityConfig findById(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Existing data not found"));
    }

    public List<SchemeSecurityConfig> findBySchemeId(long schemeId) {
        return repository.findAllBySchemeConfig_id(schemeId);
    }

    public void add(SchemeSecurityConfig securityConfig) {
        repository.save(securityConfig);
    }

    public void update(SchemeSecurityConfig securityConfig) {
        SchemeSecurityConfig existingData = validateId(securityConfig.getId());

        if (securityConfig.getFileData() == null) {
            securityConfig.setFileData(existingData.getFileData());
        }

        repository.save(securityConfig);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }

    protected SchemeSecurityConfig validateId(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Existing data not found"));
    }
}
