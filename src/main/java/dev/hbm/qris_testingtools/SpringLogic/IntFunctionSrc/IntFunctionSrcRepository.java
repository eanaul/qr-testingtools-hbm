package dev.hbm.qris_testingtools.SpringLogic.IntFunctionSrc;

import dev.hbm.qris_testingtools.Enum.FunctionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IntFunctionSrcRepository extends JpaRepository<IntFunctionSrc, Long> {
    Set<IntFunctionSrc> findByType(FunctionTypeEnum type);
}
