package dev.hbm.qris_testingtools.SpringLogic.IntFunction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntFunctionRepository extends JpaRepository<IntFunction, Long> {
    List<IntFunction> findAllByOrderByExpressionAsc();
}
