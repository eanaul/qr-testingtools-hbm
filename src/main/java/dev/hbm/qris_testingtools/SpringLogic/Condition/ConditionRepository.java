package dev.hbm.qris_testingtools.SpringLogic.Condition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
}
