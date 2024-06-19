package dev.hbm.qris_testingtools.SpringLogic.HeaderConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class HeaderConfigService {
    protected final HeaderConfigRepository repository;

    public List<HeaderConfig> findAllByScheme(long id) {
        return repository.findAllBySchemeConfig_idOrderByIdAsc(id);
    }

    public void add(HeaderConfig reqBody) {
        repository.save(reqBody);
    }

    public void update(HeaderConfig reqBody) {
        repository.save(reqBody);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
