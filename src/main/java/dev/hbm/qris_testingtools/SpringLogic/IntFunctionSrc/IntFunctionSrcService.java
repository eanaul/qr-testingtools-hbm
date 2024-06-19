package dev.hbm.qris_testingtools.SpringLogic.IntFunctionSrc;

import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class IntFunctionSrcService {
    protected final IntFunctionSrcRepository repository;

    public Set<IntFunctionSrc> findByType(FunctionTypeEnum type) {
        return repository.findByType(type);
    }
}
