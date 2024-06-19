package dev.hbm.qris_testingtools.SpringLogic.Condition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConditionService {
    private final ConditionRepository conditionRepository;

    List<Condition> getAll(){
        return conditionRepository.findAll();
    }

    Optional<Condition> getById(Long id){
        return conditionRepository.findById(id);
    }

    Condition save(Condition condition){
        return conditionRepository.save(condition);
    }

    Condition update(Condition condition){
        return conditionRepository.save(condition);
    }

    void deleteById(Long id){
        conditionRepository.deleteById(id);
    }
}
