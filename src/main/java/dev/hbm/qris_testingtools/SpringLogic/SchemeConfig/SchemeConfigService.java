package dev.hbm.qris_testingtools.SpringLogic.SchemeConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SchemeConfigService {
    protected final SchemeConfigRepository schemeConfigRepository;

    @Autowired
    public SchemeConfigService(SchemeConfigRepository schemeConfigRepository) {
        this.schemeConfigRepository = schemeConfigRepository;
    }

    public List<SchemeConfig> findAll() {
        return schemeConfigRepository.findAllByOrderByIdAsc();
    }

    public SchemeConfig findById(long id) {
        return validateSchemeConfigId(id);
    }

    public void add(SchemeConfig schemeConfig) {
        this.validateSchemeConfigName(schemeConfig.getName(), 0L);
        schemeConfigRepository.save(schemeConfig);
    }

    public void update(SchemeConfig schemeConfig){
        this.validateSchemeConfigId(schemeConfig.getId());
        schemeConfigRepository.save(schemeConfig);
    }

    public void delete(Long id){
        schemeConfigRepository.deleteById(id);
    }

    protected SchemeConfig validateSchemeConfigId(long id) {
        return this.schemeConfigRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Existing data not found, perhaps is already deleted"));
    }

    protected void validateSchemeConfigName(String name, long id) {
        this.schemeConfigRepository.findByName(name).ifPresent(existingData -> {
            if (existingData.getId() != id) {
                throw new RuntimeException("Name already exist");
            }
        });
    }
}
