package dev.hbm.qris_testingtools.SpringLogic.IntFunction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IntFunctionService {
    protected final IntFunctionRepository repository;

    public List<IntFunction> findAll() {
        return repository.findAllByOrderByExpressionAsc();
    }
}
